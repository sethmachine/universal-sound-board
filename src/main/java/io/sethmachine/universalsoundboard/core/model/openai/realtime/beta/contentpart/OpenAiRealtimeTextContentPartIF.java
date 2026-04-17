package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.contentpart;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeTextContentPartIF extends OpenAiRealtimeContentPart {
  @Default
  default String getType() {
    return "text";
  }

  Optional<String> getText();
}
