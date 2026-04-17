package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.conversation.OpenAiRealtimeConversationItemCreateEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.conversation.OpenAiRealtimeConversationItemDeleteEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.conversation.OpenAiRealtimeConversationItemTruncateEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.inputaudiobuffer.OpenAiRealtimeInputAudioBufferAppendEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.inputaudiobuffer.OpenAiRealtimeInputAudioBufferClearEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.inputaudiobuffer.OpenAiRealtimeInputAudioBufferCommitEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.response.OpenAiRealtimeResponseCancelEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.response.OpenAiRealtimeResponseCreateEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.session.OpenAiRealtimeSessionUpdateEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation.OpenAiRealtimeConversationCreatedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation.OpenAiRealtimeConversationItemCreatedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation.OpenAiRealtimeConversationItemDeletedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation.OpenAiRealtimeConversationItemInputAudioTranscriptionCompletedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation.OpenAiRealtimeConversationItemInputAudioTranscriptionDeltaEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation.OpenAiRealtimeConversationItemInputAudioTranscriptionFailedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.conversation.OpenAiRealtimeConversationItemTruncatedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.error.OpenAiRealtimeErrorEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.inputaudiobuffer.OpenAiRealtimeInputAudioBufferClearedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.inputaudiobuffer.OpenAiRealtimeInputAudioBufferCommittedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.inputaudiobuffer.OpenAiRealtimeInputAudioBufferSpeechStartedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.inputaudiobuffer.OpenAiRealtimeInputAudioBufferSpeechStoppedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.ratelimits.OpenAiRealtimeRateLimitsUpdatedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseAudioDeltaEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseAudioDoneEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseAudioTranscriptDeltaEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseAudioTranscriptDoneEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseContentPartAddedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseContentPartDoneEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseCreatedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseDoneEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseFunctionCallArgumentsDeltaEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseFunctionCallArgumentsDoneEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseOutputItemAddedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseOutputItemDoneEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseTextDeltaEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseTextDoneEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.session.OpenAiRealtimeSessionCreatedEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.session.OpenAiRealtimeSessionUpdatedEvent;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type",
  visible = true
)
@JsonSubTypes(
  {
    @JsonSubTypes.Type(
      value = OpenAiRealtimeSessionCreatedEvent.class,
      name = "session.created"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeSessionUpdatedEvent.class,
      name = "session.updated"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationCreatedEvent.class,
      name = "conversation.created"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemCreatedEvent.class,
      name = "conversation.item.created"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemInputAudioTranscriptionDeltaEvent.class,
      name = "conversation.item.input_audio_transcription.delta"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemInputAudioTranscriptionCompletedEvent.class,
      name = "conversation.item.input_audio_transcription.completed"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemInputAudioTranscriptionFailedEvent.class,
      name = "conversation.item.input_audio_transcription.failed"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemTruncatedEvent.class,
      name = "conversation.item.truncated"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemDeletedEvent.class,
      name = "conversation.item.deleted"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeInputAudioBufferCommittedEvent.class,
      name = "input_audio_buffer.committed"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeInputAudioBufferClearedEvent.class,
      name = "input_audio_buffer.cleared"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeInputAudioBufferSpeechStartedEvent.class,
      name = "input_audio_buffer.speech_started"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeInputAudioBufferSpeechStoppedEvent.class,
      name = "input_audio_buffer.speech_stopped"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseCreatedEvent.class,
      name = "response.created"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseDoneEvent.class,
      name = "response.done"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseOutputItemAddedEvent.class,
      name = "response.output_item.added"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseOutputItemDoneEvent.class,
      name = "response.output_item.done"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseContentPartDoneEvent.class,
      name = "response.content_part.done"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseContentPartAddedEvent.class,
      name = "response.content_part.added"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseAudioDeltaEvent.class,
      name = "response.audio.delta"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseAudioDoneEvent.class,
      name = "response.audio.done"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseAudioTranscriptDeltaEvent.class,
      name = "response.audio_transcript.delta"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseAudioTranscriptDoneEvent.class,
      name = "response.audio_transcript.done"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseFunctionCallArgumentsDeltaEvent.class,
      name = "response.function_call_arguments.delta"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseFunctionCallArgumentsDoneEvent.class,
      name = "response.function_call_arguments.done"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseTextDeltaEvent.class,
      name = "response.text.delta"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseTextDoneEvent.class,
      name = "response.text.done"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeRateLimitsUpdatedEvent.class,
      name = "rate_limits.updated"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemCreateEvent.class,
      name = "conversation.item.create"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemDeleteEvent.class,
      name = "conversation.item.delete"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeConversationItemTruncateEvent.class,
      name = "conversation.item.truncate"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeInputAudioBufferAppendEvent.class,
      name = "input_audio_buffer.append"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeInputAudioBufferClearEvent.class,
      name = "input_audio_buffer.clear"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeInputAudioBufferCommitEvent.class,
      name = "input_audio_buffer.commit"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseCancelEvent.class,
      name = "response.cancel"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeResponseCreateEvent.class,
      name = "response.create"
    ),
    @JsonSubTypes.Type(
      value = OpenAiRealtimeSessionUpdateEvent.class,
      name = "session.update"
    ),
    @JsonSubTypes.Type(value = OpenAiRealtimeErrorEvent.class, name = "error"),
  }
)
public interface OpenAiRealtimeEvent extends RealtimeEvent {
  String getType();
}
