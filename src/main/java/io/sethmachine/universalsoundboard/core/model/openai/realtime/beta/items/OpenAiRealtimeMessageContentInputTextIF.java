package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeMessageContentInputTextIF
  extends OpenAiRealtimeMessageContent {
  @Derived
  default String getType() {
    return "input_text";
  }

  String getText();
}
