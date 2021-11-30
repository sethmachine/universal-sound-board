package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.wiring;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface WireSinkToSourceRequestIF {
  int getSinkId();
  int getSourceId();
}
