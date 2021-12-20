package io.sethmachine.universalsoundboard.core.concurrent;

import com.google.common.base.Predicates;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.sink.StopSinkEvent;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import java.util.List;
import java.util.stream.Collectors;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinkAudioMixerRunnable implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(SinkAudioMixerRunnable.class);
  // sets the buffer size of frames read in to 1/BUFFER_SIZE
  // see: https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/chapter5.html
  private static final int BUFFER_SIZE = 5;

  private final EventBus eventBus;
  private final SinkAudioMixer sink;
  private final List<SourceAudioMixer> sources;

  private boolean stopped = false;

  @Inject
  public SinkAudioMixerRunnable(
    @Named("SinkEventBus") EventBus eventBus,
    @Assisted SinkAudioMixer sink,
    @Assisted List<SourceAudioMixer> sources
  ) {
    this.eventBus = eventBus;
    this.eventBus.register(this);
    this.sink = sink;
    this.sources = sources;
  }

  @Subscribe
  public void listen(StopSinkEvent stopSinkEvent) {
    if (stopSinkEvent.getSinkId() == sink.getAudioMixerId()) {
      LOG.info("Received a request to stop this sink: {}", sink);
      this.stopped = true;
    }
  }

  @Override
  public void run() {
    try {
      runSink(sink);
    } catch (LineUnavailableException e) {
      LOG.error("Target data line not available for this sink: {}", sink, e);
      e.printStackTrace();
    }
    // avoid dangling references to this thread even if the thread is stopped
    eventBus.unregister(this);
  }

  private void runSink(SinkAudioMixer sink) throws LineUnavailableException {
    TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(
      sink.getAudioFormat(),
      sink.getMixer().getMixerInfo()
    );

    targetDataLine.open(sink.getAudioFormat());
    targetDataLine.start();
    int numBytesRead;
    byte[] data = new byte[targetDataLine.getBufferSize() / 5];

    //    SourceDataLine toMixerSourceDataLine = AudioSystem.getSourceDataLine(
    //        toMixerAudioFormat,
    //        toMixer.getMixerInfo()
    //    );
    //    toMixerSourceDataLine.open(fromMixerAudioFormat);
    //    toMixerSourceDataLine.start();
    //    toMixerSourceDataLine.write(data, 0, numBytesRead);

    List<SourceDataLine> sourceDataLines = getOpenStartSourceDataLines(sources);

    while (!stopped) {
      // Read the next chunk of data from the TargetDataLine.
      numBytesRead = targetDataLine.read(data, 0, data.length);
      LOG.debug(
        "Read {} bytes: sink ID {}",
        numBytesRead,
        sink.getAudioMixerDescription()
      );
      for (SourceDataLine sourceDataLine : sourceDataLines) {
        sourceDataLine.write(data, 0, numBytesRead);
        LOG.debug("Wrote {} bytes to source", numBytesRead);
      }
    }
    // clean up if we ever stop
    cleanUp(targetDataLine, sourceDataLines);
  }

  private List<SourceDataLine> getOpenStartSourceDataLines(
    List<SourceAudioMixer> sources
  ) {
    return sources
      .stream()
      .map(source -> {
        try {
          SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(
            source.getAudioFormat(),
            source.getMixer().getMixerInfo()
          );
          sourceDataLine.open(sink.getAudioFormat());
          sourceDataLine.start();
          return sourceDataLine;
        } catch (LineUnavailableException e) {
          LOG.error("Failed to get source data line for source: {}", source, e);
          e.printStackTrace();
        }
        return null;
      })
      .filter(Predicates.notNull())
      .collect(Collectors.toList());
  }

  /**
   * Close any open target/source data lines.
   */
  private void cleanUp(
    TargetDataLine targetDataLine,
    List<SourceDataLine> sourceDataLines
  ) {
    targetDataLine.stop();
    targetDataLine.close();
    targetDataLine.flush();
    for (SourceDataLine sd : sourceDataLines) {
      sd.stop();
      sd.close();
      sd.flush();
    }
  }
}
