package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.inputaudiobuffer;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
//See: https://platform.openai.com/docs/api-reference/realtime-server-events/input_audio_buffer
public interface OpenAiRealtimeInputAudioBufferSpeechStoppedEventIF
  extends OpenAiRealtimeEventCore {
  @Default
  default String getType() {
    return "input_audio_buffer.speech_stopped";
  }

  // Milliseconds since the session started when speech stopped. This will correspond to the end of audio sent to the model,
  // and thus includes the min_silence_duration_ms configured in the Session.
  int getAudioEndMs();

  String getItemId();
}
