package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events;

import java.util.Optional;

public interface OpenAiRealtimeEventCore extends OpenAiRealtimeEvent {
  Optional<String> getEventId();
}
