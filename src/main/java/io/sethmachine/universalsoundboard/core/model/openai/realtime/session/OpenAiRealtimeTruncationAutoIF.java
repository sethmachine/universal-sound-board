package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeTruncationAutoIF extends OpenAiRealtimeTruncation {
  String TRUNCATION_AUTO_VALUE = "auto";
}
