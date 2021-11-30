package io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.wiring;

import com.hubspot.immutables.style.HubSpotStyle;
import io.sethmachine.universalsoundboard.core.model.audiomixers.wiring.AudioMixerWiringPair;
import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioMixerWiringListIF {
  List<AudioMixerWiringPair> getWirings();
}
