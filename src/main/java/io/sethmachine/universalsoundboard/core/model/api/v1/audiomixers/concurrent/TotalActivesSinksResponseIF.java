package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent;

import org.immutables.value.Value.Immutable;

import com.hubspot.immutables.style.HubSpotStyle;

@Immutable
@HubSpotStyle
public interface TotalActivesSinksResponseIF {
  int getCount();
}
