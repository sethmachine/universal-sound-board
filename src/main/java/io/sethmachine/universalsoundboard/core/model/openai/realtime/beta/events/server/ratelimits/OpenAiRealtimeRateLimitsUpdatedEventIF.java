package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.ratelimits;

import com.google.common.collect.ImmutableList;
import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.ratelimits.OpenAiRealtimeRateLimits;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeRateLimitsUpdatedEventIF extends OpenAiRealtimeEvent {
  ImmutableList<OpenAiRealtimeRateLimits> getRateLimits();
}
