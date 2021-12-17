package io.sethmachine.universalsoundboard.core.concurrent.source;

import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.concurrent.source.AudioFileStream;

public interface PlayAudioToSourceRunnableFactory {
  PlayAudioToSourceRunnable create(
    SourceAudioMixer sourceAudioMixer,
    AudioFileStream audioFileStream
  );
}
