package io.sethmachine.universalsoundboard.core.concurrent.source;

import static io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil.handleUnspecifiedValuesInAudioFormat;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceAudioMixerRunnable implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(
    SourceAudioMixerRunnable.class
  );
  private static final int BUFFER_SIZE = 5;

  private final EventBus eventBus;
  private final SourceAudioMixer source;
  private final AudioBytesConsumer audioBytesConsumer;

  private boolean stopped = false;

  @Inject
  public SourceAudioMixerRunnable(
    @Named("SourceEventBus") EventBus eventBus,
    @Assisted SourceAudioMixer source,
    @Assisted AudioBytesConsumer audioBytesConsumer
  ) {
    this.eventBus = eventBus;
    this.eventBus.register(this);
    this.source = source;
    this.audioBytesConsumer = audioBytesConsumer;
  }

  @Subscribe
  public void listen(StopSourceEvent stopSourceEvent) {
    if (stopSourceEvent.getSourceId() == source.getAudioMixerId()) {
      LOG.info("Received a request to stop capturing from this source: {}", source);
      this.stopped = true;
    }
  }

  @Override
  public void run() {
    try {
      captureFromSource(source);
    } catch (LineUnavailableException e) {
      LOG.error(
        "Target data line not available for capturing from source: {}",
        source,
        e
      );
      e.printStackTrace();
    } finally {
      closeConsumerIfCloseable();
      eventBus.unregister(this);
    }
  }

  private void captureFromSource(SourceAudioMixer source)
    throws LineUnavailableException {
    AudioFormat sourceAudioFormat = handleUnspecifiedValuesInAudioFormat(
      source.getAudioFormat()
    );

    TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(
      sourceAudioFormat,
      source.getMixer().getMixerInfo()
    );

    targetDataLine.open(sourceAudioFormat);
    targetDataLine.start();

    byte[] data = new byte[targetDataLine.getBufferSize() / BUFFER_SIZE];
    int numBytesRead;

    LOG.info(
      "Started capturing audio from source: {}",
      source.getAudioMixerDescription()
    );

    while (!stopped) {
      numBytesRead = targetDataLine.read(data, 0, data.length);
      LOG.debug(
        "Captured {} bytes from source ID {}",
        numBytesRead,
        source.getAudioMixerDescription()
      );

      audioBytesConsumer.accept(data, 0, numBytesRead);
    }

    LOG.info("Stopping capture from source: {}", source.getAudioMixerDescription());
    cleanUp(targetDataLine);
  }

  private void cleanUp(TargetDataLine targetDataLine) {
    targetDataLine.stop();
    targetDataLine.close();
    targetDataLine.flush();
  }

  private void closeConsumerIfCloseable() {
    if (audioBytesConsumer instanceof java.io.Closeable) {
      try {
        ((java.io.Closeable) audioBytesConsumer).close();
        LOG.info("Closed audio bytes consumer");
      } catch (Exception e) {
        LOG.error("Failed to close audio bytes consumer", e);
      }
    }
  }
}
