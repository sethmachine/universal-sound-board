package io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.mixins;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AudioFormatEncodingMixIn {

  @JsonCreator
  public AudioFormatEncodingMixIn(@JsonProperty("name") String name) {}

  @JsonProperty("name")
  abstract String getName();
}
