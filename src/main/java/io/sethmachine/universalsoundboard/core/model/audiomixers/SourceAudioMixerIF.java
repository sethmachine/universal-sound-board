package io.sethmachine.universalsoundboard.core.model.audiomixers;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface SourceAudioMixerIF extends AudioMixerBase {
  @Override
  default AudioMixerType getAudioMixerType() {
    return AudioMixerType.SOURCE;
  }
}
