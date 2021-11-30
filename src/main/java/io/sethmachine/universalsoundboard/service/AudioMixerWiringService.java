package io.sethmachine.universalsoundboard.service;

import com.google.common.collect.ImmutableList;
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

  private final AudioMixerDAO audioMixerDAO;
  private final AudioMixerWiringDAO audioMixerWiringDAO;

  @Inject
  public AudioMixerWiringService(
    AudioMixerDAO audioMixerDAO,
    AudioMixerWiringDAO audioMixerWiringDAO
  ) {
    this.audioMixerDAO = audioMixerDAO;
    this.audioMixerWiringDAO = audioMixerWiringDAO;
  }

  public void wireSinkToSource(int sinkId, int sourceId) throws NotFoundException {
    Optional<AudioMixerRow> maybeSink = audioMixerDAO.get(sinkId);
    Optional<AudioMixerRow> maybeSource = audioMixerDAO.get(sourceId);
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
    validateSinkAndSourceWiring(maybeSink.get(), maybeSource.get());

    AudioMixerWiringInsert insert = AudioMixerWiringInsert
      .builder()
      .setSinkId(sinkId)
      .setSinkName(maybeSink.get().getName())
      .setSourceId(sourceId)
      .setSourceName(maybeSource.get().getName())
      .build();
    audioMixerWiringDAO.insert(insert);
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
