package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent;

import org.immutables.value.Value.Default;

public interface PlayAudioFileRequestCore {
  int getSourceId();

  @Default
  default boolean getReformat() {
    return true;
  }
}
