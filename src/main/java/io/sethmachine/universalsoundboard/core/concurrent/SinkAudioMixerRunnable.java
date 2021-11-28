package io.sethmachine.universalsoundboard.core.concurrent;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
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

  private final int sinkId;
  private final AudioMixerDAO audioMixerDao;
  private final AudioMixerMetadataUtil audioMixerMetadataUtil;

  private boolean stopped = false;

  @Inject
  public SinkAudioMixerRunnable(
    AudioMixerDAO audioMixerDao,
    AudioMixerMetadataUtil audioMixerMetadataUtil,
    @Assisted int sinkId
  ) {
    this.audioMixerDao = audioMixerDao;
    this.audioMixerMetadataUtil = audioMixerMetadataUtil;
    this.sinkId = sinkId;
  }

  @Override
  public void run() {
    AudioMixerRow sinkRow = audioMixerDao.get(sinkId).orElseThrow();
    AudioMixerMetadataQuery query = AudioMixerMetadataQuery
      .builder()
      .setAudioMixerName(sinkRow.getName())
      .setAudioMixerType(AudioMixerType.SINK)
      .build();
    Mixer sinkMixer = audioMixerMetadataUtil
      .findFirstMixerMatchingQuery(query)
      .orElseThrow(() ->
        new NotFoundException(
          String.format("No such audio mixer found in the AudioSystem: query %s", query)
        )
      );

    try {
      runSink(sinkRow, sinkMixer);
    } catch (LineUnavailableException e) {
      LOG.error("Target data line not available for this sink: sink ID {}", sinkId, e);
      e.printStackTrace();
    }
  }

  private void runSink(AudioMixerRow sinkRow, Mixer sinkMixer)
    throws LineUnavailableException {
    TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(
      sinkRow.getAudioFormat(),
      sinkMixer.getMixerInfo()
    );

    targetDataLine.open(sinkRow.getAudioFormat());
    targetDataLine.start();
    int numBytesRead;
    byte[] data = new byte[targetDataLine.getBufferSize() / 5];

    while (!stopped) {
      // Read the next chunk of data from the TargetDataLine.
      numBytesRead = targetDataLine.read(data, 0, data.length);
      LOG.debug("Read {} bytes: sink ID {}", numBytesRead, sinkRow.getId());
    }
  }
}
