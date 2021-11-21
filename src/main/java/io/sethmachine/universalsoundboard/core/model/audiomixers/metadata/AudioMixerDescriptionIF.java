package io.sethmachine.universalsoundboard.core.model.audiomixers.metadata;

import com.hubspot.immutables.style.HubSpotStyle;
import java.util.Comparator;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerDescriptionIF extends Comparable<AudioMixerDescriptionIF> {
  String getName();

  String getVendor();

  String getDescription();

  String getVersion();

  @Override
  default int compareTo(AudioMixerDescriptionIF other) {
    return Comparator.<String>naturalOrder().compare(getName(), other.getName());
  }
}
