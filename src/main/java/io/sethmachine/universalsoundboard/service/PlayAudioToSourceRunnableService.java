package io.sethmachine.universalsoundboard.service;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.concurrent.source.AudioFileStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import javax.inject.Inject;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.ws.rs.NotFoundException;
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
    boolean reformat,
    InputStream inputStream,
    FormDataContentDisposition fileDetail
  ) {
    SourceAudioMixer source = validateAndGetSource(sourceId);
    AudioFileStream audioFileStream = buildAudioFileStream(
      inputStream,
      fileDetail,
      source.getAudioFormat(),
      reformat
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
          "No such source audio mixer exists in the audio mixer table: id %d",
          sourceId
        )
      );
    }
    return source.get();
  }

  private AudioFileStream buildAudioFileStream(
    InputStream inputStream,
    FormDataContentDisposition fileDetail,
    AudioFormat sourceAudioFormat,
    boolean reformat
  ) {
    try {
      byte[] allInputBytes = inputStream.readAllBytes();
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
        new ByteArrayInputStream(allInputBytes)
      );
      if (reformat) {
        if (!audioInputStream.getFormat().matches(sourceAudioFormat)) {
          LOG.info(
            "Attempting to reformat audio file input stream to match source audio mixer"
          );
          audioInputStream =
            reformatAudioInputStream(audioInputStream, sourceAudioFormat);
          LOG.info(
            "Successfully reformatted audio file input stream to match source audio mixer format"
          );
        }
      }
      return AudioFileStream
        .builder()
        .setFilename(fileDetail.getFileName())
        .setAudioInputStream(audioInputStream)
        .setTotalBytes(allInputBytes.length)
        .build();
    } catch (UnsupportedAudioFileException | IOException e) {
      e.printStackTrace();
      LOG.error(
        "Unable to read input file {} into an audio input stream",
        fileDetail.getFileName(),
        e
      );
      throw new RuntimeException(e);
    }
  }

  private static AudioInputStream reformatAudioInputStream(
    AudioInputStream audioInputStream,
    AudioFormat targetFormat
  ) {
    if (AudioSystem.isConversionSupported(targetFormat, audioInputStream.getFormat())) {
      return AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
    }
    LOG.error(
      "Conversion between formats unsupported.  Source format: {},  Target format: {}",
      audioInputStream.getFormat(),
      targetFormat
    );
    return audioInputStream;
  }
}
