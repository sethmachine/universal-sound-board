package io.sethmachine.universalsoundboard.core.concurrent.openai;

import static io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil.handleUnspecifiedValuesInAudioFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.sethmachine.universalsoundboard.core.concurrent.sink.StopSinkEvent;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.util.audiomixer.OpenAiAudioFormatConverter;

public class VoiceAgentSinkRunnable implements Runnable {

  public interface Factory {
    VoiceAgentSinkRunnable create(
        SinkAudioMixer sink,
        BlockingQueue<String> incomingAudioBytes
    );
  }

  private static final Logger LOG = LoggerFactory.getLogger(
    VoiceAgentSinkRunnable.class
  );
  private static final int POLL_TIMEOUT_MS = 100;
  // sets the buffer size of frames read in to 1/BUFFER_SIZE
  // see: https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/chapter5.html
  private static final int BUFFER_SIZE = 5;

  private final EventBus eventBus;
  private final SinkAudioMixer sink;
  private final BlockingQueue<String> incomingAudioBytes;

  private volatile boolean stopped = false;
  private final ByteArrayOutputStream capturedAudioStream = new ByteArrayOutputStream();

  @Inject
  public VoiceAgentSinkRunnable(
    @Named("SinkEventBus") EventBus eventBus,
    @Assisted SinkAudioMixer sink,
    @Assisted BlockingQueue<String> incomingAudioBytes
  ) {
    this.eventBus = eventBus;
    this.eventBus.register(this);
    this.sink = sink;
    this.incomingAudioBytes = incomingAudioBytes;
  }

  @Subscribe
  public void listen(StopSinkEvent stopSinkEvent) {
    if (stopSinkEvent.getSinkId() == sink.getAudioMixerId()) {
      LOG.info("Received a request to stop playing to this source: {}", sink);
      this.stopped = true;
    }
  }

  @Override
  public void run() {
    TargetDataLine targetDataLine = null;
    try {
      targetDataLine = AudioSystem.getTargetDataLine(
          sink.getAudioFormat(),
          sink.getMixer().getMixerInfo()
      );
    } catch (LineUnavailableException e) {
      throw new RuntimeException(e);
    }

    AudioFormat sinkAudioFormat = handleUnspecifiedValuesInAudioFormat(
        sink.getAudioFormat()
    );

    try {
      targetDataLine.open(sinkAudioFormat);
    } catch (LineUnavailableException e) {
      throw new RuntimeException(e);
    }
    targetDataLine.start();
    int numBytesRead;
    byte[] data = new byte[targetDataLine.getBufferSize() / BUFFER_SIZE];
    while (!stopped) {
      numBytesRead = targetDataLine.read(data, 0, data.length);
//      LOG.info(
//          "Read {} bytes: sink ID {}",
//          numBytesRead,
//          sink.getAudioMixerDescription()
//      );

      if (numBytesRead > 0) {
        try {
          byte[] actualData = numBytesRead == data.length ? data : Arrays.copyOf(data, numBytesRead);

          capturedAudioStream.write(actualData);

          String base64Audio = OpenAiAudioFormatConverter.convertSourceFormatToOpenAiBase64Pcm16(
              actualData,
              sinkAudioFormat
          );
          boolean added = incomingAudioBytes.offer(base64Audio);
          if (!added) {
            LOG.warn("Failed to add audio bytes to queue - queue may be full");
          } else {
            LOG.trace("Added {} bytes (base64 length: {}) to incoming audio queue", numBytesRead, base64Audio.length());
          }
        } catch (Exception e) {
          LOG.error("Error converting audio bytes to OpenAI format", e);
        }
      }
    }
    // clean up if we ever stop
    cleanUp(targetDataLine, sinkAudioFormat);
  }

  private void cleanUp(TargetDataLine targetDataLine, AudioFormat audioFormat) {
    if (targetDataLine != null) {
      try {
        targetDataLine.stop();
        targetDataLine.close();
        targetDataLine.flush();
        LOG.info("Cleaned up source data line for source: {}", sink);
      } catch (Exception e) {
        LOG.error("Error cleaning up source data line", e);
      }
    }

    saveCapturedAudioToFile(audioFormat);
  }

  private void saveCapturedAudioToFile(AudioFormat audioFormat) {
    try {
      byte[] audioData = capturedAudioStream.toByteArray();
      if (audioData.length == 0) {
        LOG.warn("No audio data captured, skipping file save");
        return;
      }

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
      String timestamp = dateFormat.format(new Date());
      String sanitizedSinkName = sink.getAudioMixerDescription().getName().replaceAll("[^a-zA-Z0-9]", "_");
      String filename = String.format("capture_sink_%d_%s_%s.wav", sink.getAudioMixerId(), sanitizedSinkName, timestamp);
      File outputFile = new File(filename);

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
      long frameLength = audioData.length / audioFormat.getFrameSize();
      AudioInputStream audioInputStream = new AudioInputStream(
          byteArrayInputStream,
          audioFormat,
          frameLength
      );

      AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);
      LOG.info("Captured audio saved to file: {} ({} bytes, {} frames)",
          outputFile.getAbsolutePath(),
          audioData.length,
          frameLength);

      audioInputStream.close();
    } catch (IOException e) {
      LOG.error("Failed to save captured audio to file", e);
    }
  }
}
