package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerDescriptionsIF {
  List<AudioMixerDescription> getAudioMixerDescriptions();
}
