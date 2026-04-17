package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.OpenAiRealtimeSessionUpdate;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeSessionUpdateEventIF extends OpenAiRealtimeEventCore {
  @Default
  default String getType() {
    return "session.update";
  }

  @JsonProperty("session")
  OpenAiRealtimeSessionUpdate getSessionUpdate();
}
