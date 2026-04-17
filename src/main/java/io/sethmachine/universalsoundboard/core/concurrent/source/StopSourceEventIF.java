package io.sethmachine.universalsoundboard.core.concurrent.source;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface StopSourceEventIF {
  int getSourceId();
}
