package io.sethmachine.universalsoundboard.core.model.audiomixers;

import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer;
import org.immutables.value.Value;

public interface AudioMixerBase {
  @Value.Derived
  AudioMixerType getAudioMixerType();

  AudioMixerDescription getAudioMixerDescription();

  AudioFormat getAudioFormat();

  Mixer getMixer();
}
