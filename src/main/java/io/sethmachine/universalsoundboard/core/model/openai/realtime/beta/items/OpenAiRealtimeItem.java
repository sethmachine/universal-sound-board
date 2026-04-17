package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type",
  visible = true
)
@JsonSubTypes(
  {
    @JsonSubTypes.Type(value = OpenAiRealtimeMessageItem.class, name = "message"),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeFunctionCallItem.class,
      name = "function_call"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeFunctionCallOutputItem.class,
      name = "function_call_output"
    ),
  }
)
public interface OpenAiRealtimeItem {
  String getType();
}
