package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Derived;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseAudioTranscriptDoneEventIF
  extends OpenAiRealtimeResponseDeltaEventCore {
  String getTranscript();

  @Derived
  default String getType() {
    return "response.audio_transcript.done";
  }
}
