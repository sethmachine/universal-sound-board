package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescriptionAndFormat;
import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface SingleAudioMixerDescriptionAndFormatsResponseIF {
  List<AudioMixerDescriptionAndFormat> getAudioMixerDescriptionAndFormats();
}
