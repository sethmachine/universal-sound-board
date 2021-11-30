package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerIdIF {
  @JsonProperty("audioMixerId")
  int getId();
}
