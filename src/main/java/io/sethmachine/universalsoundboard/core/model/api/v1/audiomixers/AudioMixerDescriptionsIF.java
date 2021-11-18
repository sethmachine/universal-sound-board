package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers;


import java.util.List;

import org.immutables.value.Value.Immutable;

import com.hubspot.immutables.style.HubSpotStyle;

import io.sethmachine.universalsoundboard.core.model.audiomixers.AudioMixerDescription;

@Immutable
@HubSpotStyle
public interface AudioMixerDescriptionsIF {
  List<AudioMixerDescription> getAudioMixerDescriptions();
}
