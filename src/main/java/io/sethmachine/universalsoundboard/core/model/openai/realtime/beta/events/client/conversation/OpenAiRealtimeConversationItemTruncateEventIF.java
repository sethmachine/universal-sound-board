package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.conversation;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeConversationItemTruncateEventIF
  extends OpenAiRealtimeEventCore {
  @Derived
  default String getType() {
    return "conversation.item.truncate";
  }

  String getItemId();
  int getContentIndex();
  int getAudioEndMs();
}
