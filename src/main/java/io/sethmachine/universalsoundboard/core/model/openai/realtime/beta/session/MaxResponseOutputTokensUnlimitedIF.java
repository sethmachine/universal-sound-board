package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@HubSpotStyle
@Immutable
public interface MaxResponseOutputTokensUnlimitedIF extends MaxResponseOutputTokens {
  String INFINITE_TOKENS_VALUE = "inf";
}
