package io.sethmachine.universalsoundboard.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerDescriptions;
import io.sethmachine.universalsoundboard.core.model.audiomixers.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.AudioMixerType;

public class AudioMixerMetadataService {

  @Inject
  public AudioMixerMetadataService() {

  }

  public AudioMixerDescriptions getAudioMixerDescriptions(Optional<AudioMixerType> audioMixerType) {
    return AudioMixerDescriptions.builder()
        .setAudioMixerDescriptions(getAllAudioMixerDescriptions(audioMixerType))
        .build();
  }

  private List<AudioMixerDescription> getAllAudioMixerDescriptions(Optional<AudioMixerType> audioMixerType) {
    return Stream.of(AudioSystem.getMixerInfo())
        .filter(info -> audioMixerSupportsAudioMixerType(info, audioMixerType))
        .map(info ->
        AudioMixerDescription.builder()
            .setName(info.getName())
            .setVendor(info.getVendor())
            .setDescription(info.getDescription())
            .setVersion(info.getVersion())
            .build())
        .sorted()
        .collect(Collectors.toList());
  }

  private boolean audioMixerSupportsAudioMixerType(Info audioMixerInfo, Optional<AudioMixerType> audioMixerType){
    if (audioMixerType.isEmpty()){
      return true;
    }
    Mixer audioMixer = AudioSystem.getMixer(audioMixerInfo);
    if (audioMixerType.get() == AudioMixerType.SINK){
      return Stream.of(audioMixer.getTargetLineInfo()).findAny().isPresent();
    }
    return Stream.of(audioMixer.getSourceLineInfo()).findAny().isPresent();
  }

//  public void getAudioMixerFormats(Mixer mixer, AudioMixerType audioMixerType){
//    if (audioMixerType == AudioMixerType.SINK){
//      return mixer.getTargetLineInfo()
//    }
//  }

  private Optional<Mixer> getAudioMixerByName(String name){
    return Stream.of(AudioSystem.getMixerInfo())
        .filter(info -> info.getName().equals(name))
        .map(info -> AudioSystem.getMixer(info)).findFirst();
  }

}
