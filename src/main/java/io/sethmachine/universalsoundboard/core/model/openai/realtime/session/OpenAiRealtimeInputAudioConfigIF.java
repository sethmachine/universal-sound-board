package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.OpenAiRealtimeTurnDetectionSettings;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
@JsonSerialize(as = OpenAiRealtimeInputAudioConfig.class)
public interface OpenAiRealtimeInputAudioConfigIF {
  Optional<OpenAiRealtimeAudioFormat> getFormat();

  Optional<OpenAiRealtimeInputAudioNoiseReduction> getNoiseReduction();

  Optional<OpenAiRealtimeInputAudioTranscription> getTranscription();

  Optional<OpenAiRealtimeTurnDetectionSettings> getTurnDetection();
}
