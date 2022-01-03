package io.sethmachine.universalsoundboard.core.concurrent.sink;

import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import java.util.List;

public interface SinkAudioMixerRunnableFactory {
  SinkAudioMixerRunnable create(SinkAudioMixer sink, List<SourceAudioMixer> sources);
}
