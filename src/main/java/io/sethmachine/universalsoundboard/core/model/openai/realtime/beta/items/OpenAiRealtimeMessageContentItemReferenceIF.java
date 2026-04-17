package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeMessageContentItemReferenceIF
  extends OpenAiRealtimeMessageContent {
  @Default
  default String getType() {
    return "item_reference";
  }

  String getId();
}
