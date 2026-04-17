package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.response;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeInputTokenDetailsIF {
  int getCachedTokens();
  int getTextTokens();
  int getAudioTokens();
}
