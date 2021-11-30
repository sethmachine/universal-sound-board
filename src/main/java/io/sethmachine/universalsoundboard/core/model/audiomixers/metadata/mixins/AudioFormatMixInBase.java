package io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.mixins;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import javax.sound.sampled.AudioFormat.Encoding;

@JsonInclude(Include.NON_NULL)
public abstract class AudioFormatMixInBase {

  @JsonProperty("encoding")
  abstract Encoding getEncoding();

  @JsonProperty("sampleRate")
  abstract float getSampleRate();

  @JsonProperty("sampleSizeInBits")
  abstract int getSampleSizeInBits();

  @JsonProperty("channels")
  abstract int getChannels();

  @JsonProperty("frameSize")
  abstract int getFrameSize();

  @JsonProperty("frameRate")
  abstract float getFrameRate();

  @JsonProperty("bigEndian")
  abstract boolean getBigEndian();

  @JsonProperty("properties")
  abstract Map<String, Object> getProperties();
}
