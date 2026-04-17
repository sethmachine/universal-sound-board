package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEvent;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseFunctionCallArgumentsDoneEventIF
  extends OpenAiRealtimeEvent {
  @Override
  @Derived
  default String getType() {
    return "response.function_call_arguments.done";
  }

  String getResponseId();
  String getItemId();
  int getOutputIndex();
  String getName();
  String getCallId();
  String getArguments();
}
