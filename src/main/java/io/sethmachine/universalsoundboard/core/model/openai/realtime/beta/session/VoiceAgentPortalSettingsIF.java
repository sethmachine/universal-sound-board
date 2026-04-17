package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.google.common.collect.ImmutableList;
import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface VoiceAgentPortalSettingsIF {
  String getInstructions();
  ImmutableList<VoiceAgentTool> getTools();
}
