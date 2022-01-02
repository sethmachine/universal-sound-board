package io.sethmachine.universalsoundboard.service;

import io.sethmachine.universalsoundboard.core.model.audiomixers.AudioMixerBase;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.wiring.AudioMixerWiringPair;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerWiringDAO;
import io.sethmachine.universalsoundboard.db.model.audiomixer.AudioMixerRow;
import io.sethmachine.universalsoundboard.db.model.audiomixer.wiring.AudioMixerWiringInsert;
import io.sethmachine.universalsoundboard.db.model.audiomixer.wiring.AudioMixerWiringRow;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioMixerWiringService {

  private static final Logger LOG = LoggerFactory.getLogger(
    AudioMixerWiringService.class
  );

  private final AudioMixersService audioMixersService;
  private final AudioMixerWiringDAO audioMixerWiringDAO;

  @Inject
  public AudioMixerWiringService(
    AudioMixersService audioMixersService,
    AudioMixerWiringDAO audioMixerWiringDAO
  ) {
    this.audioMixersService = audioMixersService;
    this.audioMixerWiringDAO = audioMixerWiringDAO;
  }

  public void wireSinkToSource(int sinkId, int sourceId) throws NotFoundException {
    Optional<SinkAudioMixer> maybeSink = audioMixersService.getSinkAudioMixer(sinkId);
    Optional<SourceAudioMixer> maybeSource = audioMixersService.getSourceAudioMixer(
      sourceId
    );
    if (maybeSink.isEmpty()) {
      throw new NotFoundException(
        String.format(
          "No such sink audio mixer with id %d found in the audio_mixer table",
          sinkId
        )
      );
    }
    if (maybeSource.isEmpty()) {
      throw new NotFoundException(
        String.format(
          "No such source audio mixer with id %d found in the audio_mixer table",
          sourceId
        )
      );
    }

    AudioMixerWiringInsert insert = AudioMixerWiringInsert
      .builder()
      .setSinkId(sinkId)
      .setSinkName(maybeSink.get().getAudioMixerDescription().getName())
      .setSourceId(sourceId)
      .setSourceName(maybeSource.get().getAudioMixerDescription().getName())
      .build();
    audioMixerWiringDAO.insert(insert);
  }

  public Optional<AudioMixerWiringPair> getWiring(int sinkId, int sourceId) {
    return audioMixerWiringDAO
      .get(sinkId, sourceId)
      .map(this::buildPairFromAudioWiringRow);
  }

  public List<AudioMixerWiringPair> getSinkWirings(int sinkId) {
    return audioMixerWiringDAO
      .getSinkWirings(sinkId)
      .stream()
      .map(this::buildPairFromAudioWiringRow)
      .collect(Collectors.toList());
  }

  public List<AudioMixerWiringPair> getSourceWirings(int sourceId) {
    return audioMixerWiringDAO
      .getSourceWirings(sourceId)
      .stream()
      .map(this::buildPairFromAudioWiringRow)
      .collect(Collectors.toList());
  }

  public List<AudioMixerWiringPair> getAllWirings() {
    return audioMixerWiringDAO
      .getAllWirings()
      .stream()
      .map(this::buildPairFromAudioWiringRow)
      .collect(Collectors.toList());
  }

  public void deleteSingleWiring(int sinkId, int sourceId) {
    audioMixerWiringDAO.deleteSingleWiring(sinkId, sourceId);
  }

  public void deleteAllSinkWirings(int sinkId) {
    audioMixerWiringDAO.deleteAllSinkWirings(sinkId);
  }

  public void deleteAllSourceWirings(int sourceId) {
    audioMixerWiringDAO.deleteAllSourceWirings(sourceId);
  }

  public List<SourceAudioMixer> getSourceMixersFromWirings(
    List<AudioMixerWiringPair> wirings
  ) {
    return wirings
      .stream()
      .map(wiring -> audioMixersService.getSourceAudioMixer(wiring.getSourceId()))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toList());
  }

  private void validateSinkAndSourceWiring(AudioMixerRow sink, AudioMixerRow source) {
    if (sink.getAudioMixerType() != AudioMixerType.SINK) {
      throw new IllegalArgumentException(
        "The audio mixer used for a sink must actually be a sink"
      );
    }

    if (source.getAudioMixerType() != AudioMixerType.SOURCE) {
      throw new IllegalArgumentException(
        "The audio mixer used for a source must actually be a source"
      );
    }
  }

  private AudioMixerWiringPair buildPairFromAudioWiringRow(AudioMixerWiringRow row) {
    return AudioMixerWiringPair
      .builder()
      .setSinkId(row.getSinkId())
      .setSinkName(row.getSinkName())
      .setSourceId(row.getSourceId())
      .setSourceName(row.getSourceName())
      .build();
  }
}
