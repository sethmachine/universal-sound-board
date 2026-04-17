package io.sethmachine.universalsoundboard.core.concurrent.openai;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.OpenAiRealtimeEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.conversation.OpenAiRealtimeConversationItemCreateEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.inputaudiobuffer.OpenAiRealtimeInputAudioBufferAppendEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.client.response.OpenAiRealtimeResponseCreateEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.events.server.response.OpenAiRealtimeResponseAudioDeltaEvent;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items.OpenAiRealtimeMessageContentInputText;
import io.sethmachine.universalsoundboard.core.model.openai.realtime.beta.items.OpenAiRealtimeMessageItem;

public class OutboundVoiceAgentRunnable implements Runnable {

  public interface Factory {
    OutboundVoiceAgentRunnable create(
        EventBus eventBus,
        @Assisted("incoming") BlockingQueue<String> incomingAudioBytesQueue,
        @Assisted("outgoing") BlockingQueue<String> outgoingAudioBytesQueue,
      OpenAiRealtimeClient openAiRealtimeClient
    );
  }


  private static final String WELCOME_MESSAGE =
      "You speak English.  Before you respond to the customer, introduce yourself with your name and role. Mention that you are an AI. Your name is Ava and your role is customer support.";
  // TODO: tune this as needed
  private static final long INCOMING_AUDIO_BYTES_POLL_TIMEOUT_MS = 100;

  private static final Logger LOG = LoggerFactory.getLogger(
    OutboundVoiceAgentRunnable.class
  );
  private static final int BUFFER_SIZE = 5;

  private final BlockingQueue<String> incomingAudioBytesQueue;
  private final BlockingQueue<String> outgoingAudioBytesQueue;
  private final EventBus eventBus;
  private final OpenAiRealtimeClient openAiRealtimeClient;

  private boolean stopped = false;

  @Inject
  public OutboundVoiceAgentRunnable(
    @Assisted EventBus eventBus,
    @Assisted("incoming") BlockingQueue<String> incomingAudioBytesQueue,
    @Assisted("outgoing") BlockingQueue<String> outgoingAudioBytesQueue,
    @Assisted OpenAiRealtimeClient openAiRealtimeClient
  ) {
    this.eventBus = eventBus;
    this.eventBus.register(this);
    this.incomingAudioBytesQueue = incomingAudioBytesQueue;
    this.outgoingAudioBytesQueue = outgoingAudioBytesQueue;
    this.openAiRealtimeClient = openAiRealtimeClient;
  }

  @Subscribe
  public void listen(OpenAiRealtimeEvent openAiRealtimeEvent) {

    if (!(openAiRealtimeEvent instanceof OpenAiRealtimeResponseAudioDeltaEvent)){
      LOG.info("Received open AI event: {}", openAiRealtimeEvent);
    }
  }

  @Subscribe
  public void listen(OpenAiRealtimeResponseAudioDeltaEvent audioDeltaEvent) throws InterruptedException {
//    LOG.info("Received audio delta event: {}", audioDeltaEvent);
    outgoingAudioBytesQueue.put(audioDeltaEvent.getDelta());
  }

  @Override
  public void run() {
    try {
      // start Open AI WebSocket connection, provide the same event bus as here
      LOG.info("Connecting to Open AI Realtime API");
      openAiRealtimeClient.connectBlocking();
      LOG.info("Connected to Open AI Realtime API");
      triggerWelcomeMessage();
      // make a subscribe method that pushes the Open AI audio bytes to the outgoing audio bytes queue
      // in the sink
      //      captureFromSource(source);
      while (!stopped){
        // read from incoming audio bytes
        String base64AudioBytes = outgoingAudioBytesQueue.poll(INCOMING_AUDIO_BYTES_POLL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
        if (base64AudioBytes != null && !base64AudioBytes.isEmpty()) {
          openAiRealtimeClient.sendMessage(
              OpenAiRealtimeInputAudioBufferAppendEvent
                  .builder()
                  .setType("input_audio_buffer.append")
                  .setAudio(base64AudioBytes)
                  .build()
          );
        }
      }
    } catch (InterruptedException e) {
      LOG.error("Unexpected error when running the Outbound Voice Agent", e);
    } finally {
      //      closeConsumerIfCloseable();
      eventBus.unregister(this);
    }
  }

  private void triggerWelcomeMessage() {
    OpenAiRealtimeMessageItem welcomeMessageItem =
        OpenAiRealtimeMessageItem
            .builder()
            .setRole("system")
            .setContent(
                ImmutableList.of(
                    OpenAiRealtimeMessageContentInputText
                        .builder()
                        .setText(WELCOME_MESSAGE)
                        .build()
                )
            )
            .build();

    // Send a system message to instruct the model on a welcome message
    openAiRealtimeClient.sendMessage(
        OpenAiRealtimeConversationItemCreateEvent
            .builder()
            .setItem(welcomeMessageItem)
            .build()
    );
    // Send a response create event to trigger the model to respond
    openAiRealtimeClient.sendMessage(OpenAiRealtimeResponseCreateEvent.builder().build());

  }
  //  private void captureFromSource(SourceAudioMixer source)
  //    throws LineUnavailableException {
  //    AudioFormat sourceAudioFormat = handleUnspecifiedValuesInAudioFormat(
  //      source.getAudioFormat()
  //    );
  //
  //    TargetDataLine targetDataLine = AudioSystem.getTargetDataLine(
  //      sourceAudioFormat,
  //      source.getMixer().getMixerInfo()
  //    );
  //
  //    targetDataLine.open(sourceAudioFormat);
  //    targetDataLine.start();
  //
  //    byte[] data = new byte[targetDataLine.getBufferSize() / BUFFER_SIZE];
  //    int numBytesRead;
  //
  //    LOG.info(
  //      "Started capturing audio from source: {}",
  //      source.getAudioMixerDescription()
  //    );
  //
  //    while (!stopped) {
  //      numBytesRead = targetDataLine.read(data, 0, data.length);
  //      LOG.debug(
  //        "Captured {} bytes from source ID {}",
  //        numBytesRead,
  //        source.getAudioMixerDescription()
  //      );
  //
  //      // we need to convert to the format open AI needs, then send to Open AI WebSocket client
  //
  //      audioBytesConsumer.accept(data, 0, numBytesRead);
  //    }
  //
  //    LOG.info("Stopping capture from source: {}", source.getAudioMixerDescription());
  //    cleanUp(targetDataLine);
  //  }
  //
  //  private void cleanUp(TargetDataLine targetDataLine) {
  //    targetDataLine.stop();
  //    targetDataLine.close();
  //    targetDataLine.flush();
  //  }

  //  private void closeConsumerIfCloseable() {
  //    if (audioBytesConsumer instanceof java.io.Closeable) {
  //      try {
  //        ((java.io.Closeable) audioBytesConsumer).close();
  //        LOG.info("Closed audio bytes consumer");
  //      } catch (Exception e) {
  //        LOG.error("Failed to close audio bytes consumer", e);
  //      }
  //    }
  //  }
}
