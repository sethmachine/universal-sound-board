package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.inputaudiobuffer;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeInputAudioBufferAppendEventIF
  extends OpenAiRealtimeEventCore {
  // Base64-encoded audio bytes. This must be in the format specified
  // by the input_audio_format field in the session configuration.
  String getAudio();
}
