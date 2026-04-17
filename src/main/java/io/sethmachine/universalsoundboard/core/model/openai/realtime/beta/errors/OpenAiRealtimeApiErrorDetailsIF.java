package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.errors;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeApiErrorDetailsIF {
  Optional<String> getErrorType();
  Optional<String> getCode();
  Optional<String> getMessage();
  Optional<String> getParam();
  Optional<String> getEventId();
}
