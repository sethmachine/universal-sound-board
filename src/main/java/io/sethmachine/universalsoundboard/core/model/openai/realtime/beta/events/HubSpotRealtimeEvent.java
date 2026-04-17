package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
  {
    @JsonSubTypes.Type(
      value = VoiceInactivityDetectedEvent.class,
      name = "voice_inactivity_detected"
    ),
  }
)
public interface HubSpotRealtimeEvent extends RealtimeEvent {}
