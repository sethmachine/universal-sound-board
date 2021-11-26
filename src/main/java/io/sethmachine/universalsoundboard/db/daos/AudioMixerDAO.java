package io.sethmachine.universalsoundboard.db.daos;

import java.util.Optional;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import com.hubspot.rosetta.jdbi3.BindWithRosetta;

import io.sethmachine.universalsoundboard.db.audiomixer.AudioMixerInsert;
import io.sethmachine.universalsoundboard.db.audiomixer.AudioMixerRow;

public interface AudioMixerDAO {
  String AUDIO_MIXER_INSERT_FIELDS =
      "\"name\", \"description\", \"vendor\", \"version\", \"audioMixerType\", \"audioFormat\"";
  String AUDIO_MIXER_INSERT_BINDING_FIELDS =
      ":name, :description, :vendor, :version, :audioMixerType, :audioFormat";

  @SqlUpdate(
      "INSERT INTO audio_mixer (" +
          AUDIO_MIXER_INSERT_FIELDS +
          ") VALUES (" +
          AUDIO_MIXER_INSERT_BINDING_FIELDS +
          ")"
  )
  void insert(@BindWithRosetta AudioMixerInsert insert);

  @SqlQuery("SELECT * FROM audio_mixer WHERE \"id\" = :id")
  Optional<AudioMixerRow> get(@Bind("id") int id);
}
