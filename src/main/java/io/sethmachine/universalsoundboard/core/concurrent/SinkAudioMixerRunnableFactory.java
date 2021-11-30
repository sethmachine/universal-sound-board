package io.sethmachine.universalsoundboard.core.concurrent;

import java.util.List;

import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;

public interface SinkAudioMixerRunnableFactory {
  SinkAudioMixerRunnable create(SinkAudioMixer sink,
                                List<SourceAudioMixer> sources);
}
