package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeToolIF {
  Optional<String> getType(); // The type of the tool, i.e., function

  Optional<String> getName(); // The name of the function

  Optional<String> getDescription(); // The description of the function, with guidance on when and how to call it

  Optional<OpenAiRealtimeToolParameters> getParameters(); // The parameters of the function in JSON Schema format
}
