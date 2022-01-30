package io.sethmachine.universalsoundboard.service;

import static io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil.createAudioFileStream;

import com.google.common.eventbus.EventBus;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.concurrent.source.AudioFileStream;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil;
import java.io.InputStream;
import java.util.concurrent.ThreadPoolExecutor;
import javax.inject.Inject;
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
    SourceAudioMixer source = audioMixersService
      .getSourceAudioMixer(sourceId)
      .map(AudioFormatUtil::reformatSourceAudioMixerFormatIfItHasUnspecifiedValues)
      .orElseThrow(() ->
        new NotFoundException(
          String.format(
            "No such source audio mixer exists in the audio mixer table: id %d",
            sourceId
          )
        )
      );

    AudioFileStream audioFileStream = createAudioFileStream(
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
}
