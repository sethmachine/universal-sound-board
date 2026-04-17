package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events;

import com.hubspot.immutables.style.HubSpotStyle;
import java.time.Instant;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface VoiceInactivityDetectedEventIF extends HubSpotRealtimeEvent {
  Instant getDetectedAt();
}
