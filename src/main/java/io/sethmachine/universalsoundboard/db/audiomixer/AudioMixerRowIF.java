package io.sethmachine.universalsoundboard.db.audiomixer;

import com.hubspot.immutables.style.HubSpotStyle;
import com.hubspot.rosetta.annotations.RosettaProperty;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerRowIF extends AudioMixerDTOCore {
  int getId();
}