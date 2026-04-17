package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeFunctionCallItemIF extends OpenAiRealtimeItemCore {
  @JsonAlias("call_id")
  String getCallId();

  @JsonAlias("name")
  String getName();

  @JsonAlias("arguments")
  String getArguments();
}
