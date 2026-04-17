package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.conversation;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeConversationItemDeleteEventIF
  extends OpenAiRealtimeEventCore {
  String getItemId();
}
