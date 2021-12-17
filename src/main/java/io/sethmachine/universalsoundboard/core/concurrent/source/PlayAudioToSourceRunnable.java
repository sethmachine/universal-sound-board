package io.sethmachine.universalsoundboard.core.concurrent.source;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.concurrent.source.AudioFileStream;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayAudioToSourceRunnable implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(
    PlayAudioToSourceRunnable.class
  );
  // sets the buffer size of frames read in to 1/BUFFER_SIZE
  // see: https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/chapter5.html
  private static final int BUFFER_SIZE = 5;

  private final EventBus eventBus;
  private final SourceAudioMixer source;
  private final AudioFileStream audioFileStream;

  private boolean stopped = false;

  @Inject
  public PlayAudioToSourceRunnable(
    @Named("SourceEventBus") EventBus eventBus,
    @Assisted SourceAudioMixer source,
    @Assisted AudioFileStream audioFileStream
  ) {
    this.eventBus = eventBus;
    this.eventBus.register(this);
    this.source = source;
    this.audioFileStream = audioFileStream;
  }

  //  @Subscribe
  //  public void listen(StopSinkEvent stopSinkEvent) {
  //    if (stopSinkEvent.getSinkId() == sink.getAudioMixerId()) {
  //      LOG.info("Received a request to stop this sink: {}", sink);
  //      this.stopped = true;
  //    }
  //  }

  @Override
  public void run() {
    try {
      playAudio();
    } catch (LineUnavailableException | IOException e) {
      LOG.error("Source data line not available for this source: {}", source, e);
      e.printStackTrace();
    }
    // avoid dangling references to this thread even if the thread is stopped
    eventBus.unregister(this);
  }

  private void playAudio() throws LineUnavailableException, IOException {
    SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(
      source.getAudioFormat(),
      source.getMixer().getMixerInfo()
    );

    sourceDataLine.open(source.getAudioFormat());
    sourceDataLine.start();
    int numBytesRead;
    byte[] data = new byte[sourceDataLine.getBufferSize() / 5];
    int numBytesToRead = sourceDataLine.getBufferSize() / 5;
    int total = 0;
    int totalToRead = audioFileStream.getAudioInputStream().available();

    while (total < totalToRead) {
      numBytesRead = audioFileStream.getAudioInputStream().read(data, 0, numBytesToRead);
      LOG.info("Read {} bytes from {}", numBytesRead, audioFileStream.getFilename());
      if (numBytesRead == -1) break;
      total += numBytesRead;
      sourceDataLine.write(data, 0, numBytesRead);
    }
    sourceDataLine.drain();
    cleanUp(sourceDataLine);
  }

  /**
   * Close the source data line after finishing play back.
   */
  private void cleanUp(SourceDataLine sourceDataLine) {
    sourceDataLine.stop();
    sourceDataLine.close();
    sourceDataLine.flush();
  }
}
