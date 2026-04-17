package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.response;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import java.util.Optional;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseCancelEventIF extends OpenAiRealtimeEventCore {
  @Derived
  default String getType() {
    return "response.cancel";
  }

  Optional<String> getResponseId();
}
