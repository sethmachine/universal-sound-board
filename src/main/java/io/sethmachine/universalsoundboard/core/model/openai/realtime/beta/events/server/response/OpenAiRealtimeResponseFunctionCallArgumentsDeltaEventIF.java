package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEvent;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseFunctionCallArgumentsDeltaEventIF
  extends OpenAiRealtimeEvent {
  String getResponseId();
  String getItemId();
  int getOutputIndex();
  String getCallId();
  String getDelta();
}
