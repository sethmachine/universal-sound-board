package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.contentpart;

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
    @JsonSubTypes.Type(value = OpenAiRealtimeAudioContentPart.class, name = "audio"),
    @JsonSubTypes.Type(value = OpenAiRealtimeTextContentPart.class, name = "text"),
  }
)
public interface OpenAiRealtimeContentPart {
  String getType();
}
