package io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import java.util.Optional;
import org.immutables.value.Value;

@Value.Immutable
@HubSpotStyle
public interface AudioMixerMetadataQueryIF {
  Optional<AudioMixerType> getAudioMixerType();

  Optional<String> getAudioMixerName();
}
