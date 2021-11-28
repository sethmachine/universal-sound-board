package io.sethmachine.universalsoundboard.service;

import com.google.inject.Inject;
import io.sethmachine.universalsoundboard.core.model.audiomixers.AudioMixerBase;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioMixerMetadataUtil;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.model.audiomixer.AudioMixerRow;
import java.util.Optional;
import javax.sound.sampled.Mixer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioMixersService {

  private static final Logger LOG = LoggerFactory.getLogger(AudioMixerMetadataUtil.class);

  private final AudioMixerDAO audioMixerDAO;
  private final AudioMixerMetadataUtil audioMixerMetadataUtil;

  @Inject
  public AudioMixersService(
    AudioMixerDAO audioMixerDAO,
    AudioMixerMetadataUtil audioMixerMetadataUtil
  ) {
    this.audioMixerDAO = audioMixerDAO;
    this.audioMixerMetadataUtil = audioMixerMetadataUtil;
  }

  public Optional<AudioMixerBase> getAudioMixer(int audioMixerId) {
    return audioMixerDAO
      .get(audioMixerId)
      .flatMap(audioMixerRow -> {
        AudioMixerMetadataQuery query = AudioMixerMetadataQuery
          .builder()
          .setAudioMixerName(audioMixerRow.getName())
          .setAudioMixerType(audioMixerRow.getAudioMixerType())
          .build();
        return audioMixerMetadataUtil
          .findFirstMixerMatchingQuery(query)
          .flatMap(mixer -> {
            if (audioMixerRow.getAudioMixerType() == AudioMixerType.SINK) {
              return Optional.of(buildSinkAudioMixer(audioMixerRow, mixer));
            } else if (audioMixerRow.getAudioMixerType() == AudioMixerType.SOURCE) {
              return Optional.of(buildSourceAudioMixer(audioMixerRow, mixer));
            }
            LOG.error(
              "Unsupported audio mixer type: {}",
              audioMixerRow.getAudioMixerType()
            );
            return Optional.empty();
          });
      });
  }

  public Optional<SinkAudioMixer> getSinkAudioMixer(int audioMixerId) {
    return getAudioMixer(audioMixerId).map(base -> (SinkAudioMixer) base);
  }

  public Optional<SourceAudioMixer> getSourceAudioMixer(int audioMixerId) {
    return getAudioMixer(audioMixerId).map(base -> (SourceAudioMixer) base);
  }

  public int insertAudioMixer() {
    return 0;
  }

  private SinkAudioMixer buildSinkAudioMixer(AudioMixerRow audioMixerRow, Mixer mixer) {
    return SinkAudioMixer
      .builder()
      .setAudioMixerDescription(buildAudioMixerDescription(audioMixerRow))
      .setAudioFormat(audioMixerRow.getAudioFormat())
      .setMixer(mixer)
      .build();
  }

  private SourceAudioMixer buildSourceAudioMixer(
    AudioMixerRow audioMixerRow,
    Mixer mixer
  ) {
    return SourceAudioMixer
      .builder()
      .setAudioMixerDescription(buildAudioMixerDescription(audioMixerRow))
      .setAudioFormat(audioMixerRow.getAudioFormat())
      .setMixer(mixer)
      .build();
  }

  private AudioMixerDescription buildAudioMixerDescription(AudioMixerRow audioMixerRow) {
    return AudioMixerDescription
      .builder()
      .setName(audioMixerRow.getName())
      .setVendor(audioMixerRow.getVendor())
      .setDescription(audioMixerRow.getDescription())
      .setVersion(audioMixerRow.getVersion())
      .build();
  }
}
