package io.sethmachine.universalsoundboard.core.model.audiomixers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import org.immutables.value.Value;

public interface AudioMixerBase {
  int getAudioMixerId();

  @Value.Derived
  AudioMixerType getAudioMixerType();

  AudioMixerDescription getAudioMixerDescription();

  AudioFormat getAudioFormat();

  @JsonIgnore
  Mixer getMixer();

  String getDataLineName();
}
