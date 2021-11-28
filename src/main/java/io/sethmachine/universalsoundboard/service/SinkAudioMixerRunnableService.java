package io.sethmachine.universalsoundboard.service;

import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerWiringDAO;
import io.sethmachine.universalsoundboard.db.model.audiomixer.AudioMixerRow;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinkAudioMixerRunnableService {

  private static final Logger LOG = LoggerFactory.getLogger(
    SinkAudioMixerRunnableService.class
  );

  private final AudioMixerDAO audioMixerDAO;
  private final AudioMixerWiringDAO audioMixerWiringDAO;
  private final SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory;
  private final ThreadPoolExecutor sinkThreadPoolExecutor;

  @Inject
  public SinkAudioMixerRunnableService(
    AudioMixerDAO audioMixerDAO,
    AudioMixerWiringDAO audioMixerWiringDAO,
    SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory,
    @Named("SinkThreadPoolExecutor") ThreadPoolExecutor sinkThreadPoolExecutor
  ) {
    this.audioMixerDAO = audioMixerDAO;
    this.audioMixerWiringDAO = audioMixerWiringDAO;
    this.sinkAudioMixerRunnableFactory = sinkAudioMixerRunnableFactory;
    this.sinkThreadPoolExecutor = sinkThreadPoolExecutor;
  }

  public void startSink(int sinkId) {
    AudioMixerRow sinkRow = validateAndGetSink(sinkId);
    SinkAudioMixerRunnable sinkAudioMixerRunnable = sinkAudioMixerRunnableFactory.create(
      sinkId
    );
    sinkThreadPoolExecutor.execute(sinkAudioMixerRunnable);
  }

  public void stopSink(int sinkId) {
    AudioMixerRow sinkRow = validateAndGetSink(sinkId);
  }

  public int getTotalActiveSinks() {
    return this.sinkThreadPoolExecutor.getActiveCount();
  }

  private AudioMixerRow validateAndGetSink(int sinkId) {
    Optional<AudioMixerRow> sinkRow = audioMixerDAO.get(sinkId);
    if (sinkRow.isEmpty()) {
      throw new NotFoundException(
        String.format(
          "No such sink audio mixer exists in the audio mixer table: id %d",
          sinkId
        )
      );
    }
    return sinkRow.get();
  }
}
