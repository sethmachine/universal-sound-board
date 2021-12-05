package io.sethmachine.universalsoundboard.service.api;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.wiring.AudioMixerWiringList;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.wiring.WireSinkToSourceRequest;
import io.sethmachine.universalsoundboard.core.model.audiomixers.wiring.AudioMixerWiringPair;
import io.sethmachine.universalsoundboard.service.AudioMixerWiringService;
import java.util.Optional;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioMixerWiringApiService {

  private static final Logger LOG = LoggerFactory.getLogger(
    AudioMixerWiringApiService.class
  );

  private final AudioMixerWiringService audioMixerWiringService;

  @Inject
  public AudioMixerWiringApiService(AudioMixerWiringService audioMixerWiringService) {
    this.audioMixerWiringService = audioMixerWiringService;
  }

  public void wireSinkToSource(WireSinkToSourceRequest wireSinkToSourceRequest) {
    audioMixerWiringService.wireSinkToSource(
      wireSinkToSourceRequest.getSinkId(),
      wireSinkToSourceRequest.getSourceId()
    );
  }

  public Optional<AudioMixerWiringPair> getWiring(int sinkId, int sourceId) {
    return audioMixerWiringService.getWiring(sinkId, sourceId);
  }

  public AudioMixerWiringList getSinkWirings(int sinkId) {
    return AudioMixerWiringList
      .builder()
      .setWirings(audioMixerWiringService.getSinkWirings(sinkId))
      .build();
  }

  public AudioMixerWiringList getSourceWirings(int sourceId) {
    return AudioMixerWiringList
      .builder()
      .setWirings(audioMixerWiringService.getSourceWirings(sourceId))
      .build();
  }
}
