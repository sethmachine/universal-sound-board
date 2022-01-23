package io.sethmachine.universalsoundboard.service;

import static io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil.createAudioFileStream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.inject.name.Named;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.source.PlayAudioToSourceRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.concurrent.source.AudioFileStream;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoundBoardService {

  private static final Logger LOG = LoggerFactory.getLogger(SoundBoardService.class);

  private final AudioMixersService audioMixersService;
  private final AudioMixerWiringService audioMixerWiringService;
  private final PlayAudioToSourceRunnableFactory playAudioToSourceRunnableFactory;
  private final ThreadPoolExecutor sourceThreadPoolExecutor;
  private final EventBus eventBus;

  @Inject
  public SoundBoardService(
    AudioMixersService audioMixersService,
    AudioMixerWiringService audioMixerWiringService,
    PlayAudioToSourceRunnableFactory playAudioToSourceRunnableFactory,
    @Named(
      "PlayAudioToSourceThreadPoolExecutor"
    ) ThreadPoolExecutor sourceThreadPoolExecutor,
    @Named("SourceEventBus") EventBus eventBus
  ) {
    this.audioMixersService = audioMixersService;
    this.audioMixerWiringService = audioMixerWiringService;
    this.playAudioToSourceRunnableFactory = playAudioToSourceRunnableFactory;
    this.sourceThreadPoolExecutor = sourceThreadPoolExecutor;
    this.eventBus = eventBus;
  }

  public void playAudioToSourceAndSink(
    int sourceId,
    int sinkId,
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
    SinkAudioMixer sink = audioMixersService
      .getSinkAudioMixer(sinkId)
      .orElseThrow(() ->
        new NotFoundException(
          String.format(
            "No such sink audio mixer exists in the audio mixer table: id %d",
            sinkId
          )
        )
      );

    List<SourceAudioMixer> sources = audioMixerWiringService
      .getSourceMixersFromWirings(audioMixerWiringService.getSinkWirings(sinkId))
      .stream()
      .map(AudioFormatUtil::reformatSourceAudioMixerFormatIfItHasUnspecifiedValues)
      .collect(Collectors.toList());
    if (sources.isEmpty()) {
      throw new NotAllowedException(
        String.format(
          "Unable to play audio to the sink with id %d as it does not have any sources wired to it.",
          sinkId
        )
      );
    }
    try {
      byte[] audioFileBytes = inputStream.readAllBytes();
      // since the sources can have different formats we need to create a stream for each format
      Iterables
        .concat(sources, ImmutableList.of(source))
        .forEach(sourceAudioMixer ->
          playAudioBytesToSource(fileDetail, audioFileBytes, sourceAudioMixer, reformat)
        );
    } catch (IOException e) {
      e.printStackTrace();
      LOG.error(
        "Unable to read audio bytes from input file {}",
        fileDetail.getFileName(),
        e
      );
      throw new RuntimeException(e);
    }
  }

  private void playAudioBytesToSource(
    FormDataContentDisposition fileDetail,
    byte[] audioFileBytes,
    SourceAudioMixer source,
    boolean reformat
  ) {
    AudioFileStream audioFileStream = createAudioFileStream(
      fileDetail,
      audioFileBytes,
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
