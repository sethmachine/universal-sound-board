package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeFunctionCallOutputItemIF extends OpenAiRealtimeItemCore {
  @Derived
  default String getType() {
    return "function_call_output";
  }

  String getCallId();

  String getOutput();
}
