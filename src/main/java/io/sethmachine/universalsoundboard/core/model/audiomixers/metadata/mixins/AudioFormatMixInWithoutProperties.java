package io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.mixins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.sound.sampled.AudioFormat.Encoding;

public abstract class AudioFormatMixInWithoutProperties extends AudioFormatMixInBase {

  @JsonCreator
  public AudioFormatMixInWithoutProperties(
    @JsonProperty("encoding") Encoding encoding,
    @JsonProperty("sampleRate") float sampleRate,
    @JsonProperty("sampleSizeInBits") int sampleSizeInBits,
    @JsonProperty("channels") int channels,
    @JsonProperty("frameSize") int frameSize,
    @JsonProperty("frameRate") float frameRate,
    @JsonProperty("bigEndian") boolean bigEndian
  ) {}
}
