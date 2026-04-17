package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.session;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.OpenAiRealtimeSession;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeSessionCreatedEventIF extends OpenAiRealtimeEventCore {
  OpenAiRealtimeSession getSession();
}
