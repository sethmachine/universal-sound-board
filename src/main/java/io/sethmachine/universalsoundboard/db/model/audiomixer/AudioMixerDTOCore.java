package io.sethmachine.universalsoundboard.db.model.audiomixer;

import com.hubspot.rosetta.annotations.StoredAsJson;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import javax.sound.sampled.AudioFormat;

public interface AudioMixerDTOCore {
  String getName();

  String getVendor();

  String getDescription();

  String getVersion();

  AudioMixerType getAudioMixerType();

  @StoredAsJson
  AudioFormat getAudioFormat();

  String getDataLineName();
}
