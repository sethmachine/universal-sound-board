package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.inputaudiobuffer;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEventCore;
import java.util.Optional;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
//See: https://platform.openai.com/docs/api-reference/realtime-server-events/input_audio_buffer
public interface OpenAiRealtimeInputAudioBufferCommittedEventIF
  extends OpenAiRealtimeEventCore {
  @Default
  default String getType() {
    return "input_audio_buffer.committed";
  }

  Optional<String> getPreviousItemId();

  Optional<String> getItemId();
}
