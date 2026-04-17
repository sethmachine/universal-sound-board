package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeClientSecretIF {
  String getValue(); // The client secret value

  long getExpiresAt(); // The expiration time of the client secret (Unix timestamp, epoch seconds)
}
