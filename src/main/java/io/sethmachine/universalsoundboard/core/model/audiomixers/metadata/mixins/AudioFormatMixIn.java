package io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.mixins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.sound.sampled.AudioFormat.Encoding;

public abstract class AudioFormatMixIn extends AudioFormatMixInBase {

  @JsonCreator
  public AudioFormatMixIn(
    @JsonProperty("encoding") Encoding encoding,
    @JsonProperty("sampleRate") float sampleRate,
    @JsonProperty("sampleSizeInBits") int sampleSizeInBits,
    @JsonProperty("channels") int channels,
    @JsonProperty("frameSize") int frameSize,
    @JsonProperty("frameRate") float frameRate,
    @JsonProperty("bigEndian") boolean bigEndian,
    @JsonProperty("properties") Map<String, Object> properties
  ) {}
}
