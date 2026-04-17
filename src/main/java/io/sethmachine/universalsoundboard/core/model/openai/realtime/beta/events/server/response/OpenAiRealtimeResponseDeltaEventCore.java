package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response;

import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEvent;

public interface OpenAiRealtimeResponseDeltaEventCore extends OpenAiRealtimeEvent {
  String getResponseId();
  String getItemId();
  int getOutputIndex();
  int getContentIndex();
}
