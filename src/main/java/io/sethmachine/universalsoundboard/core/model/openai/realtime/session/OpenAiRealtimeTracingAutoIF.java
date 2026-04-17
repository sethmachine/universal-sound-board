package io.sethmachine.universalsoundboard.core.model.openai.realtime.session;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface OpenAiRealtimeTracingAutoIF extends OpenAiRealtimeTracing {
  String TRACING_AUTO_VALUE = "auto";
}
