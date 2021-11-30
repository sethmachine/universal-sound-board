package io.sethmachine.universalsoundboard.service;

import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerWiringDAO;
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

  private final AudioMixersService audioMixersService;
  private final AudioMixerWiringDAO audioMixerWiringDAO;
  private final SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory;
  private final ThreadPoolExecutor sinkThreadPoolExecutor;

  @Inject
  public SinkAudioMixerRunnableService(
    AudioMixersService audioMixersService,
    AudioMixerWiringDAO audioMixerWiringDAO,
    SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory,
    @Named("SinkThreadPoolExecutor") ThreadPoolExecutor sinkThreadPoolExecutor
  ) {
    this.audioMixersService = audioMixersService;
    this.audioMixerWiringDAO = audioMixerWiringDAO;
    this.sinkAudioMixerRunnableFactory = sinkAudioMixerRunnableFactory;
    this.sinkThreadPoolExecutor = sinkThreadPoolExecutor;
  }

  public void startSink(int sinkId) {
    SinkAudioMixer sink = validateAndGetSink(sinkId);
    SinkAudioMixerRunnable sinkAudioMixerRunnable = sinkAudioMixerRunnableFactory.create(
      sink
    );
    sinkThreadPoolExecutor.execute(sinkAudioMixerRunnable);
  }

  public void stopSink(int sinkId) {
    SinkAudioMixer sink = validateAndGetSink(sinkId);
  }

  public int getTotalActiveSinks() {
    return this.sinkThreadPoolExecutor.getActiveCount();
  }

  private SinkAudioMixer validateAndGetSink(int sinkId) {
    Optional<SinkAudioMixer> sink = audioMixersService.getSinkAudioMixer(sinkId);
    if (sink.isEmpty()) {
      throw new NotFoundException(
        String.format(
          "No such sink audio mixer exists in the audio mixer table: id %d",
          sinkId
        )
      );
    }
    return sink.get();
  }
}
