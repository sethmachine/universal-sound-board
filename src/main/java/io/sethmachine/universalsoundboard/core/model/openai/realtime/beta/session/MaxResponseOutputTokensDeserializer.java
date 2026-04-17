package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import static io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.MaxResponseOutputTokensUnlimitedIF.INFINITE_TOKENS_VALUE;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class MaxResponseOutputTokensDeserializer
  extends JsonDeserializer<MaxResponseOutputTokens> {

  @Override
  public MaxResponseOutputTokens deserialize(JsonParser p, DeserializationContext ctxt)
    throws IOException {
    // Check the type of the current token (whether it's an integer or a string)
    if (p.getCurrentToken().isNumeric()) {
      // If it's an integer, create a MaxResponseOutputTokensLimited
      return MaxResponseOutputTokensLimited.builder().setValue(p.getIntValue()).build();
    } else if (
      p.getCurrentToken().isScalarValue() && p.getText().equals(INFINITE_TOKENS_VALUE)
    ) {
      // If it's the string "inf", create a MaxResponseOutputTokensInfinite
      return MaxResponseOutputTokensUnlimited.builder().build();
    } else {
      // Handle other cases as needed
      throw new IOException("Unknown type for maxResponseOutputTokens");
    }
  }
}
