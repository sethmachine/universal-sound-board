package io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.session;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize(using = MaxResponseOutputTokensDeserializer.class)
@JsonSerialize(using = MaxResponseOutputTokensSerializer.class)
public interface MaxResponseOutputTokens {}
