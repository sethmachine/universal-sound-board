package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items.OpenAiRealtimeItem;
import java.util.Optional;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeConversationItemCreatedEventIF
  extends OpenAiRealtimeEventCore {
  Optional<String> getPreviousItemId();
  OpenAiRealtimeItem getItem();

  @Derived
  default String getType() {
    return "conversation.item.create";
  }
}
