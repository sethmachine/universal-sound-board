package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeMessageContentInputAudioIF
  extends OpenAiRealtimeMessageContent {
  @Default
  default String getType() {
    return "input_audio";
  }

  // Base64-encoded audio bytes, used for input_audio content type.
  Optional<String> getAudio();

  // The transcript of the audio, used for input_audio content type
  Optional<String> getTranscript();
}
