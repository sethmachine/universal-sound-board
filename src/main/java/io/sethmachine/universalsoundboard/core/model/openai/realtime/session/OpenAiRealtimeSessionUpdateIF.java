package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.MaxResponseOutputTokens;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.MaxResponseOutputTokensUnlimited;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.OpenAiRealtimeTool;
import java.util.Optional;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
@JsonSerialize(as = OpenAiRealtimeSessionUpdate.class)
public interface OpenAiRealtimeSessionUpdateIF {
  Optional<OpenAiRealtimeAudioConfig> getAudio();

  Optional<String> getInstructions();

  Optional<String> getModel();

  @JsonProperty("output_modalities")
  Optional<ImmutableSet<String>> getOutputModalities();

  Optional<ImmutableList<OpenAiRealtimeTool>> getTools();

  Optional<String> getToolChoice();

  Optional<ImmutableList<String>> getInclude();

  Optional<OpenAiRealtimePrompt> getPrompt();

  Optional<OpenAiRealtimeTracing> getTracing();

  Optional<OpenAiRealtimeTruncation> getTruncation();

  @JsonProperty("max_output_tokens")
  @Default
  default MaxResponseOutputTokens getMaxOutputTokens() {
    return MaxResponseOutputTokensUnlimited.builder().build();
  }
}
