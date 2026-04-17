package io.sethmachine.universalsoundboard.core.concurrent.openai;

import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEvent;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.apache.http.client.utils.URIBuilder;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wrap the <a href="https://platform.openai.com/docs/guides/realtime">Open AI Realtime API</a>.
 */
public class OpenAiRealtimeClient extends WebSocketClient {

  public interface Factory {
    OpenAiRealtimeClient create(EventBus eventBus);
  }

  private static final String OPEN_AI_API_KEY =
    "put your key here";
  private static final String DEFAULT_OPEN_AI_MODEl = "gpt-realtime-2025-08-28";

  private static final Logger LOG = LoggerFactory.getLogger(OpenAiRealtimeClient.class);
  private static final Duration WEBSOCKET_TIMEOUT_CONNECT = Duration.ofSeconds(2);
  private static final String OPEN_AI_REALTIME_WEBSOCKET_URI =
    "wss://api.openai.com/v1/realtime";

  private final OpenAiRealtimeModelSerializer openAiRealtimeModelSerializer;
  private final EventBus eventBus;

  @Inject
  public OpenAiRealtimeClient(
    OpenAiRealtimeModelSerializer openAiRealtimeModelSerializer,
    @Assisted EventBus eventBus
  ) throws URISyntaxException {
    super(
      new URIBuilder(OPEN_AI_REALTIME_WEBSOCKET_URI)
        .addParameter("model", DEFAULT_OPEN_AI_MODEl)
        .build(),
      new Draft_6455(),
      createHeaders(OPEN_AI_API_KEY),
      (int) WEBSOCKET_TIMEOUT_CONNECT.toMillis()
    );
    this.openAiRealtimeModelSerializer = openAiRealtimeModelSerializer;
    this.eventBus = eventBus;
    this.eventBus.register(this);
  }

  public EventBus getEventBus() {
    return eventBus;
  }

  @Override
  public void onOpen(ServerHandshake handshakedata) {
    LOG.info("Opened connection: {}", handshakedata.toString());
    // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
  }

  @Override
  public void onMessage(String message) {
//    LOG.info("Received Open AI WebSocket message: {}", message);
    openAiRealtimeModelSerializer
      .deserializeOpenAiRealtimeEvent(message, OpenAiRealtimeEvent.class)
      .ifPresentOrElse(
        openAiRealtimeEvent -> {
//          LOG.info("Successfully deserialized event: {}", openAiRealtimeEvent);
          eventBus.post(openAiRealtimeEvent);
        },
        () -> LOG.error("Failed to deserialize event!")
      );
  }

  @Override
  public void onClose(int code, String reason, boolean remote) {
    LOG.info("Connection closed by {}. Code: {}, Reason: {}", remote, code, reason);
  }

  @Override
  public void onError(Exception e) {
    LOG.error("Error occurred:", e);
  }

  public void sendMessage(OpenAiRealtimeEvent openAiRealtimeEvent) {
//    LOG.info("Would send Open AI WebSocket message: {}", openAiRealtimeEvent);
    openAiRealtimeModelSerializer.serializeOpenAiRealtimePayload(openAiRealtimeEvent).
        ifPresent(this::send);
  }

  /**
   * See details on <a href="https://platform.openai.com/docs/guides/realtime-websocket#connection-details">required headers:</a>.
   */
  private static Map<String, String> createHeaders(String apiKey) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Authorization", "Bearer " + apiKey);
    headers.put("OpenAI-Beta", "realtime=v1");
    return headers;
  }
}
