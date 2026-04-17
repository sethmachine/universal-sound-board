package io.sethmachine.universalsoundboard.core.model.openai.realtime.events.server.response;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseDeltaEventCore;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseOutputAudioDeltaEventIF
  extends OpenAiRealtimeResponseDeltaEventCore {
  String getDelta();

  @Derived
  default String getType() {
    return "response.output_audio.delta";
  }
}
