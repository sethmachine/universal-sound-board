package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixersListIF {
  List<AudioMixerResponse> getAudioMixers();
}
