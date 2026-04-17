package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import static io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session.MaxResponseOutputTokensUnlimitedIF.INFINITE_TOKENS_VALUE;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class MaxResponseOutputTokensSerializer
  extends JsonSerializer<MaxResponseOutputTokens> {

  @Override
  public void serialize(
    MaxResponseOutputTokens value,
    JsonGenerator gen,
    SerializerProvider serializers
  ) throws IOException {
    if (value instanceof MaxResponseOutputTokensLimited) {
      // If the value is an instance of MaxResponseOutputTokensLimited, serialize it as an integer
      gen.writeNumber(((MaxResponseOutputTokensLimited) value).getValue());
    } else if (value instanceof MaxResponseOutputTokensUnlimited) {
      // If the value is an instance of MaxResponseOutputTokensInfinite, serialize it as a string "inf"
      gen.writeString(INFINITE_TOKENS_VALUE);
    } else {
      // Handle other cases (or throw an exception if it's unexpected)
      throw new IOException("Unknown type for MaxResponseOutputTokens");
    }
  }
}
