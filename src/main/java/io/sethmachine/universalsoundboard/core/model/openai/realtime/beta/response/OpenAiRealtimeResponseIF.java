package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.response;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items.OpenAiRealtimeItem;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.MaxResponseOutputTokens;
import java.util.Optional;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeResponseIF {
  Optional<String> getId();

  default String getObject() {
    return "realtime.response";
  }

  Optional<String> getStatus();

  Optional<OpenAiRealtimeStatusDetails> getStatusDetails();

  @Default
  default ImmutableList<OpenAiRealtimeItem> getOutput() {
    return ImmutableList.of();
  }

  Optional<ImmutableMap<String, String>> getMetadata();

  Optional<OpenAiRealtimeUsage> getUsage();

  /**
   * Which conversation the response is added to, determined by the conversation field in the response.create event.
   * If auto, the response will be added to the default conversation and the value of conversation_id will be an id like conv_1234.
   * If none, the response will not be added to any conversation and the value of conversation_id will be null.
   * If responses are being triggered by server VAD, the response will be added to the default conversation,
   * thus the conversation_id will be an id like conv_1234.
   */
  Optional<String> getConversationId();

  Optional<String> getVoice();

  Optional<ImmutableSet<String>> getModalities();

  Optional<String> getOutputAudioFormat();

  Optional<MaxResponseOutputTokens> getMaxResponseOutputTokens();

  Optional<Integer> getTemperature();
}
