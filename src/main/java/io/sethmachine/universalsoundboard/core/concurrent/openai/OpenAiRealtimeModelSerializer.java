package io.sethmachine.universalsoundboard.core.concurrent.openai;

import static io.sethmachine.universalsoundboard.guice.UniversalSoundBoardModule.OPEN_AI_OBJECT_MAPPER;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAiRealtimeModelSerializer {

  private static final Logger LOG = LoggerFactory.getLogger(
    OpenAiRealtimeModelSerializer.class
  );

  private final ObjectMapper objectMapper;

  @Inject
  public OpenAiRealtimeModelSerializer(
    @Named(OPEN_AI_OBJECT_MAPPER) ObjectMapper objectMapper
  ) {
    this.objectMapper = objectMapper;
  }

  public <T> Optional<T> deserializeOpenAiRealtimeEvent(String json, Class<T> clazz) {
    try {
      return Optional.of(objectMapper.readValue(json, clazz));
    } catch (Exception exception) {
      LOG.error("Failed to deserialize due to: ", exception);
      return Optional.empty();
    }
  }

  public <T> Optional<String> serializeOpenAiRealtimePayload(
      T object
  ) {
    try {
      return Optional.of(
          objectMapper.writeValueAsString(object)
      );
    } catch (Exception exception) {
      LOG.error("Failed to serialize due to: ", exception);
      return Optional.empty();
    }
  }

}
