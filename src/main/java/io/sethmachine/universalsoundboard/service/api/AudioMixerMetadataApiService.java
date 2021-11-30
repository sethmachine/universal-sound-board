package io.sethmachine.universalsoundboard.service.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.AudioMixerDescriptionsResponse;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.AudioMixerFormatsResponse;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.SingleAudioMixerDescriptionAndFormatsResponse;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescriptionAndFormat;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioMixerMetadataUtil;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
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
