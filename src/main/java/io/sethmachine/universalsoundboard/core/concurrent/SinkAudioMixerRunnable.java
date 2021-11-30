package io.sethmachine.universalsoundboard.core.concurrent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioMixerMetadataUtil;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.model.audiomixer.AudioMixerRow;
import io.sethmachine.universalsoundboard.service.api.AudioMixerMetadataApiService;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.TargetDataLine;
import javax.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinkAudioMixerRunnable implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(SinkAudioMixerRunnable.class);
  // sets the buffer size of frames read in to 1/BUFFER_SIZE
  // see: https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/chapter5.html
  private static final int BUFFER_SIZE = 5;

  private final SinkAudioMixer sink;

  private boolean stopped = false;

  @Inject
  public SinkAudioMixerRunnable(@Assisted SinkAudioMixer sink) {
    this.sink = sink;
  }

  @Override
  public void run() {
    try {
      runSink(sink);
    } catch (LineUnavailableException e) {
      LOG.error("Target data line not available for this sink: {}", sink, e);
      e.printStackTrace();
    }
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

    while (!stopped) {
      // Read the next chunk of data from the TargetDataLine.
      numBytesRead = targetDataLine.read(data, 0, data.length);
      LOG.debug(
        "Read {} bytes: sink ID {}",
        numBytesRead,
        sink.getAudioMixerDescription()
      );
    }
  }
}
