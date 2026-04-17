package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.conversation;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeTranscriptionErrorIF {
  Optional<String> getType();
  Optional<String> getCode();
  Optional<String> getMessage();
  Optional<String> getParam();
}
