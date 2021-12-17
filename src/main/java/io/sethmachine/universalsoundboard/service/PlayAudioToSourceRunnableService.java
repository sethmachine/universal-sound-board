package io.sethmachine.universalsoundboard.service;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.concurrent.sink.StopSinkEvent;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.wiring.AudioMixerWiringPair;
import io.sethmachine.universalsoundboard.core.model.concurrent.source.AudioFileStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.ws.rs.NotFoundException;
import javax.xml.transform.Source;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayAudioToSourceRunnableService {

  private static final Logger LOG = LoggerFactory.getLogger(
    PlayAudioToSourceRunnableService.class
  );

  private final AudioMixersService audioMixersService;
  private final PlayAudioToSourceRunnableFactory playAudioToSourceRunnableFactory;
  private final ThreadPoolExecutor sourceThreadPoolExecutor;
  private final EventBus eventBus;

  @Inject
  public PlayAudioToSourceRunnableService(
    AudioMixersService audioMixersService,
    PlayAudioToSourceRunnableFactory playAudioToSourceRunnableFactory,
    @Named(
      "PlayAudioToSourceThreadPoolExecutor"
    ) ThreadPoolExecutor sourceThreadPoolExecutor,
    @Named("SourceEventBus") EventBus eventBus
  ) {
    this.audioMixersService = audioMixersService;
    this.playAudioToSourceRunnableFactory = playAudioToSourceRunnableFactory;
    this.sourceThreadPoolExecutor = sourceThreadPoolExecutor;
    this.eventBus = eventBus;
  }

  public void playAudio(
    int sourceId,
    InputStream inputStream,
    FormDataContentDisposition fileDetail
  ) {
    SourceAudioMixer source = validateAndGetSource(sourceId);
    AudioFileStream audioFileStream = buildAudioFileStream(
      inputStream,
      fileDetail,
      source.getAudioFormat()
    );
    PlayAudioToSourceRunnable playAudioToSourceRunnable = playAudioToSourceRunnableFactory.create(
      source,
      audioFileStream
    );
    sourceThreadPoolExecutor.execute(playAudioToSourceRunnable);
  }

  private SourceAudioMixer validateAndGetSource(int sourceId) {
    Optional<SourceAudioMixer> source = audioMixersService.getSourceAudioMixer(sourceId);
    if (source.isEmpty()) {
      throw new NotFoundException(
        String.format(
          "No such sink audio mixer exists in the audio mixer table: id %d",
          sourceId
        )
      );
    }
    return source.get();
  }

  private AudioFileStream buildAudioFileStream(
    InputStream inputStream,
    FormDataContentDisposition fileDetail,
    AudioFormat sourceAudioFormat
  ) {
    try {
      // since playback is done async, we have to read all the bytes when the audio file is uploaded
      // otherwise we'll only get the first chunk of the audio and not play the full file!
      return AudioFileStream
        .builder()
        .setFilename(fileDetail.getFileName())
        .setAudioInputStream(
          AudioSystem.getAudioInputStream(
            new ByteArrayInputStream(inputStream.readAllBytes())
          )
        )
        .build();
    } catch (IOException | UnsupportedAudioFileException e) {
      LOG.error("Failed to create audio file stream", e);
      e.printStackTrace();
    }
    throw new RuntimeException("Failed to create audio file stream");
  }
}
