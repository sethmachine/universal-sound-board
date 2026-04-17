package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
@JsonSerialize(as = OpenAiRealtimeAudioFormat.class)
public interface OpenAiRealtimeAudioFormatIF {
  String getType();

  int getRate();
}
