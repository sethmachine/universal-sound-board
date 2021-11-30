package io.sethmachine.universalsoundboard.core.model.audiomixers.wiring;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerWiringPairIF {
  int getSinkId();
  String getSinkName();
  int getSourceId();
  String getSourceName();
}
