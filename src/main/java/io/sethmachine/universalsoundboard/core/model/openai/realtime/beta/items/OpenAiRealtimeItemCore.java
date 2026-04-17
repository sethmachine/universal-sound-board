package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items;

import java.util.Optional;

public interface OpenAiRealtimeItemCore extends OpenAiRealtimeItem {
  Optional<String> getId();

  Optional<String> getObject();

  Optional<String> getStatus();
}
