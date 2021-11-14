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

  -s ~/.m2/personal-settings.xml

  mvn -s ~/.m2/personal-settings.xml -Dmaven.repo.local=/Users/sdworman/.m2/personal-repository -Pmaven-central archetype:generate \
  -DarchetypeGroupId=io.dropwizard.archetypes \
  -DarchetypeArtifactId=java-simple \
  -DarchetypeVersion=2.0.25
  
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

    
