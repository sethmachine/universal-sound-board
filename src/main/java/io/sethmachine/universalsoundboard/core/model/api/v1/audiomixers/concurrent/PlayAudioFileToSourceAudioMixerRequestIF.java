package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Default;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface PlayAudioFileToSourceAudioMixerRequestIF {
  int getSourceId();

  @Default
  default boolean getReformat() {
    return true;
  }
}
