package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeInputAudioTranscriptionSettingsIF {
  Optional<String> getModel();
}
