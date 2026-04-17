package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseAudioDeltaEventIF
  extends OpenAiRealtimeResponseDeltaEventCore {
  @Default
  default String getType() {
    return "response.audio.delta";
  }

  String getDelta();
}
