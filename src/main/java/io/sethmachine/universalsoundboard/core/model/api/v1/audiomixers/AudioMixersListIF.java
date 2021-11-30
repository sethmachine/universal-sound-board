package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers;

import java.util.List;

import org.immutables.value.Value.Immutable;

import com.hubspot.immutables.style.HubSpotStyle;

@Immutable
@HubSpotStyle
public interface AudioMixersListIF {
  List<AudioMixerResponse> getAudioMixers();
}
