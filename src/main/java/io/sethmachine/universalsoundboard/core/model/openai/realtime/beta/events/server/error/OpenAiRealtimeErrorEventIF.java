package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.error;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.errors.OpenAiRealtimeApiErrorDetails;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeErrorEventIF extends OpenAiRealtimeEventCore {
  @Override
  @Derived
  default String getType() {
    return "error";
  }

  OpenAiRealtimeApiErrorDetails getError();
}
