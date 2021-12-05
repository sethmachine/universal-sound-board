package io.sethmachine.universalsoundboard.core.concurrent.sink;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface StopSinkEventIF {
  int getSinkId();
}
