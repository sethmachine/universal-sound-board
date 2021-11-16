--liquibase formatted sql

--changeset sethmachine:1
-- casing matters, since derby defaults to all caps by default but this breaks Rosetta/JDBI mapper
CREATE TABLE audio_mixer (
  id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), -- Autoincrement id of the audio mixer device being used
  name VARCHAR(255) NOT NULL, -- The human readable name identifying the audio mixer
  PRIMARY KEY (id)
);

--rollback DROP TABLE audio_mixer;

--changeset sethmachine:2
CREATE TABLE audio_mixer_wiring (
  sinkAudioMixerId INTEGER NOT NULL, -- Identifies the audio mixer sink used to write audio to a source
  sourceAudioMixerId INTEGER NOT NULL, -- Identifies the audio mixer source that the sink writes audio to
  PRIMARY KEY (sinkAudioMixerId, sourceAudioMixerId)
);

--rollback DROP TABLE audio_mixer_wiring;