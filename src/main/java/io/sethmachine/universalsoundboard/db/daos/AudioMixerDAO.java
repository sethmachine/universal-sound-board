package io.sethmachine.universalsoundboard.db.daos;

import com.hubspot.rosetta.jdbi3.BindWithRosetta;
import io.sethmachine.universalsoundboard.db.model.audiomixer.AudioMixerInsert;
import io.sethmachine.universalsoundboard.db.model.audiomixer.AudioMixerRow;
import java.util.List;
import java.util.Optional;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface AudioMixerDAO {
  String AUDIO_MIXER_INSERT_FIELDS =
    "\"name\", \"description\", \"vendor\", \"version\", \"audioMixerType\", \"audioFormat\", " +
    "\"dataLineName\"";
  String AUDIO_MIXER_INSERT_BINDING_FIELDS =
    ":name, :description, :vendor, :version, :audioMixerType, :audioFormat, :dataLineName";

  @SqlUpdate(
    "INSERT INTO audio_mixer (" +
    AUDIO_MIXER_INSERT_FIELDS +
    ") VALUES (" +
    AUDIO_MIXER_INSERT_BINDING_FIELDS +
    ")"
  )
  @GetGeneratedKeys
  int insert(@BindWithRosetta AudioMixerInsert insert);

  @SqlQuery("SELECT * FROM audio_mixer WHERE \"id\" = :id")
  Optional<AudioMixerRow> get(@Bind("id") int id);

  @SqlQuery("SELECT * FROM audio_mixer FETCH FIRST 1000 ROWS ONLY")
  List<AudioMixerRow> getAll();
}
