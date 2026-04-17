package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.contentpart.OpenAiRealtimeContentPart;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseContentPartDoneEventIF
  extends OpenAiRealtimeEventCore {
  String getResponseId();
  String getItemId();
  int getOutputIndex();
  int getContentIndex();
  OpenAiRealtimeContentPart getPart();
}
