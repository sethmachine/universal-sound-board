package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.conversation;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items.OpenAiRealtimeItem;
import java.util.Optional;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeConversationItemCreateEventIF
  extends OpenAiRealtimeEventCore {
  @Value.Derived
  default String getType() {
    return "conversation.item.create";
  }

  Optional<String> getPreviousItemId();
  OpenAiRealtimeItem getItem();
}
