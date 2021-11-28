package io.sethmachine.universalsoundboard.core.util.audiomixer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescriptionAndFormat;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;

public class AudioMixerMetadataUtil {

  private static final Logger LOG = LoggerFactory.getLogger(AudioMixerMetadataUtil.class);

  public List<AudioMixerDescription> getAllMatchingAudioMixerDescriptions(
    AudioMixerMetadataQuery query
  ) {
    return Stream
      .of(AudioSystem.getMixerInfo())
      .filter(info -> audioMixerInfoMatchesQuery(info, query))
      .map(this::buildAudioMixerDescription)
      .sorted()
      .collect(Collectors.toList());
  }

  public Optional<Mixer> findFirstMixerMatchingQuery(AudioMixerMetadataQuery query) {
    List<String> matchedMixerNames = Collections.synchronizedList(new ArrayList<>());
    return Stream
      .of(AudioSystem.getMixerInfo())
      .filter(info -> audioMixerInfoMatchesQuery(info, query))
      .peek(info -> matchedMixerNames.add(info.getName()))
      .map(info -> {
        if (matchedMixerNames.size() > 1) {
          LOG.warn(
            "Matched {} audio mixers to query but only expected 1: query {}, matched names: {}",
            matchedMixerNames.size(),
            query,
            matchedMixerNames
          );
        }
        return AudioSystem.getMixer(info);
      })
      .findFirst();
  }

  public List<AudioMixerDescriptionAndFormat> getSingleAudioMixerDescriptionAndFormats(
    AudioMixerMetadataQuery query
  ) {
    return findFirstMixerMatchingQuery(query)
      .map(mixer -> {
        ImmutableList.Builder<AudioMixerDescriptionAndFormat> builder = ImmutableList.builder();
        if (query.getAudioMixerType().isPresent()) {
          if (query.getAudioMixerType().get() == AudioMixerType.SINK) {
            builder.addAll(getMixerTargetDescriptionAndFormats(mixer));
          } else if (query.getAudioMixerType().get() == AudioMixerType.SOURCE) {
            builder.addAll(getMixerSourceDescriptionAndFormats(mixer));
          }
        } else {
          builder.addAll(getMixerTargetDescriptionAndFormats(mixer));
          builder.addAll(getMixerSourceDescriptionAndFormats(mixer));
        }
        return builder.build();
      })
      .orElseGet(ImmutableList::of);
  }

  private List<AudioMixerDescriptionAndFormat> getMixerTargetDescriptionAndFormats(
    Mixer mixer
  ) {
    return buildAudioMixerDescriptionAndFormats(
      mixer,
      mixer.getTargetLineInfo(),
      AudioMixerType.SINK
    );
  }

  private List<AudioMixerDescriptionAndFormat> getMixerSourceDescriptionAndFormats(
    Mixer mixer
  ) {
    return buildAudioMixerDescriptionAndFormats(
      mixer,
      mixer.getSourceLineInfo(),
      AudioMixerType.SOURCE
    );
  }

  private List<AudioMixerDescriptionAndFormat> buildAudioMixerDescriptionAndFormats(
    Mixer mixer,
    Line.Info[] lines,
    AudioMixerType audioMixerType
  ) {
    return Stream
      .of(lines)
      .map(targetInfo -> {
        final DataLine.Info dataLineInfo = (DataLine.Info) targetInfo;
        return Stream
          .of(dataLineInfo.getFormats())
          .map(audioFormat ->
            AudioMixerDescriptionAndFormat
              .builder()
              .setAudioMixerDescription(buildAudioMixerDescription(mixer.getMixerInfo()))
              .setAudioFormat(audioFormat)
              .setAudioMixerTypeForFormat(audioMixerType)
              .setDataLineName(dataLineInfo.toString())
              .build()
          );
      })
      .collect(Collectors.toList())
      .stream()
      .flatMap(Function.identity())
      .collect(Collectors.toList());
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
        !findSupportedAudioMixerTypes(audioMixerInfo)
          .contains(query.getAudioMixerType().get())
      ) {
        return false;
      }
    }
    return true;
  }

  private ImmutableSet<AudioMixerType> findSupportedAudioMixerTypes(Info audioMixerInfo) {
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

  private AudioMixerDescription buildAudioMixerDescription(Info audioMixerInfo) {
    return AudioMixerDescription
      .builder()
      .setName(audioMixerInfo.getName())
      .setVendor(audioMixerInfo.getVendor())
      .setDescription(audioMixerInfo.getDescription())
      .setVersion(audioMixerInfo.getVersion())
      .setSupportedAudioMixerTypes(findSupportedAudioMixerTypes(audioMixerInfo))
      .build();
  }

  private List<AudioFormat> filterAudioFormats(AudioFormat[] audioFormats) {
    return Stream
      .of(audioFormats)
      .filter(audioFormat -> audioFormat.getFrameRate() > 0)
      .collect(ImmutableList.toImmutableList());
  }
}
