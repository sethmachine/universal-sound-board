package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.session;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.OpenAiRealtimeSession;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeSessionUpdatedEventIF extends OpenAiRealtimeEventCore {
  @JsonAlias("session")
  OpenAiRealtimeSession getSession();
}
