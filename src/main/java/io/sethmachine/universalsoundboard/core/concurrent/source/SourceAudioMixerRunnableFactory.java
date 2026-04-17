package io.sethmachine.universalsoundboard.core.concurrent.source;

import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;

public interface SourceAudioMixerRunnableFactory {
  SourceAudioMixerRunnable create(
    SourceAudioMixer source,
    AudioBytesConsumer audioBytesConsumer
  );
}
