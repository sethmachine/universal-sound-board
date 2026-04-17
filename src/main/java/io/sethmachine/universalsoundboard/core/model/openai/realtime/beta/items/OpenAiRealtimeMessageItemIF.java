package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import com.google.common.collect.ImmutableList;
import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeMessageItemIF extends OpenAiRealtimeItemCore {
  @Derived
  default String getType() {
    return "message";
  }

  String getRole();

  ImmutableList<OpenAiRealtimeMessageContent> getContent();
}
