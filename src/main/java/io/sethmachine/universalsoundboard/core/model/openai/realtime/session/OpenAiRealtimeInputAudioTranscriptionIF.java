package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
@JsonSerialize(as = OpenAiRealtimeInputAudioTranscription.class)
public interface OpenAiRealtimeInputAudioTranscriptionIF {
  Optional<String> getLanguage();

  String getModel();

  Optional<String> getPrompt();
}
