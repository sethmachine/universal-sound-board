package io.sethmachine.universalsoundboard.core.concurrent;

import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
import io.sethmachine.universalsoundboard.db.audiomixer.AudioMixerRow;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.service.AudioMixerMetadataService;

public class SinkAudioMixerRunnable implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(
      SinkAudioMixerRunnable.class
  );
  // sets the buffer size of frames read in to 1/BUFFER_SIZE
  // see: https://docs.oracle.com/javase/8/docs/technotes/guides/sound/programmer_guide/chapter5.html
  private static final int BUFFER_SIZE = 5;

  private final int sinkId;
  private final AudioMixerDAO audioMixerDao;
  private final AudioMixerMetadataService audioMixerMetadataService;

  private boolean stopped = false;

  @Inject
  public SinkAudioMixerRunnable(AudioMixerDAO audioMixerDao,
                                AudioMixerMetadataService audioMixerMetadataService,
                                @Assisted int sinkId){
    this.audioMixerDao = audioMixerDao;
    this.audioMixerMetadataService = audioMixerMetadataService;
    this.sinkId = sinkId;
  }

  @Override
  public void run() {
    AudioMixerRow sinkRow = audioMixerDao.get(sinkId).orElseThrow();
    AudioMixerMetadataQuery query = AudioMixerMetadataQuery.builder()
        .setAudioMixerName(sinkRow.getName())
        .setAudioMixerType(AudioMixerType.SINK)
        .build();
    Mixer sinkMixer = audioMixerMetadataService.findSingleMixerMatchingQuery(
        query).orElseThrow(() ->
        new NotFoundException(String.format("No such audio mixer found in the AudioSystem: query %s", query
            )));

    try {
      runSink(sinkRow, sinkMixer);
    } catch (LineUnavailableException e) {
      LOG.error("Target data line not available for this sink: sink ID {}", sinkId, e);
      e.printStackTrace();
    }
  }

  private void runSink(AudioMixerRow sinkRow, Mixer sinkMixer) throws LineUnavailableException {
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
