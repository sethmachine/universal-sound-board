package io.sethmachine.universalsoundboard.service;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.sink.SinkAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.sink.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.concurrent.sink.StopSinkEvent;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import javax.inject.Inject;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinkAudioMixerRunnableService {

  private static final Logger LOG = LoggerFactory.getLogger(
    SinkAudioMixerRunnableService.class
  );

  private final AudioMixersService audioMixersService;
  private final AudioMixerWiringService audioMixerWiringService;
  private final SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory;
  private final ThreadPoolExecutor sinkThreadPoolExecutor;
  private final EventBus eventBus;

  @Inject
  public SinkAudioMixerRunnableService(
    AudioMixersService audioMixersService,
    AudioMixerWiringService audioMixerWiringService,
    SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory,
    @Named("SinkThreadPoolExecutor") ThreadPoolExecutor sinkThreadPoolExecutor,
    @Named("SinkEventBus") EventBus eventBus
  ) {
    this.audioMixersService = audioMixersService;
    this.audioMixerWiringService = audioMixerWiringService;
    this.sinkAudioMixerRunnableFactory = sinkAudioMixerRunnableFactory;
    this.sinkThreadPoolExecutor = sinkThreadPoolExecutor;
    this.eventBus = eventBus;
  }

  public void startSink(int sinkId) {
    SinkAudioMixer sink = audioMixersService
      .getSinkAudioMixer(sinkId)
      .orElseThrow(() ->
        new NotFoundException(
          String.format(
            "No such sink audio mixer exists in the audio mixer table: id %d",
            sinkId
          )
        )
      );
    List<SourceAudioMixer> sources = audioMixerWiringService.getSourceMixersFromWirings(
      audioMixerWiringService.getSinkWirings(sinkId)
    );
    if (sources.isEmpty()) {
      throw new NotAllowedException(
        String.format(
          "Unable to start the sink with id %d as it does not have any sources wired to it.",
          sinkId
        )
      );
    }
    SinkAudioMixerRunnable sinkAudioMixerRunnable = sinkAudioMixerRunnableFactory.create(
      sink,
      sources
    );
    sinkThreadPoolExecutor.execute(sinkAudioMixerRunnable);
  }

  public void stopSink(int sinkId) {
    audioMixersService
      .getSinkAudioMixer(sinkId)
      .orElseThrow(() ->
        new NotFoundException(
          String.format(
            "No such sink audio mixer exists in the audio mixer table: id %d",
            sinkId
          )
        )
      );
    eventBus.post(StopSinkEvent.builder().setSinkId(sinkId).build());
  }

  public int getTotalActiveSinks() {
    return this.sinkThreadPoolExecutor.getActiveCount();
  }
}
