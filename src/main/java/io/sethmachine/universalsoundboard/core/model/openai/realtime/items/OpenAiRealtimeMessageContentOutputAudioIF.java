package io.sethmachine.universalsoundboard.core.model.openai.realtime.items;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items.OpenAiRealtimeMessageContent;
import java.util.Optional;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeMessageContentOutputAudioIF
  extends OpenAiRealtimeMessageContent {
  @Default
  default String getType() {
    return "output_audio";
  }

  Optional<String> getTranscript();
}
