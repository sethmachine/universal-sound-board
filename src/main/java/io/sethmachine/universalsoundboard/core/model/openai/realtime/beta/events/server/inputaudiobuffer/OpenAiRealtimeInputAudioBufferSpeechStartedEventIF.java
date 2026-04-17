package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.inputaudiobuffer;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
//See: https://platform.openai.com/docs/api-reference/realtime-server-events/input_audio_buffer
public interface OpenAiRealtimeInputAudioBufferSpeechStartedEventIF
  extends OpenAiRealtimeEventCore {
  @Default
  default String getType() {
    return "input_audio_buffer.speech_started";
  }

  // Milliseconds from the start of all audio written to the buffer during the session when speech was first detected
  // This will correspond to the beginning of audio sent to the model, and thus includes the prefix_padding_ms configured in the Session.
  int getAudioStartMs();

  String getItemId();
}
