package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.conversation.OpenAiRealtimeConversation;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeConversationCreatedEventIF
  extends OpenAiRealtimeEventCore {
  OpenAiRealtimeConversation getConversation();
}
