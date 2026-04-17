package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.ratelimits;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeRateLimitsIF {
  String getName();
  int getLimit();
  int getRemaining();
  int getResetSeconds();
}
