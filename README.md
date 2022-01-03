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

### Provide a yaml config that can be used at runtime

How to parse yaml with Java: https://www.baeldung.com/jackson-yaml
How to provide custom command line arguments to Dropwizard at runtime: https://stackoverflow.com/questions/34119690/how-to-accept-custom-command-line-arguments-with-dropwizard

### Assisted inject stuff

You need this dependency first: https://mvnrepository.com/artifact/com.google.inject.extensions/guice-assistedinject

https://stackoverflow.com/questions/8976250/how-to-use-guices-assistedinject
https://github.com/google/guice/wiki/AssistedInject



## Derby stuff

Derby automatically capitalizes all columns/tables names by default. So when you use Rosetta you should use Rosetta property to make this mapping explicit.

Supported data types: https://db.apache.org/derby/docs/10.7/ref/crefsqlj31068.html

Use blob for raw JSON strings

## curl HTTP requests

### Audio Mixers Resource

#### Get all audio mixers

This finds all sinks
curl -X GET -H "Content-Type: application/json" "localhost:8080/audio-mixer-metadata/descriptions?audioMixerType=SINK" | python -m json.tool

This finds sources:

curl -X GET -H "Content-Type: application/json" "localhost:8080/audio-mixer-metadata/descriptions?audioMixerType=SOURCE" | python -m json.tool

        {
            "name": "BlackHole 2ch",
            "vendor": "Existential Audio Inc.",
            "description": "Direct Audio Device: BlackHole 2ch",
            "version": "Unknown Version",
            "supportedAudioMixerTypes": [
                "SINK",
                "SOURCE"
            ]
        },


{
  "name": "MacBook Pro Microphone",
  "vendor": "Apple Inc.",
  "description": "Direct Audio Device: MacBook Pro Microphone",
  "version": "Unknown Version"
}

{
    "name": "BlackHole 2ch",
    "vendor": "Existential Audio Inc.",
    "description": "Direct Audio Device: BlackHole 2ch",
    "version": "Unknown Version"
}

        {
            "name": "MacBook Pro Speakers",
            "vendor": "Apple Inc.",
            "description": "Direct Audio Device: MacBook Pro Speakers",
            "version": "Unknown Version",
            "supportedAudioMixerTypes": [
                "SOURCE"
            ]
        },


Get it's audio formats and choose one

--data-urlencode "paramName=value"

"Bose QC35 II"

curl -G --data-urlencode "audioMixerName=Bose QC35 II" "localhost:8080/audio-mixer-metadata/audio-formats" | python -m json.tool

curl -G --data-urlencode "audioMixerName=MacBook Pro Microphone" "localhost:8080/audio-mixer-metadata/audio-formats" | python -m json.tool

curl -G --data-urlencode "audioMixerName=BlackHole 2ch" --data-urlencode "audioMixerType=SOURCE" "localhost:8080/audio-mixer-metadata/audio-formats" | python -m json.tool

{"audioMixerDescription":{"name":"BlackHole 2ch","vendor":"Existential Audio Inc.","description":"Direct Audio Device: BlackHole 2ch","version":"Unknown Version","supportedAudioMixerTypes":["SINK","SOURCE"]},"audioFormat":{"encoding":{"name":"PCM_SIGNED"},"sampleRate":44100.0,"sampleSizeInBits":16,"channels":2,"frameSize":4,"frameRate":44100.0,"bigEndian":false},"audioMixerTypeForFormat":"SOURCE","dataLineName":"interface SourceDataLine supporting 14 audio formats, and buffers of at least 32 bytes"}



{
    "encoding": {curl -G --data-urlencode "audioMixerName=MacBook Pro Speakers" "localhost:8080/audio-mixer-metadata/audio-formats" | python -m json.tool
        "name": "PCM_SIGNED"
    },
    "sampleRate": 48000.0,
    "sampleSizeInBits": 16,
    "channels": 1,
    "frameSize": 2,
    "frameRate": 48000.0,
    "bigEndian": true,
    "properties": null
}

            {
                "encoding": {
                    "name": "PCM_SIGNED"
                },
                "sampleRate": 44100.0,
                "sampleSizeInBits": 16,
                "channels": 2,
                "frameSize": 4,
                "frameRate": 44100.0,
                "bigEndian": true,
                "properties": null
            }


{"audioMixerDescription":{"name":"MacBook Pro Speakers","vendor":"Apple Inc.","description":"Direct Audio Device: MacBook Pro Speakers","version":"Unknown Version","supportedAudioMixerTypes":["SOURCE"]},"audioFormat":{"encoding":{"name":"PCM_SIGNED"},"sampleRate":48000.0,"sampleSizeInBits":16,"channels":2,"frameSize":4,"frameRate":48000.0,"bigEndian":false},"audioMixerTypeForFormat":"SOURCE","dataLineName":"interface SourceDataLine supporting 14 audio formats, and buffers of at least 32 bytes"}

#### Create the audio mixer

Bose headphones microphone sink

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"audioMixerDescription":{"name":"Bose QC35 II","vendor":"9e","description":"Direct Audio Device: Bose QC35 II","version":"Unknown Version","supportedAudioMixerTypes":["SINK"]},"audioFormat":{"encoding":{"name":"PCM_UNSIGNED"},"sampleRate":-1.0,"sampleSizeInBits":8,"channels":1,"frameSize":1,"frameRate":-1.0,"bigEndian":false},"audioMixerTypeForFormat":"SINK","dataLineName":"interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes"}'

MAcbook speakers

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"audioMixerDescription":{"name":"BlackHole 2ch","vendor":"Existential Audio Inc.","description":"Direct Audio Device: BlackHole 2ch","version":"Unknown Version","supportedAudioMixerTypes":["SINK","SOURCE"]},"audioFormat":{"encoding":{"name":"PCM_SIGNED"},"sampleRate":44100.0,"sampleSizeInBits":16,"channels":2,"frameSize":4,"frameRate":44100.0,"bigEndian":false},"audioMixerTypeForFormat":"SOURCE","dataLineName":"interface SourceDataLine supporting 14 audio formats, and buffers of at least 32 bytes"}'

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"audioMixerDescription":{"name":"MacBook Pro Speakers","vendor":"Apple Inc.","description":"Direct Audio Device: MacBook Pro Speakers","version":"Unknown Version","supportedAudioMixerTypes":["SOURCE"]},"audioFormat":{"encoding":{"name":"PCM_SIGNED"},"sampleRate":48000.0,"sampleSizeInBits":16,"channels":2,"frameSize":4,"frameRate":48000.0,"bigEndian":false},"audioMixerTypeForFormat":"SOURCE","dataLineName":"interface SourceDataLine supporting 14 audio formats, and buffers of at least 32 bytes"}'

Macbook microphones


curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"audioMixerDescription":{"name":"MacBook Pro Microphone","vendor":"Apple Inc.","description":"Direct Audio Device: MacBook Pro Microphone","version":"Unknown Version","supportedAudioMixerTypes":["SINK"]},"audioFormat":{"encoding":{"name":"PCM_SIGNED"},"sampleRate":48000.0,"sampleSizeInBits":16,"channels":1,"frameSize":2,"frameRate":48000.0,"bigEndian":true},"audioMixerTypeForFormat":"SINK","dataLineName":"interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes"}'

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"name": "MacBook Pro Microphone", "vendor": "Apple Inc.", "description": "Direct Audio Device: MacBook Pro Microphone", "version": "Unknown Version", "audioFormat": { "encoding": { "name": "PCM_SIGNED" }, "sampleRate": 48000.0, "sampleSizeInBits": 16, "channels": 1, "frameSize": 2, "frameRate": 48000.0, "bigEndian": true}, "audioMixerType": "SINK"}'

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"audioMixerDescription":{"name":"BlackHole 2ch","vendor":"Existential Audio Inc.","description":"Direct Audio Device: BlackHole 2ch","version":"Unknown Version","supportedAudioMixerTypes":["SINK","SOURCE"]},"audioFormat":{"encoding":{"name":"PCM_SIGNED"},"sampleRate":44100.0,"sampleSizeInBits":16,"channels":2,"frameSize":4,"frameRate":44100.0,"bigEndian":true},"audioMixerTypeForFormat":"SOURCE","dataLineName":"interface Clip supporting 14 audio formats, and buffers of at least 32 bytes"}'

curl -X GET -H "Content-Type: application/json" localhost:8080/audio-mixers/1

Response: {"audioMixerId":101}

Blackhole 2 channel

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"name": "BlackHole 2ch", "vendor": "Existential Audio Inc.", "description": "Direct Audio Device: BlackHole 2ch", "version": "Unknown Version", "audioFormat": {"encoding": { "name": "PCM_SIGNED" }, "sampleRate": 44100.0, "sampleSizeInBits": 16, "channels": 2, "frameSize": 4, "frameRate": 44100.0, "bigEndian": true}, "audioMixerType":"SINK"}'

Response: {"audioMixerId":201}

#### Wire up a sink and source



#### Start the sink audio mixer

curl -X POST -H "Content-Type: application/json" localhost:8080/sink/start --data '{"sinkId":101}'

 blackhole 2 channel
curl -X POST -H "Content-Type: application/json" localhost:8080/sink/start --data '{"sinkId":201}'

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixer --data '{"bigEndian": true, "encoding": {
"name": "PCM_SIGNED"}, "sampleRate": 48000.0, "sampleSizeInBits": 16, "channels": 2, "frameSize": 4, "frameRate": 48000.0 }'


VB-Cable

curl -G --data-urlencode audioMixerName="VB-Cable" localhost:8080/audio-mixer-metadata/audio-formats

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixer --data '{"bigEndian": true, "encoding": {
"name": "PCM_SIGNED"}, "sampleRate": 48000.0, "sampleSizeInBits": 16, "channels": 2, "frameSize": 4, "frameRate": 48000.0 }'

Insert an audio mixer device:

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"name": "VB-Cable", "vendor": "VB Audio", "description": "Direct Audio Device: VB-Cable", "version": "Unknown Version", "audioMixerType": "SINK", "audioFormat": {"bigEndian": true, "encoding": {"name": "PCM_SIGNED"}, "sampleRate": 48000.0, "sampleSizeInBits": 16, "channels": 2, "frameSize": 4, "frameRate":
48000.0 }}' 

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixers --data '{"name": "VB-Cable-Source", "vendor": "VB Audio", "description": "Direct Audio Device: VB-Cable", "version": "Unknown Version", "audioMixerType": "SOURCE", "audioFormat": {"bigEndian": true, "encoding": {"name": "PCM_SIGNED"}, "sampleRate": 48000.0, "sampleSizeInBits": 16, "channels": 2, "frameSize": 4, "frameRate":
48000.0 }}'

curl -X GET -H "Content-Type: application/json" localhost:8080/audio-mixers/1

Wire audio devices together:

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixer-wiring --data '{"sinkId":1,
"sourceId":2}'

curl -X POST -H "Content-Type: application/json" localhost:8080/audio-mixer-wiring --data '{"sinkId":501,
"sourceId":401}'

Start a sink

curl -X POST -H "Content-Type: application/json" localhost:8080/sinks/start --data '{"sinkId": 1}'

curl -X POST -H "Content-Type: application/json" localhost:8080/sinks/start --data '{"sinkId": 501}'


### upload files and play audio clips

// sourceId 301 is my macbook speakers

curl -H "Content-Type:multipart/form-data" -F 'audioFile=@/Users/sdworman/laugh.wav' -F "sourceCommandRequest={\"sourceId\":301, \"reformat\":\"false\"};type=application/json" localhost:8080/sources/play

curl -H "Content-Type:multipart/form-data" -F 'audioFile=@/Users/sdworman/laugh.wav' -F "sourceCommandRequest={\"sourceId\":301, \"reformat\":\"true\"};type=application/json" localhost:8080/sources/play

curl -H "Content-Type:multipart/form-data" -F 'audioFile=@/Users/sdworman/laugh48.wav' -F "sourceCommandRequest={\"sourceId\":301};type=application/json" localhost:8080/sources/play

curl -H "Content-Type:multipart/form-data" -F 'audioFile=@/Users/sdworman/laugh.wav' -F "sourceCommandRequest={\"sourceId\":301};type=application/json" localhost:8080/sources/play

curl -H "Content-Type:multipart/form-data" -F 'audioFile=@/Users/sdworman/laugh.wav' -F "sourceCommandRequest={\"sourceId\":201};type=application/json" localhost:8080/sources/play

sound board resource, "play" to a sink and a source simultaneously

curl -H "Content-Type:multipart/form-data" -F 'audioFile=@/Users/sdworman/laugh.wav' -F "playAudioFileToSourceAndSinkRequest={\"sinkId\":1, \"sourceId\":301, \"reformat\":\"true\"};type=application/json" localhost:8080/sound-board/play

### change audio formats with ffmpeg

https://askubuntu.com/questions/421607/how-to-convert-an-audio-streams-format

ffmpeg -i laugh.wav -acodec s16be -ar 48000 laugh48-be.wav

### Override configurations

This overrides where the database is stored:

-Ddw.database.url=jdbc:derby:</path/to/database>;create=true

where `</path/to/database/>` is any path to where the database is to exist (properly formatted for the operating system).  E.g. it might `/Users/foobar/myUsbDerbyDb` on macOS but 
`C:\opt\myUsbDerbyDb` on Windows.  

This overrides the port the application runs on:

-Ddw.server.applicationConnectors[0].port=9999

You would only override the port if you had some very specific setup or other applications 
running on the default port of `8080`.  

