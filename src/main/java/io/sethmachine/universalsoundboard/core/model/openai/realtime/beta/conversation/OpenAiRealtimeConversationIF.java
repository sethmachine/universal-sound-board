package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.conversation;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeConversationIF {
  String getId();

  // The object type, must be realtime.conversation
  default String getObject() {
    return "realtime.conversation";
  }
}
