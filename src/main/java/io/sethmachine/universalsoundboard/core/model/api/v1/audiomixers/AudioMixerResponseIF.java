package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import javax.sound.sampled.AudioFormat;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerResponseIF {
  int getAudioMixerId();
  AudioMixerDescription getAudioMixerDescription();
  AudioFormat getAudioFormat();
  AudioMixerType getAudioMixerTypeForFormat();
  String getDataLineName();
}
