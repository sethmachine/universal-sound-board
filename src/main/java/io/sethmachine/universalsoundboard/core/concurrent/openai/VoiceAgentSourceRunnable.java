package io.sethmachine.universalsoundboard.core.concurrent.openai;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;

import io.sethmachine.universalsoundboard.core.concurrent.source.StopSourceEventIF;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.util.audiomixer.Base64AudioDecoder;
import io.sethmachine.universalsoundboard.core.util.audiomixer.OpenAiAudioFormatConverter;

public class VoiceAgentSourceRunnable implements Runnable {

  public interface Factory {
    VoiceAgentSourceRunnable create(
        SourceAudioMixer source,
        BlockingQueue<String> outgoingAudioBytesQueue
    );
  }

  private static final Logger LOG = LoggerFactory.getLogger(
    VoiceAgentSourceRunnable.class
  );
  private static final int POLL_TIMEOUT_MS = 100;

  private final EventBus eventBus;
  private final SourceAudioMixer source;
  private final BlockingQueue<String> outgoingAudioBytesQueue;

  private volatile boolean stopped = false;

  @Inject
  public VoiceAgentSourceRunnable(
    @Named("SourceEventBus") EventBus eventBus,
    @Assisted SourceAudioMixer source,
    @Assisted BlockingQueue<String> outgoingAudioBytesQueue
  ) {
    this.eventBus = eventBus;
    this.eventBus.register(this);
    this.source = source;
    this.outgoingAudioBytesQueue = outgoingAudioBytesQueue;
  }

  @Subscribe
  public void listen(StopSourceEventIF stopSourceEvent) {
    if (stopSourceEvent.getSourceId() == source.getAudioMixerId()) {
      LOG.info("Received a request to stop playing to this source: {}", source);
      this.stopped = true;
    }
  }

  @Override
  public void run() {
    SourceDataLine sourceDataLine = null;
    try {
      sourceDataLine = AudioSystem.getSourceDataLine(
          source.getAudioFormat(),
          source.getMixer().getMixerInfo()
      );

      sourceDataLine.open(source.getAudioFormat());
      sourceDataLine.start();
      LOG.info("Started voice agent audio playback to source: {}", source);

      while (!stopped) {
        String base64AudioBytes = outgoingAudioBytesQueue.poll(POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        if (base64AudioBytes != null && !base64AudioBytes.isEmpty()) {
          try {
            byte[] pcm16Bytes = Base64AudioDecoder.decodeBase64ToPcm16Bytes(base64AudioBytes);
            if (pcm16Bytes.length > 0) {
              byte[] convertedBytes = OpenAiAudioFormatConverter.convertOpenAiPcm16ToTargetFormat(
                pcm16Bytes,
                source.getAudioFormat()
              );
              int bytesWritten = sourceDataLine.write(convertedBytes, 0, convertedBytes.length);
              LOG.trace("Wrote {} bytes to source audio line", bytesWritten);
            }
          } catch (Exception e) {
            LOG.error("Error decoding or writing audio bytes to source", e);
          }
        }
      }

      LOG.info("Voice agent audio playback stopped for source: {}", source);
    } catch (LineUnavailableException e) {
      LOG.error("Failed to open audio line for source: {}", source, e);
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      LOG.warn("Voice agent audio playback interrupted for source: {}", source);
      Thread.currentThread().interrupt();
    } finally {
      cleanUp(sourceDataLine);
    }
  }

  private void cleanUp(SourceDataLine sourceDataLine) {
    if (sourceDataLine != null) {
      try {
        sourceDataLine.drain();
        sourceDataLine.stop();
        sourceDataLine.close();
        LOG.info("Cleaned up source data line for source: {}", source);
      } catch (Exception e) {
        LOG.error("Error cleaning up source data line", e);
      }
    }
  }
}
