package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent;

import com.hubspot.immutables.style.HubSpotStyle;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface PlayAudioFileToSourceAndSinkRequestIF extends PlayAudioFileRequestCore {
  int getSinkId();
}
