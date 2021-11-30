package io.sethmachine.universalsoundboard.core.model.audiomixers.metadata;

import com.hubspot.immutables.style.HubSpotStyle;
import javax.sound.sampled.AudioFormat;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerDescriptionAndFormatIF {
  AudioMixerDescription getAudioMixerDescription();
  AudioFormat getAudioFormat();
  AudioMixerType getAudioMixerTypeForFormat();
  String getDataLineName();
}
