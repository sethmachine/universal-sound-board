--liquibase formatted sql

--changeset sethmachine:1
-- casing matters, since derby defaults to all caps by default but this breaks Rosetta/JDBI mapper
-- note the table name will be all caps by default since it's not in quotes
-- the columns are quoted so they are lowercased to work with Rosetta/JDBI mappers
create TABLE audio_mixer (
  "id" INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START with 1, INCREMENT BY 1), -- Autoincrement id of the audio mixer device being used
  "name" VARCHAR(255) NOT NULL, -- The human readable name identifying the audio mixer
  "description" VARCHAR(255) NOT NULL, -- The description of the audio mixer (defined by the vendor, not the user)
  "vendor" VARCHAR(255) NOT NULL, -- The vendor/maker of the audio mixer
  "version" VARCHAR(255) NOT NULL, -- The version of the audio mixer
  "audioMixerType" VARCHAR(255) NOT NULL, -- The type of the audio mixer, maps to Java enum, either SINK or SOURCE
  "audioFormat" LONG VARCHAR NOT NULL, -- The serialized JSON string of the audio format used by the sampler
  "dataLineName" VARCHAR(255) NOT NULL, -- The human readable name of the javax.sound data line used to open the audio mixer device
  PRIMARY KEY ("id")
);

--rollback DROP TABLE "audio_mixer";

--changeset sethmachine:2
create TABLE audio_mixer_wiring (
  "sinkId" INTEGER NOT NULL, -- Identifies the audio mixer sink (row in audio_mixer table) used to write audio to a source
  "sinkName" VARCHAR(255) NOT NULL, -- The human readable name of the audio mixer sink
  "sourceId" INTEGER NOT NULL, -- Identifies the audio mixer source (row in audio_mixer table) that the sink writes audio to
  "sourceName" VARCHAR(255) NOT NULL, -- The human readable name of the audio mixer source
  PRIMARY KEY ("sinkId", "sourceId")
);

--rollback DROP TABLE audio_mixer_wiring;
