package io.sethmachine.universalsoundboard.service;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.source.AudioBytesConsumer;
import io.sethmachine.universalsoundboard.core.concurrent.source.FileAudioBytesConsumer;
import io.sethmachine.universalsoundboard.core.concurrent.source.SourceAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.source.SourceAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.concurrent.source.StopSourceEvent;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadPoolExecutor;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceAudioMixerRunnableService {

  private static final Logger LOG = LoggerFactory.getLogger(
    SourceAudioMixerRunnableService.class
  );

  private final AudioMixersService audioMixersService;
  private final SourceAudioMixerRunnableFactory sourceAudioMixerRunnableFactory;
  private final ThreadPoolExecutor sourceThreadPoolExecutor;
  private final EventBus eventBus;

  @Inject
  public SourceAudioMixerRunnableService(
    AudioMixersService audioMixersService,
    SourceAudioMixerRunnableFactory sourceAudioMixerRunnableFactory,
    @Named("SourceCaptureThreadPoolExecutor") ThreadPoolExecutor sourceThreadPoolExecutor,
    @Named("SourceEventBus") EventBus eventBus
  ) {
    this.audioMixersService = audioMixersService;
    this.sourceAudioMixerRunnableFactory = sourceAudioMixerRunnableFactory;
    this.sourceThreadPoolExecutor = sourceThreadPoolExecutor;
    this.eventBus = eventBus;
  }

  public void startOutboundVoiceAgent(int sourceId) {
    SourceAudioMixer source = audioMixersService
      .getSourceAudioMixer(sourceId)
      .orElseThrow(() ->
        new NotFoundException(
          String.format(
            "No such source audio mixer exists in the audio mixer table: id %d",
            sourceId
          )
        )
      );

    AudioBytesConsumer consumer = createFileConsumer(source);

    SourceAudioMixerRunnable sourceAudioMixerRunnable = sourceAudioMixerRunnableFactory.create(
      source,
      consumer
    );
    sourceThreadPoolExecutor.execute(sourceAudioMixerRunnable);
    LOG.info(
      "Started capturing audio from source: {}",
      source.getAudioMixerDescription()
    );
  }

  public void stopSourceCapture(int sourceId) {
    audioMixersService
      .getSourceAudioMixer(sourceId)
      .orElseThrow(() ->
        new NotFoundException(
          String.format(
            "No such source audio mixer exists in the audio mixer table: id %d",
            sourceId
          )
        )
      );
    eventBus.post(StopSourceEvent.builder().setSourceId(sourceId).build());
    LOG.info("Stopping capture from source with id: {}", sourceId);
  }

  public int getTotalActiveSourceCaptures() {
    return this.sourceThreadPoolExecutor.getActiveCount();
  }

  private AudioBytesConsumer createFileConsumer(SourceAudioMixer source) {
    String timestamp = LocalDateTime
      .now()
      .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    String sanitizedSourceName = source.getAudioMixerDescription().getName();
    String filename = String.format(
      "capture_source_%d_%s_%s.wav",
      source.getAudioMixerId(),
      sanitizedSourceName,
      timestamp
    );

    File outputFile = new File(filename);
    LOG.info(
      "Creating file audio consumer for source {}: {}",
      source.getAudioMixerId(),
      outputFile.getAbsolutePath()
    );

    return new FileAudioBytesConsumer(source.getAudioFormat(), outputFile);
  }
}
