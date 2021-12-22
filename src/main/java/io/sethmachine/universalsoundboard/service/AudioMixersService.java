package io.sethmachine.universalsoundboard.service;

import com.google.inject.Inject;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerId;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.CreateAudioMixerRequest;
import io.sethmachine.universalsoundboard.core.model.audiomixers.AudioMixerBase;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SinkAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerDescription;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
import io.sethmachine.universalsoundboard.core.util.audiomixer.AudioMixerMetadataUtil;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.model.audiomixer.AudioMixerInsert;
import io.sethmachine.universalsoundboard.db.model.audiomixer.AudioMixerRow;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    return audioMixerDAO.get(audioMixerId).flatMap(this::generateAudioMixerBaseFromRow);
  }

  public Optional<SinkAudioMixer> getSinkAudioMixer(int audioMixerId) {
    return getAudioMixer(audioMixerId).map(base -> (SinkAudioMixer) base);
  }

  public Optional<SourceAudioMixer> getSourceAudioMixer(int audioMixerId) {
    return getAudioMixer(audioMixerId).map(base -> (SourceAudioMixer) base);
  }

  public List<AudioMixerBase> getAllAudioMixers() {
    return audioMixerDAO
      .getAll()
      .stream()
      .map(this::generateAudioMixerBaseFromRow)
      .filter(Optional::isPresent)
      .map(Optional::get)
      .collect(Collectors.toList());
  }

  public AudioMixerId createAudioMixer(CreateAudioMixerRequest createAudioMixerRequest) {
    return AudioMixerId
      .builder()
      .setId(
        audioMixerDAO.insert(
          AudioMixerInsert
            .builder()
            .setName(createAudioMixerRequest.getAudioMixerDescription().getName())
            .setDescription(
              createAudioMixerRequest.getAudioMixerDescription().getDescription()
            )
            .setVendor(createAudioMixerRequest.getAudioMixerDescription().getVendor())
            .setVersion(createAudioMixerRequest.getAudioMixerDescription().getVersion())
            .setAudioFormat(createAudioMixerRequest.getAudioFormat())
            .setAudioMixerType(createAudioMixerRequest.getAudioMixerTypeForFormat())
            .setDataLineName(createAudioMixerRequest.getDataLineName())
            .build()
        )
      )
      .build();
  }

  public void deleteAudioMixer(int audioMixerId) {
    audioMixerDAO.deleteAudioMixer(audioMixerId);
  }

  private Optional<AudioMixerBase> generateAudioMixerBaseFromRow(
    AudioMixerRow audioMixerRow
  ) {
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
        LOG.error("Unsupported audio mixer type: {}", audioMixerRow.getAudioMixerType());
        return Optional.empty();
      });
  }

  private SinkAudioMixer buildSinkAudioMixer(AudioMixerRow audioMixerRow, Mixer mixer) {
    return SinkAudioMixer
      .builder()
      .setAudioMixerId(audioMixerRow.getId())
      .setAudioMixerDescription(buildAudioMixerDescription(audioMixerRow, mixer))
      .setAudioFormat(audioMixerRow.getAudioFormat())
      .setMixer(mixer)
      .setDataLineName(audioMixerRow.getDataLineName())
      .build();
  }

  private SourceAudioMixer buildSourceAudioMixer(
    AudioMixerRow audioMixerRow,
    Mixer mixer
  ) {
    return SourceAudioMixer
      .builder()
      .setAudioMixerId(audioMixerRow.getId())
      .setAudioMixerDescription(buildAudioMixerDescription(audioMixerRow, mixer))
      .setAudioFormat(audioMixerRow.getAudioFormat())
      .setMixer(mixer)
      .setDataLineName(audioMixerRow.getDataLineName())
      .build();
  }

  private AudioMixerDescription buildAudioMixerDescription(
    AudioMixerRow audioMixerRow,
    Mixer mixer
  ) {
    return AudioMixerDescription
      .builder()
      .setName(audioMixerRow.getName())
      .setVendor(audioMixerRow.getVendor())
      .setDescription(audioMixerRow.getDescription())
      .setVersion(audioMixerRow.getVersion())
      .setSupportedAudioMixerTypes(
        audioMixerMetadataUtil.findSupportedAudioMixerTypes(mixer.getMixerInfo())
      )
      .build();
  }
}
