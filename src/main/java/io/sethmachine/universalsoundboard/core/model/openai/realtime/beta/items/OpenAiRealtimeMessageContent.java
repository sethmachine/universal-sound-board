package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.items.OpenAiRealtimeMessageContentOutputAudio;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type",
  visible = true
)
@JsonSubTypes(
  {
    @JsonSubTypes.Type(value = OpenAiRealtimeMessageContentText.class, name = "text"),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeMessageContentInputText.class,
      name = "input_text"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeMessageContentItemReference.class,
      name = "item_reference"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeMessageContentInputAudio.class,
      name = "input_audio"
    ),
    @JsonSubTypes.Type(value = OpenAiRealtimeMessageContentAudio.class, name = "audio"),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeMessageContentOutputAudio.class,
      name = "output_audio"
    ),
  }
)
public interface OpenAiRealtimeMessageContent {
  String getType();
}
