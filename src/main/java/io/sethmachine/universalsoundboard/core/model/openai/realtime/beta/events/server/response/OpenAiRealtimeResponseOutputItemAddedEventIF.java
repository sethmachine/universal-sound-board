package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items.OpenAiRealtimeItem;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseOutputItemAddedEventIF
  extends OpenAiRealtimeEventCore {
  String getResponseId();
  int getOutputIndex();
  OpenAiRealtimeItem getItem();
}
