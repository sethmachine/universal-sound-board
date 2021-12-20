package io.sethmachine.universalsoundboard.core.model.concurrent.source;

import com.hubspot.immutables.style.HubSpotStyle;
import javax.sound.sampled.AudioInputStream;

import org.immutables.value.Value.Immutable;

@Immutable
@HubSpotStyle
public interface AudioFileStreamIF {
  String getFilename();
  AudioInputStream getAudioInputStream();
  int getTotalBytes();
}
