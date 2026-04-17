package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeSessionIF {
  Optional<String> getId();

  default String getObject() {
    return "realtime.session";
  }

  ImmutableSet<String> getModalities();

  Optional<String> getModel();

  Optional<String> getInstructions();

  Optional<String> getVoice();

  Optional<String> getInputAudioFormat();

  Optional<String> getOutputAudioFormat();

  Optional<OpenAiRealtimeTurnDetectionSettings> getTurnDetection();

  Optional<OpenAiRealtimeInputAudioTranscriptionSettings> getInputAudioTranscription();

  ImmutableList<OpenAiRealtimeTool> getTools();

  Optional<String> getToolChoice();

  Optional<Double> getTemperature();

  Optional<OpenAiRealtimeClientSecret> getClientSecret();

  /**
   * Maximum number of output tokens for a single assistant response, inclusive of tool calls.
   * Provide an integer between 1 and 4096 to limit output tokens,
   * or inf for the maximum available tokens for a given model. Defaults to inf.
   */
  @Default
  default MaxResponseOutputTokens getMaxResponseOutputTokens() {
    return MaxResponseOutputTokensUnlimited.builder().build();
  }
}
