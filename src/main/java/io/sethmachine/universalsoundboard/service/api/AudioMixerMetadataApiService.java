package io.sethmachine.universalsoundboard.service.api;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.AudioMixerDescriptionsResponse;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.SingleAudioMixerDescriptionAndFormatsResponse;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioMixerMetadataUtil;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioMixerMetadataApiService {

  private static final Logger LOG = LoggerFactory.getLogger(
    AudioMixerMetadataApiService.class
  );

  private final AudioMixerMetadataUtil audioMixerMetadataUtil;

  @Inject
  public AudioMixerMetadataApiService(AudioMixerMetadataUtil audioMixerMetadataUtil) {
    this.audioMixerMetadataUtil = audioMixerMetadataUtil;
  }

  public AudioMixerDescriptionsResponse getAudioMixerDescriptions(
    AudioMixerMetadataQuery query
  ) {
    return AudioMixerDescriptionsResponse
      .builder()
      .setAudioMixerDescriptions(
        audioMixerMetadataUtil.getAllMatchingAudioMixerDescriptions(query)
      )
      .build();
  }

  public SingleAudioMixerDescriptionAndFormatsResponse getSingleAudioMixerSupportedFormats(
    AudioMixerMetadataQuery query
  ) {
    return SingleAudioMixerDescriptionAndFormatsResponse
      .builder()
      .setAudioMixerDescriptionAndFormats(
        audioMixerMetadataUtil.getSingleAudioMixerDescriptionAndFormats(query)
      )
      .build();
  }
}
