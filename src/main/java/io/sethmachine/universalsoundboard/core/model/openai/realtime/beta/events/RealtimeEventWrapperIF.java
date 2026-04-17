package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events;

import com.hubspot.immutables.style.HubSpotStyle;
import java.time.Instant;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface RealtimeEventWrapperIF {
  RealtimeEvent getEvent();
  Instant getReceivedAt();
}
