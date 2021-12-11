package io.sethmachine.universalsoundboard.db.daos;

import com.hubspot.rosetta.jdbi3.BindWithRosetta;
import io.sethmachine.universalsoundboard.db.model.audiomixer.wiring.AudioMixerWiringInsert;
import io.sethmachine.universalsoundboard.db.model.audiomixer.wiring.AudioMixerWiringRow;
import java.util.List;
import java.util.Optional;
import org.jdbi.v3.sqlobject.SingleValue;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AudioMixerWiringDAO {
  String AUDIO_MIXER_WIRING_INSERT_FIELDS =
    "\"sinkId\", \"sinkName\", \"sourceId\", \"sourceName\"";
  String AUDIO_MIXER_WIRING_INSERT_BINDING_FIELDS =
    ":sinkId, :sinkName, :sourceId, :sourceName";

  @SqlUpdate(
    "INSERT INTO audio_mixer_wiring (" +
    AUDIO_MIXER_WIRING_INSERT_FIELDS +
    ") VALUES (" +
    AUDIO_MIXER_WIRING_INSERT_BINDING_FIELDS +
    ")"
  )
  int insert(@BindWithRosetta AudioMixerWiringInsert insert);

  @SqlQuery(
    "SELECT * FROM audio_mixer_wiring WHERE \"sinkId\" = :sinkId AND \"sourceId\" = :sourceId"
  )
  @SingleValue
  Optional<AudioMixerWiringRow> get(
    @Bind("sinkId") int sinkId,
    @Bind("sourceId") int sourceId
  );

  @SqlQuery("SELECT * FROM audio_mixer_wiring WHERE \"sinkId\" = :sinkId")
  List<AudioMixerWiringRow> getSinkWirings(@Bind("sinkId") int sinkId);

  @SqlQuery("SELECT * FROM audio_mixer_wiring WHERE \"sourceId\" = :sourceId")
  List<AudioMixerWiringRow> getSourceWirings(@Bind("sourceId") int sourceId);

  @SqlQuery("SELECT * FROM audio_mixer_wiring")
  List<AudioMixerWiringRow> getAllWirings();
}
