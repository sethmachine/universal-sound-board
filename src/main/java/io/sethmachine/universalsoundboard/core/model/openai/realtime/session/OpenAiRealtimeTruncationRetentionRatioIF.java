package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
@JsonSerialize(as = OpenAiRealtimeTruncationRetentionRatio.class)
public interface OpenAiRealtimeTruncationRetentionRatioIF
  extends OpenAiRealtimeTruncation {
  @Default
  default String getType() {
    return "retention_ratio";
  }

  double getRetentionRatio();
}
