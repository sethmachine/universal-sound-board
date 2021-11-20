package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerSupportedFormatsIF {
  AudioMixerDescription getAudioMixerDescription();

  Map<String, List<AudioFormat>> getSinkAudioFormats();

  Map<String, List<AudioFormat>> getSourceAudioFormats();
}
