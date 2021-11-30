package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers;

import javax.sound.sampled.AudioFormat;

import org.immutables.value.Value.Immutable;

import com.hubspot.immutables.style.HubSpotStyle;

import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;

@Immutable
@HubSpotStyle
public interface AudioMixerResponseIF {
  AudioMixerDescription getAudioMixerDescription();
  AudioFormat getAudioFormat();
  AudioMixerType getAudioMixerTypeForFormat();
  String getDataLineName();
}
