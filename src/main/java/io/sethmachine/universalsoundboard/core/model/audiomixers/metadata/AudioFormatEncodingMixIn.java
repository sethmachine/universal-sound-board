package io.sethmachine.universalsoundboard.core.model.audiomixers.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AudioFormatEncodingMixIn {

  @JsonProperty("name")
  abstract String getName();
}
