package io.sethmachine.universalsoundboard.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.name.Named;

import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.wiring.AudioMixerWiringPair;

public class SinkAudioMixerRunnableService {

  private static final Logger LOG = LoggerFactory.getLogger(
    SinkAudioMixerRunnableService.class
  );

  private final AudioMixersService audioMixersService;
  private final AudioMixerWiringService audioMixerWiringService;
  private final SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory;
  private final ThreadPoolExecutor sinkThreadPoolExecutor;

  @Inject
  public SinkAudioMixerRunnableService(
    AudioMixersService audioMixersService,
    AudioMixerWiringService audioMixerWiringService,
    SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory,
    @Named("SinkThreadPoolExecutor") ThreadPoolExecutor sinkThreadPoolExecutor
  ) {
    this.audioMixersService = audioMixersService;
    this.audioMixerWiringService = audioMixerWiringService;
    this.sinkAudioMixerRunnableFactory = sinkAudioMixerRunnableFactory;
    this.sinkThreadPoolExecutor = sinkThreadPoolExecutor;
  }

  public void startSink(int sinkId) {
    SinkAudioMixer sink = validateAndGetSink(sinkId);
    List<SourceAudioMixer> sources = getSourceMixersFromWirings(audioMixerWiringService.getSinkWirings(sinkId));
    SinkAudioMixerRunnable sinkAudioMixerRunnable = sinkAudioMixerRunnableFactory.create(
      sink,
        sources
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

  private List<SourceAudioMixer> getSourceMixersFromWirings(List<AudioMixerWiringPair> wirings){
    return wirings.stream().map(wiring ->
    audioMixersService.getSourceAudioMixer(
        wiring.getSourceId()))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }
}
