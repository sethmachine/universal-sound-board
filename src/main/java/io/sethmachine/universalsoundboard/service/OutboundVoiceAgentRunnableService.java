package io.sethmachine.universalsoundboard.service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Named;

import io.sethmachine.universalsoundboard.core.concurrent.openai.OpenAiRealtimeClient;
import io.sethmachine.universalsoundboard.core.concurrent.openai.OutboundVoiceAgentRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.openai.VoiceAgentSinkRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.openai.VoiceAgentSourceRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.sink.StopSinkEvent;
import io.sethmachine.universalsoundboard.core.concurrent.source.SourceAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.StartOutboundVoiceAgentRequest;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.StopOutboundVoiceAgentRequest;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil;

public class OutboundVoiceAgentRunnableService {

  private static final Logger LOG = LoggerFactory.getLogger(
    OutboundVoiceAgentRunnableService.class
  );

  private final EventBus sinkEventBus;
  private final OpenAiRealtimeClient.Factory openAiRealtimeClientFactory;
  private final AudioMixersService audioMixersService;
  private final SourceAudioMixerRunnableFactory sourceAudioMixerRunnableFactory;
  private final ThreadPoolExecutor sourceThreadPoolExecutor;
  private final OutboundVoiceAgentRunnable.Factory outboundVoiceAgentRunnableFactory;
  private final VoiceAgentSourceRunnable.Factory voiceAgentSourceRunnableFactory;
  private final VoiceAgentSinkRunnable.Factory voiceAgentSinkRunnableFactory;

  @Inject
  public OutboundVoiceAgentRunnableService(
    OpenAiRealtimeClient.Factory openAiRealtimeClientFactory,
    AudioMixersService audioMixersService,
    SourceAudioMixerRunnableFactory sourceAudioMixerRunnableFactory,
    OutboundVoiceAgentRunnable.Factory outboundVoiceAgentRunnableFactory,
    VoiceAgentSourceRunnable.Factory voiceAgentSourceRunnableFactory,
    VoiceAgentSinkRunnable.Factory voiceAgentSinkRunnableFactory,
    @Named("SinkEventBus") EventBus sinkEventBus,
    @Named("SourceCaptureThreadPoolExecutor") ThreadPoolExecutor sourceThreadPoolExecutor
  ) {
    this.openAiRealtimeClientFactory = openAiRealtimeClientFactory;
    this.audioMixersService = audioMixersService;
    this.sourceAudioMixerRunnableFactory = sourceAudioMixerRunnableFactory;
    this.outboundVoiceAgentRunnableFactory = outboundVoiceAgentRunnableFactory;
    this.voiceAgentSourceRunnableFactory = voiceAgentSourceRunnableFactory;
    this.voiceAgentSinkRunnableFactory = voiceAgentSinkRunnableFactory;
    this.sourceThreadPoolExecutor = sourceThreadPoolExecutor;
    this.sinkEventBus = sinkEventBus;
  }

  public void startVoiceAgent(StartOutboundVoiceAgentRequest startOutboundVoiceAgentRequest) throws InterruptedException {
    SourceAudioMixer source = audioMixersService
        .getSourceAudioMixer(startOutboundVoiceAgentRequest.getSourceId())
        .map(AudioFormatUtil::reformatSourceAudioMixerFormatIfItHasUnspecifiedValues)
        .orElseThrow(() ->
            new NotFoundException(
                String.format(
                    "No such source audio mixer exists in the audio mixer table: id %d",
                    startOutboundVoiceAgentRequest.getSourceId()
                )
            )
        );

    SinkAudioMixer sink = audioMixersService
        .getSinkAudioMixer(startOutboundVoiceAgentRequest.getSinkId())
        .orElseThrow(() ->
            new NotFoundException(
                String.format(
                    "No such source audio mixer exists in the audio mixer table: id %d",
                    startOutboundVoiceAgentRequest.getSourceId()
                )
            )
        );


    EventBus eventBus = new EventBus();
    BlockingQueue<String> incomingAudioBytesQueue = new LinkedBlockingQueue<>();
    BlockingQueue<String> outgoingAudioBytesQueue = new LinkedBlockingQueue<>();

    OpenAiRealtimeClient openAiRealtimeClient = openAiRealtimeClientFactory.create(
      eventBus
    );

    OutboundVoiceAgentRunnable outboundVoiceAgentRunnable = outboundVoiceAgentRunnableFactory.create(
        eventBus,
      incomingAudioBytesQueue,
      outgoingAudioBytesQueue,
      openAiRealtimeClient
    );
    VoiceAgentSourceRunnable voiceAgentSourceRunnable = voiceAgentSourceRunnableFactory.create(source, outgoingAudioBytesQueue);
    VoiceAgentSinkRunnable voiceAgentSinkRunnable = voiceAgentSinkRunnableFactory.create(sink, incomingAudioBytesQueue);

    sourceThreadPoolExecutor.execute(outboundVoiceAgentRunnable);
    sourceThreadPoolExecutor.execute(voiceAgentSourceRunnable);
    sourceThreadPoolExecutor.execute(voiceAgentSinkRunnable);
  }

  public void stopVoiceAgent(StopOutboundVoiceAgentRequest stopOutboundVoiceAgentRequest) {
      sinkEventBus.post(StopSinkEvent.builder().setSinkId(stopOutboundVoiceAgentRequest.getSinkId()).build());
      LOG.info("Stopping capture from source with id: {}", stopOutboundVoiceAgentRequest.getSinkId());
    }
}
