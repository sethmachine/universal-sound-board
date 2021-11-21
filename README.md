# UniversalSoundBoard

How to start the UniversalSoundBoard application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/UniversalSoundBoard-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`

Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`

## Maven commands for non HubSpot environment

mvn -Dmaven.repo.local=/Users/sdworman/.m2/personal-repository -Pmaven-central archetype:generate \
-DarchetypeGroupId=io.dropwizard.archetypes \
-DarchetypeArtifactId=java-simple \
-DarchetypeVersion=2.0.25

mvn -Dmaven.repo.local=$HOME/.my/other/repository clean install

mvn -Dmaven.repo.local=/Users/sdworman/.m2/personal-repository -s ~/.m2/personal-settings.xml clean install

mvn -Dmaven.repo.local=/Users/sdworman/.m2/personal-repository -s ~/.m2/personal-settings.xml clean package

curl -d name=john --data-urlencode passwd=@31&3*J https://www.example.com

mvn prettier:check -Dmaven.repo.local=/Users/sdworman/.m2/personal-repository -s ~/.m2/personal-settings.xml

mvn prettier:write -Dmaven.repo.local=/Users/sdworman/.m2/personal-repository -s ~/.m2/personal-settings.xml

mvn -Dmaven.repo.local=/Users/sdworman/.m2/personal-repository -s ~/.m2/personal-settings.xml clean verify

-s ~/.m2/personal-settings.xml

mvn -s ~/.m2/personal-settings.xml -Dmaven.repo.local=/Users/sdworman/.m2/personal-repository -Pmaven-central archetype:generate \
-DarchetypeGroupId=io.dropwizard.archetypes \
-DarchetypeArtifactId=java-simple \
-DarchetypeVersion=2.0.25

org.apache.maven.plugins:maven-checkstyle-plugin:3.1.2:check

## Key requirements and functionality

* List all audio mixers for either target or source
* Create a target/sink audio mixer
* Create a source audio mixer
* Route the target to the source (also remove a source)
* Play an audio clip/file directly to the specified source
* Pause/mute the target, aka mute microphone
* Unpause/unmute the target, aka unmute microphone
* Persistence of audio wiring to a SQL database
    * probably 2 tables: one for each audio mixer used, and another for the wiring rules

## Derby stuff

Derby automatically capitalizes all columns/tables names by default. So when you use Rosetta you should use Rosetta property to make this mapping explicit.

Supported data types: https://db.apache.org/derby/docs/10.7/ref/crefsqlj31068.html

Use blob for raw JSON strings

## curl HTTP requests

VB-Cable

curl -G --data-urlencode audioMixerName="VB-Cable" localhost:8080/audio-mixer-metadata/supported-audio-formats

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixer --data '{"bigEndian": true, "encoding": {
"name": "PCM_SIGNED"}, "sampleRate": 48000.0, "sampleSizeInBits": 16, "channels": 2, "frameSize": 4, "frameRate": 48000.0 }' 

