package io.sethmachine.universalsoundboard.core.concurrent;

import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;

public interface SinkAudioMixerRunnableFactory {
  SinkAudioMixerRunnable create(SinkAudioMixer sinkAudioMixer);
}
