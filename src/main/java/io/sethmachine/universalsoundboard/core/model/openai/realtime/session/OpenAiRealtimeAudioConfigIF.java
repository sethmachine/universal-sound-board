package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
@JsonSerialize(as = OpenAiRealtimeAudioConfig.class)
public interface OpenAiRealtimeAudioConfigIF {
  Optional<OpenAiRealtimeInputAudioConfig> getInput();

  Optional<OpenAiRealtimeOutputAudioConfig> getOutput();
}
