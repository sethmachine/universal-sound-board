package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
@JsonSerialize(as = OpenAiRealtimePrompt.class)
public interface OpenAiRealtimePromptIF {
  Optional<String> getId();

  Optional<String> getVersion();

  Optional<ImmutableMap<String, Object>> getVariables();
}
