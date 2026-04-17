package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

/**
 * Configuration for turn detection. Can be set to null to turn off.
 * Server VAD means that the model will detect the start and
 * end of speech based on audio volume and respond at the end of user speech.
 */
@HubSpotStyle
@Immutable
public interface OpenAiRealtimeTurnDetectionSettingsIF {
  Optional<String> getType();

  Optional<Double> getThreshold(); // Activation threshold for VAD (0.0 to 1.0), defaults to 0.5

  Optional<Integer> getPrefixPaddingMs(); // Amount of audio to include before VAD detected speech, defaults to 300ms

  Optional<Integer> getSilenceDurationMs(); // Duration of silence to detect speech stop, defaults to 500ms

  Optional<Boolean> getCreateResponse();
}
