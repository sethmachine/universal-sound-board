package io.sethmachine.universalsoundboard.core.concurrent;

public interface SinkAudioMixerRunnableFactory {
  SinkAudioMixerRunnable create(int sinkId);
}
