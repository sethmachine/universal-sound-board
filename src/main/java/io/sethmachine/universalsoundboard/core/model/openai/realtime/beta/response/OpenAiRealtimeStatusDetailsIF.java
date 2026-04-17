package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.response;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeStatusDetailsIF {
  String getType();
  Optional<String> getReason();
  Optional<OpenAiRealtimeStatusDetailsError> getError();
}
