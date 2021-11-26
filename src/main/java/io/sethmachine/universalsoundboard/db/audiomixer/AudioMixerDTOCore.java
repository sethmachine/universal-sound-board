package io.sethmachine.universalsoundboard.db.audiomixer;

import javax.sound.sampled.AudioFormat;

import com.hubspot.rosetta.annotations.StoredAsJson;

import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;

public interface AudioMixerDTOCore {
  String getName();

  String getVendor();

  String getDescription();

  String getVersion();

  AudioMixerType getAudioMixerType();

  @StoredAsJson
  AudioFormat getAudioFormat();
}
