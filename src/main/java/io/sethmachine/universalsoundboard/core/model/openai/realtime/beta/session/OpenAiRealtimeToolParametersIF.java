package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonTypeName("object")
public interface OpenAiRealtimeToolParametersIF {
  ImmutableMap<String, Object> getProperties();

  Optional<ImmutableList<String>> getRequired();

  Optional<String> getAdditionalProperties();
}
