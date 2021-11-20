package io.sethmachine.universalsoundboard.db.daos;

import com.hubspot.rosetta.jdbi3.RosettaRowMapperFactory;
import io.sethmachine.universalsoundboard.db.audio.mixer.AudioMixerRow;
import java.util.List;
import java.util.Optional;
import org.jdbi.v3.sqlobject.SingleValue;
import org.jdbi.v3.sqlobject.config.RegisterRowMapperFactory;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterRowMapperFactory(RosettaRowMapperFactory.class)
public interface AudioMixerDAO {
  @SqlUpdate("INSERT INTO audio_mixer (name) VALUES (:name)")
  void insert(@Bind("name") String name);

  @SqlQuery("SELECT id, name FROM audio_mixer WHERE id = :id FETCH FIRST 1 ROWS ONLY")
  @SingleValue
  Optional<AudioMixerRow> findNameById(@Bind("id") int id);

  @SqlQuery("SELECT id, name FROM audio_mixer")
  List<AudioMixerRow> getAllAudioMixers();
}
