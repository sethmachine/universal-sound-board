package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeTruncationDisabledIF extends OpenAiRealtimeTruncation {
  String TRUNCATION_DISABLED_VALUE = "disabled";
}
