package io.sethmachine.universalsoundboard.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.AudioMixerDescriptionsResponse;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.AudioMixerFormatsResponse;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
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

public class AudioMixerMetadataService {

  private static final Logger LOG = LoggerFactory.getLogger(
    AudioMixerMetadataService.class
  );

  @Inject
  public AudioMixerMetadataService() {}

  public AudioMixerDescriptionsResponse getAudioMixerDescriptions(
    AudioMixerMetadataQuery query
  ) {
    return AudioMixerDescriptionsResponse
      .builder()
      .setAudioMixerDescriptions(getAllMatchingAudioMixerDescriptions(query))
      .build();
  }

  public Optional<AudioMixerFormatsResponse> getSingleAudioMixerSupportedFormats(
    AudioMixerMetadataQuery query
  ) {
    return findSingleAudioMixerSupportedFormats(query);
  }

  private Optional<AudioMixerFormatsResponse> findSingleAudioMixerSupportedFormats(
    AudioMixerMetadataQuery query
  ) {
    AtomicInteger count = new AtomicInteger();
    return Stream
      .of(AudioSystem.getMixerInfo())
      .filter(info -> audioMixerInfoMatchesQuery(info, query))
      .peek(info -> count.incrementAndGet())
      .findFirst()
      .map(info -> {
        if (count.get() > 1) {
          LOG.warn(
            "Matched {} audio mixers to query but only expected 1: query {}",
            count.get(),
            query
          );
        }
        Map<AudioMixerType, ImmutableMap<String, List<AudioFormat>>> formats = buildAudioMixerFormats(
          info
        );
        return AudioMixerFormatsResponse
          .builder()
          .setAudioMixerDescription(buildAudioMixerDescription(info))
          .setSinkAudioFormats(
            Objects.requireNonNull(
              formats.getOrDefault(AudioMixerType.SINK, ImmutableMap.of())
            )
          )
          .setSourceAudioFormats(
            Objects.requireNonNull(
              formats.getOrDefault(AudioMixerType.SOURCE, ImmutableMap.of())
            )
          )
          .build();
      });
  }

  private List<AudioMixerDescription> getAllMatchingAudioMixerDescriptions(
    AudioMixerMetadataQuery query
  ) {
    return Stream
      .of(AudioSystem.getMixerInfo())
      .filter(info -> audioMixerInfoMatchesQuery(info, query))
      .map(this::buildAudioMixerDescription)
      .sorted()
      .collect(Collectors.toList());
  }

  private AudioMixerDescription buildAudioMixerDescription(Info audioMixerInfo) {
    return AudioMixerDescription
      .builder()
      .setName(audioMixerInfo.getName())
      .setVendor(audioMixerInfo.getVendor())
      .setDescription(audioMixerInfo.getDescription())
      .setVersion(audioMixerInfo.getVersion())
      .build();
  }

  private boolean audioMixerInfoMatchesQuery(
    Info audioMixerInfo,
    AudioMixerMetadataQuery query
  ) {
    if (query.getAudioMixerName().isPresent()) {
      if (!audioMixerInfo.getName().equals(query.getAudioMixerName().get())) {
        return false;
      }
    }
    if (query.getAudioMixerType().isPresent()) {
      if (
        !determineSupportedAudioMixerTypes(audioMixerInfo)
          .contains(query.getAudioMixerType().get())
      ) {
        return false;
      }
    }
    return true;
  }

  private ImmutableSet<AudioMixerType> determineSupportedAudioMixerTypes(
    Info audioMixerInfo
  ) {
    Mixer audioMixer = AudioSystem.getMixer(audioMixerInfo);
    ImmutableSet.Builder<AudioMixerType> builder = ImmutableSet.builder();
    if (Stream.of(audioMixer.getTargetLineInfo()).findAny().isPresent()) {
      builder.add(AudioMixerType.SINK);
    }
    if (Stream.of(audioMixer.getSourceLineInfo()).findAny().isPresent()) {
      builder.add(AudioMixerType.SOURCE);
    }
    return builder.build();
  }

  private ImmutableMap<AudioMixerType, ImmutableMap<String, List<AudioFormat>>> buildAudioMixerFormats(
    Info audioMixerInfo
  ) {
    ImmutableMap.Builder<AudioMixerType, ImmutableMap<String, List<AudioFormat>>> builder = ImmutableMap.builder();
    Mixer audioMixer = AudioSystem.getMixer(audioMixerInfo);
    builder.put(
      AudioMixerType.SINK,
      Stream
        .of(audioMixer.getTargetLineInfo())
        .map(targetInfo -> {
          final DataLine.Info dataLineInfo = (DataLine.Info) targetInfo;
          return Map.entry(
            dataLineInfo.toString(),
            filterAudioFormats(dataLineInfo.getFormats())
          );
        })
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue))
    );
    builder.put(
      AudioMixerType.SOURCE,
      Stream
        .of(audioMixer.getSourceLineInfo())
        .map(targetInfo -> {
          final DataLine.Info dataLineInfo = (DataLine.Info) targetInfo;
          return Map.entry(
            dataLineInfo.toString(),
            filterAudioFormats(dataLineInfo.getFormats())
          );
        })
        .collect(ImmutableMap.toImmutableMap(Entry::getKey, Entry::getValue))
    );
    return builder.build();
  }

  private List<AudioFormat> filterAudioFormats(AudioFormat[] audioFormats) {
    return Stream
      .of(audioFormats)
      .filter(audioFormat -> audioFormat.getFrameRate() > 0)
      .collect(ImmutableList.toImmutableList());
  }
}
