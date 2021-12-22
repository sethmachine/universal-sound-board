package io.sethmachine.universalsoundboard.service.api;

import com.google.inject.Inject;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerId;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerResponse;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixersList;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.CreateAudioMixerRequest;
import io.sethmachine.universalsoundboard.core.model.audiomixers.AudioMixerBase;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioMixerMetadataUtil;
import io.sethmachine.universalsoundboard.service.AudioMixersService;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioMixersApiService {

  private static final Logger LOG = LoggerFactory.getLogger(AudioMixerMetadataUtil.class);

  private final AudioMixersService audioMixersService;
  private final AudioMixerMetadataUtil audioMixerMetadataUtil;

  @Inject
  public AudioMixersApiService(
    AudioMixersService audioMixersService,
    AudioMixerMetadataUtil audioMixerMetadataUtil
  ) {
    this.audioMixersService = audioMixersService;
    this.audioMixerMetadataUtil = audioMixerMetadataUtil;
  }

  public Optional<AudioMixerResponse> getAudioMixer(int audioMixerId) {
    return audioMixersService
      .getAudioMixer(audioMixerId)
      .map(this::buildResponseFromMixer);
  }

  public AudioMixersList getAllAudioMixers() {
    return AudioMixersList
      .builder()
      .setAudioMixers(
        audioMixersService
          .getAllAudioMixers()
          .stream()
          .map(this::buildResponseFromMixer)
          .collect(Collectors.toList())
      )
      .build();
  }

  public AudioMixerId createAudioMixer(CreateAudioMixerRequest createAudioMixerRequest) {
    return audioMixersService.createAudioMixer(createAudioMixerRequest);
  }

  public void deleteAudioMixer(int audioMixerId) {
    audioMixersService.deleteAudioMixer(audioMixerId);
  }

  private AudioMixerResponse buildResponseFromMixer(AudioMixerBase mixer) {
    return AudioMixerResponse
      .builder()
      .setAudioMixerId(mixer.getAudioMixerId())
      .setAudioMixerDescription(mixer.getAudioMixerDescription())
      .setAudioFormat(mixer.getAudioFormat())
      .setAudioMixerTypeForFormat(mixer.getAudioMixerType())
      .setDataLineName(mixer.getDataLineName())
      .build();
  }
}
