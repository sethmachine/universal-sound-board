# UniversalSoundBoard

![Universal Sound Board Diagram](examples/USBD-diagram-revised-2022-05-16.jpg)

Ever wondered how to play audio through your microphone?  Universal Sound Board allows you to do just that!  

UniversalSoundBoard (USBD) is a Java application that allows for playing audio clips through your microphone with a virtual audio device.  Because USBD is written in Java, it can be run anywhere (tested on Windows and macOS).  USBD is a standalone application, so it will work with any voice chat application like Discord or Zoom.    

More specifically, USBD allows the wiring of input audio devices to output audio devices (audio wiring).  One example is sending physical microphone inputs to a virtual audio device, which effectively turns the virtual audio device into a microphone.  At the same time, you can tell USBD to play an audio file to a virtual audio device, which results in others hearing the audio file as if it were played on their computer.  

USBD's interface is through HTTP requests, so it can be integrated with any language or framework.  USBD provides a high level abstraction of the [Java Sound API](https://docs.oracle.com/javase/tutorial/sound/) to simplify programming sound board like applications.    

### Stack

* Dropwizard
* Derby embedded database
* Jackson
* Guice
* dropwizard-guicey
* Rosetta
* Liquibase

## Installation

UniversalSoundBoard is a standalone Java application that can be run with the compiled JAR and [dropwizard configuration YAML](https://github.com/sethmachine/universal-sound-board/blob/master/universal-sound-board.yml).  That being said, to play audio through the microphone it is necessary to install [a 3rd party virtual audio device](#virtual-audio-devices).

### Requirements

* Windows or macOS operating system (not tested on Linux)
* Java 11 runtime
* Java 11 development kit (if you want to build USBD from source)
* Virtual audio device that routes output to input (technically optional)


### Virtual Audio Devices

The following virtual audio devices have been successfully tested with USBD to play audio through the microphone. You will need to install these separately in order to use them with USBD. You only need one them, e.g. macOS has both Blackhole and Virtual Audio Cable, but you only need to install one of these.  

#### Windows
* [Virtual Audio Cable](https://vb-audio.com/Cable)
#### macOS
* [Virtual Audio Cable](https://vb-audio.com/Cable)
* [BlackHole](https://github.com/ExistentialAudio/BlackHole)

### Building

The follow is instructions on how to build the JAR from source.  

This has been tested with [Apache Maven](https://maven.apache.org/) 3.8.1 and an [OpenJDK 11](https://openjdk.java.net/projects/jdk/11/) runtime on macOS Catalina.

1. Clone the repository: `git clone https://github.com/sethmachine/universal-sound-board`
2. Open a command line and enter the cloned repo, e.g. `cd universal-sound-board/`
3. Build using Maven: `mvn clean verify`
4. Verify the fat JAR exists under `universal-sound-board/target/UniversalSoundBoard-1.0-SNAPSHOT.jar`

## Usage Guide

This guide shows how to set up UniversalSoundBoard (USBD) on Windows.  I'll use the following audio devices but you can substitute for your own physical microphone and speakers:

* Bose Headset microphone (substitute with whatever physical microphone you are using)
* Bose Headset output (substitute with whatever physical speakers you are using)
* Virtual Audio Cable Output
* Virtual Audio Cable Input

In the guide we'll complete these steps:

1. Start the Universal Sound Board webserver in the background
2. Choose a microphone, its audio format, and persist it to the database 
3. Choose the Virtual Audio Cable Input, its audio format, and persist it to the database
4. Wire the microphone to the Virtual Audio Cable Input
5. Configure Discord to use Virtual Audio Cable Output as the microphone 
6. Play a sound file that simultaneously plays on your speakers and through the microphone on Discord

### Requirements

* Java 11 runtime
* Access to a terminal or command line program
* An HTTP client to interact with USBD, e.g. [curl](https://curl.se/)
* [Virtual Audio Cable](https://vb-audio.com/Cable)
* (Optional) [Python](https://www.python.org/downloads/) or another command line program to pretty print JSON output

If you choose not to use Python, omit any `| python -m json.tool` from all command line examples.  

### Running the server

UniversalSoundBoard is started like any Dropwizard application from the command line.  USBD runs as a web server in the background as it routes audio from one device to another.  

You'll need a compiled JAR to run the server.  If necessary, follow the [build instructions to create the JAR](#building).  These examples will all use [curl](https://curl.se/) to interact with the UniversalSoundBoard server.   

* [Build](#building) or locate the fat JAR, e.g. `target/UniversalSoundBoard-1.0-SNAPSHOT.jar`
* Locate the [Dropwizard configuration YAML](https://github.com/sethmachine/universal-sound-board/blob/master/universal-sound-board.yml).  This tells Dropwizard how to run the server.  
* Run the server on the command line, e.g. `java -jar target/UniversalSoundBoard-1.0-SNAPSHOT.jar server universal-sound-board.yml`

Exiting the command line or hitting CTRL+C will stop the application. The server admin page can be visited at http://localhost:8081 where you can view the [healthchecks](http://localhost:8081/healthcheck?pretty=true).  The healthchecks can be ignored as they are not yet configured properly.  

You should see output like the following indicating the server has successfully started up:

```shell
INFO  [2022-01-09 16:17:22,952] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@7683ea43{Admin context,/,null,AVAILABLE}
INFO  [2022-01-09 16:17:22,961] org.eclipse.jetty.server.AbstractConnector: Started application@3e5c7588{HTTP/1.1, (http/1.1)}{0.0.0.0:8080}
INFO  [2022-01-09 16:17:22,963] org.eclipse.jetty.server.AbstractConnector: Started admin@5b2ff4df{HTTP/1.1, (http/1.1)}{0.0.0.0:8081}
INFO  [2022-01-09 16:17:22,963] org.eclipse.jetty.server.Server: Started @4233ms
```

After running the server for the first time, a new directory called `usbDerbyDB/` is created in the directory wherever the server was run.  This directory is the embedded Apache Derby database that is used to manage and memorize different audio wiring setups.  On future runs, the server will re-use these local database files if server is run from the same directory.  

The directory contents of the embedded database should look like this:

```shell
sethmachine UniversalSoundBoard % ls usbDerbyDB 
README_DO_NOT_TOUCH_FILES.txt   dbex.lck                        seg0                            tmp
db.lck                          log                             service.properties
```

You will never have to modify any files in that directory.  They managed by the server's Apache Derby database connector.

To change where the database exists on your machine see: [How do I change the database location](#how-do-i-change-the-location-of-the-embedded-database).  

### Sinks and Sources

[comment]: <> (Maybe put an image of a sink and source?)

We need to specify which audio devices should be wired together in order to create a soundboard experience.  In most cases this means wiring up a physical microphone (built-in microphone, headset, etc.) to a virtual audio device.  Of course, as long as the audio devices are compatible, any input device can be wired to any output device.

There are 2 kinds of audio devices to distinguish.  For clarity, we will refer to these as **sinks** and **sources**.  The notion of sink and source is heavily influenced by [PulseAudio](https://www.freedesktop.org/wiki/Software/PulseAudio/).  

* Sink: an audio device that collects audio data and can be read from.  One example is a microphone.  
* Source: an audio device that plays back audio and can be written to.  One example is speakers.

Most audio devices will either be a sink or a source but some can act as both (e.g. Blackhole).  In general the flow will be to select a sink, select a source, wire these together, and then start the sink in order to pipe it to the source.

### Setting up a sink

The steps here allow us to set up a sink (audio input), so that USBD can begin capturing your microphone input and route it to a virtual audio device.  This is necessary so that we can simultaneously send microphone input and audio files in a single audio input stream to Discord, Zoom, etc.  

The server provides an API to [list all installed audio devices](#list-installed-audio-devices), from which we can choose a sink and source.  In this next step, we will list out all sinks, and then choose a physical microphone as a sink.  Make sure the server is running while issuing these commands.  

List all the sinks and view in prettified JSON:

```shell
curl -X GET "localhost:8080/audio-mixer-metadata/descriptions?audioMixerType=SINK" | python -m json.tool
```

Note if you do not have Python installed, simply execute `curl -X GET "localhost:8080/audio-mixer-metadata/descriptions?audioMixerType=SINK"`.  This is simply dropping `| python -m json.tool` which you can do for all examples going forward.  

You should get a JSON output with a list of all installed audio devices on your computer that can act as sinks.  The actual output will be vary based on your computer's installed audio devices.

```json
{
  "audioMixerDescriptions": [
    {
      "name": "Headset (Bose QC35 II Hands-Fre",
      "vendor": "Unknown Vendor",
      "description": "Direct Audio Device: DirectSound Capture",
      "version": "Unknown Version",
      "supportedAudioMixerTypes": [
        "SINK"
      ]
    },
    {
      "name": "Port DELL U2718Q (NVIDIA High Defini",
      "vendor": "Unknown Vendor",
      "description": "Port Mixer",
      "version": "10.0",
      "supportedAudioMixerTypes": [
        "SINK"
      ]
    },
    {
      "name": "Port Headphones (Bose QC35 II Stereo",
      "vendor": "Unknown Vendor",
      "description": "Port Mixer",
      "version": "0.0",
      "supportedAudioMixerTypes": [
        "SINK"
      ]
    }
  ]
}
```

The output is quite verbose.  What we care about is the value of `"name"` field for the sink we wish to set up.  In this case I'm using the microphone on my Bose Headset which is called `"Headset (Bose QC35 II Hands-Fre"`.  Be sure to copy the name somewhere as we will need to reference it exactly in the next step (don't worry that the name of microphone appears truncated).  

Now we need to tell Java how incoming audio to the microphone should be sampled.  The server provides an [API to list all supported audio formats for an audio device](#list-all-supported-audio-formats-for-a-single-device) which we can query to pick a supported format.

This command will list all supported formats for the Bose Headset microphone in prettified JSON:

```shell
curl -G "localhost:8080/audio-mixer-metadata/audio-formats" ^
--data-urlencode "audioMixerName=Headset (Bose QC35 II Hands-Fre" ^
--data-urlencode "audioMixerType=SINK" | python -m json.tool
```

Here is the output (truncated due to its verbosity, as it is every audio format supported by this audio device):

```json
{
  "audioMixerDescriptionAndFormats": [
    {
      "audioMixerDescription": {
        "name": "Headset (Bose QC35 II Hands-Fre",
        "vendor": "Unknown Vendor",
        "description": "Direct Audio Device: DirectSound Capture",
        "version": "Unknown Version",
        "supportedAudioMixerTypes": [
          "SINK"
        ]
      },
      "audioFormat": {
        "encoding": {
          "name": "PCM_UNSIGNED"
        },
        "sampleRate": -1.0,
        "sampleSizeInBits": 8,
        "channels": 1,
        "frameSize": 1,
        "frameRate": -1.0,
        "bigEndian": false
      },
      "audioMixerTypeForFormat": "SINK",
      "dataLineName": "interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
    },
    {
      "audioMixerDescription": {
        "name": "Headset (Bose QC35 II Hands-Fre",
        "vendor": "Unknown Vendor",
        "description": "Direct Audio Device: DirectSound Capture",
        "version": "Unknown Version",
        "supportedAudioMixerTypes": [
          "SINK"
        ]
      },
      "audioFormat": {
        "encoding": {
          "name": "PCM_SIGNED"
        },
        "sampleRate": -1.0,
        "sampleSizeInBits": 8,
        "channels": 1,
        "frameSize": 1,
        "frameRate": -1.0,
        "bigEndian": false
      },
      "audioMixerTypeForFormat": "SINK",
      "dataLineName": "interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
    }
  ]
}
```
Both of these formats have what appears to be a nonsensical `-1.0` as the sample rate and the frame rate.  This value is [AudioSystem.NOT_SPECIFIED](https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/AudioSystem.html#NOT_SPECIFIED), which means to substitute it with any sensible value.  The server defaults to `48000` for any unspecified sample or frame rates, so the choice between formats will not matter much.    

Finally, we will persist the sink to USBD's database in order to wire it up later Virtual Audio Cable.  We will need to copy the entire JSON of one of the audio formats (any element of the JSON response).  Since each element is quite verbose, we can save it to a JSON file and then tell curl to use that file as the JSON body to the server.  I'll save the first audio format to `bose-audio-format.json` which should look like:

```json
    {
      "audioMixerDescription": {
        "name": "Headset (Bose QC35 II Hands-Fre",
        "vendor": "Unknown Vendor",
        "description": "Direct Audio Device: DirectSound Capture",
        "version": "Unknown Version",
        "supportedAudioMixerTypes": [
          "SINK"
        ]
      },
      "audioFormat": {
        "encoding": {
          "name": "PCM_UNSIGNED"
        },
        "sampleRate": -1.0,
        "sampleSizeInBits": 8,
        "channels": 1,
        "frameSize": 1,
        "frameRate": -1.0,
        "bigEndian": false
      },
      "audioMixerTypeForFormat": "SINK",
      "dataLineName": "interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
    }
```

Now persist the sink:

```shell
curl -X POST -H "Content-Type: application/json" "localhost:8080/audio-mixers" -d @bose-audio-format.json  
```

Which should return a response from the server:

```json
{"audioMixerId":1}
```

This audio mixer ID will be needed to reference the sink we've just setup (which is the Bose Headset in this case with the particular audio format we chose).  It is possible you could get a different ID back and that's OK.    

We can double-check the sink has been persisted through another request to the server.  The output of this command is all the audio devices persisted to the server, not those installed on your computer.  

```shell
curl -X GET "localhost:8080/audio-mixers" | python -m json.tool
```

Which should return:

```json
{
    "audioMixers": [
        {
            "audioMixerId": 1,
            "audioMixerDescription": {
                "name": "Headset (Bose QC35 II Hands-Fre",
                "vendor": "Unknown Vendor",
                "description": "Direct Audio Device: DirectSound Capture",
                "version": "Unknown Version",
                "supportedAudioMixerTypes": [
                    "SINK"
                ]
            },
            "audioFormat": {
                "encoding": {
                    "name": "PCM_UNSIGNED"
                },
                "sampleRate": -1.0,
                "sampleSizeInBits": 8,
                "channels": 1,
                "frameSize": 1,
                "frameRate": -1.0,
                "bigEndian": false
            },
            "audioMixerTypeForFormat": "SINK",
            "dataLineName": "interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
        }
    ]
}
```

### Setting up a source

The steps here allow us to set up a source (audio output), so that USBD has a place to send captured microphone input to.  This is necessary so that we can simultaneously send microphone input and audio files in a single audio input stream to Discord, Zoom, etc.

We will repeat [the same steps as we did to set up a sink](#setting-up-a-sink), except this time it will be for the Virtual Audio Cable as a source (this is where the physical microphone will send its input).  


List all the sources and view in prettified JSON:

```shell
curl -X GET "localhost:8080/audio-mixer-metadata/descriptions?audioMixerType=SOURCE" | python -m json.tool
```

The JSON output should list all audio devices installed on your PC that support the source interface:

```json
{
    "audioMixerDescriptions": [
        {
            "name": "DELL U2718Q (NVIDIA High Definition Audio)",
            "vendor": "Unknown Vendor",
            "description": "Direct Audio Device: DirectSound Playback",
            "version": "Unknown Version",
            "supportedAudioMixerTypes": [
                "SOURCE"
            ]
        },
        {
            "name": "Headphones (Bose QC35 II Stereo)",
            "vendor": "Unknown Vendor",
            "description": "Direct Audio Device: DirectSound Playback",
            "version": "Unknown Version",
            "supportedAudioMixerTypes": [
                "SOURCE"
            ]
        },
        {
            "name": "Speakers (Steam Streaming Speakers)",
            "vendor": "Unknown Vendor",
            "description": "Direct Audio Device: DirectSound Playback",
            "version": "Unknown Version",
            "supportedAudioMixerTypes": [
                "SOURCE"
            ]
        },
        {
            "name": "VoiceMeeter Aux Input (VB-Audio Virtual Cable)",
            "vendor": "Unknown Vendor",
            "description": "Direct Audio Device: DirectSound Playback",
            "version": "Unknown Version",
            "supportedAudioMixerTypes": [
                "SOURCE"
            ]
        }
    ]
}
```

`"VoiceMeeter Aux Input (VB-Audio Virtual Cable)"` is the audio device we need to set up as a source to receive the input of the microphone we set up as a sink in the previous steps.  You can also see other sources listed like `"DELL U2718Q (NVIDIA High Definition Audio)"` which correspond to the speakers on my monitor.  We could wire that to the microphone but the net effect would just be hearing our voice through our speakers.  Virtual Audio Cable is special in that it will automatically route its input to its output device.  


We'll now list all the supported audio formats for the Virtual Audio Cable in prettified JSON.  It's not necessary for the format of the sink and source to match, as Java will automatically handle conversion for us.  

```shell
curl -G "localhost:8080/audio-mixer-metadata/audio-formats" ^
--data-urlencode "audioMixerName=VoiceMeeter Aux Input (VB-Audio Virtual Cable)" ^
--data-urlencode "audioMixerType=SOURCE" | python -m json.tool
```

Here is the truncated output showing the first 2 audio formats supported:

```json
{
  "audioMixerDescriptionAndFormats": [
    {
      "audioMixerDescription": {
        "name": "VoiceMeeter Aux Input (VB-Audio Virtual Cable)",
        "vendor": "Unknown Vendor",
        "description": "Direct Audio Device: DirectSound Playback",
        "version": "Unknown Version",
        "supportedAudioMixerTypes": [
          "SOURCE"
        ]
      },
      "audioFormat": {
        "encoding": {
          "name": "PCM_UNSIGNED"
        },
        "sampleRate": -1.0,
        "sampleSizeInBits": 8,
        "channels": 1,
        "frameSize": 1,
        "frameRate": -1.0,
        "bigEndian": false
      },
      "audioMixerTypeForFormat": "SOURCE",
      "dataLineName": "interface SourceDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
    },
    {
      "audioMixerDescription": {
        "name": "VoiceMeeter Aux Input (VB-Audio Virtual Cable)",
        "vendor": "Unknown Vendor",
        "description": "Direct Audio Device: DirectSound Playback",
        "version": "Unknown Version",
        "supportedAudioMixerTypes": [
          "SOURCE"
        ]
      },
      "audioFormat": {
        "encoding": {
          "name": "PCM_SIGNED"
        },
        "sampleRate": -1.0,
        "sampleSizeInBits": 8,
        "channels": 1,
        "frameSize": 1,
        "frameRate": -1.0,
        "bigEndian": false
      },
      "audioMixerTypeForFormat": "SOURCE",
      "dataLineName": "interface SourceDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
    }
  ]
}
```

As in the previous step, we'll save the first format to a JSON file called `vb-audio-cable-input-format.json`.  Note we will only want to use formats that have `"interface SourceDataLine"` in the `"dataLineName"` field.  The contents of the JSON file should look similar to this:


```json
{
  "audioMixerDescription": {
    "name": "VoiceMeeter Aux Input (VB-Audio Virtual Cable)",
    "vendor": "Unknown Vendor",
    "description": "Direct Audio Device: DirectSound Playback",
    "version": "Unknown Version",
    "supportedAudioMixerTypes": [
      "SOURCE"
    ]
  },
  "audioFormat": {
    "encoding": {
      "name": "PCM_UNSIGNED"
    },
    "sampleRate": -1.0,
    "sampleSizeInBits": 8,
    "channels": 1,
    "frameSize": 1,
    "frameRate": -1.0,
    "bigEndian": false
  },
  "audioMixerTypeForFormat": "SOURCE",
  "dataLineName": "interface SourceDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
}
```

With a full description of how we are using the Virtual Audio Cable as a source, we can save it to the USBD database so that it can be wired to our sink:

```shell
curl -X POST -H "Content-Type: application/json" "localhost:8080/audio-mixers" -d @vb-audio-cable-input-format.json  
```

Which should return a response from the server:

```json
{"audioMixerId":101}
```

This audio mixer ID will be needed to continue reference the source we've just setup.  Again it is possible you could get a different ID assigned to the source.  

We can double-check the source has been persisted through another request to the server which lists all installed audio devices.  Note that installed devices here refers to audio devices setup with the server (separate from the actual audio devices installed on your computer).

```shell
curl -X GET "localhost:8080/audio-mixers" | python -m json.tool
```

Which should now return both the sink and source:

```json
{
    "audioMixers": [
        {
            "audioMixerId": 1,
            "audioMixerDescription": {
                "name": "Headset (Bose QC35 II Hands-Fre",
                "vendor": "Unknown Vendor",
                "description": "Direct Audio Device: DirectSound Capture",
                "version": "Unknown Version",
                "supportedAudioMixerTypes": [
                    "SINK"
                ]
            },
            "audioFormat": {
                "encoding": {
                    "name": "PCM_UNSIGNED"
                },
                "sampleRate": -1.0,
                "sampleSizeInBits": 8,
                "channels": 1,
                "frameSize": 1,
                "frameRate": -1.0,
                "bigEndian": false
            },
            "audioMixerTypeForFormat": "SINK",
            "dataLineName": "interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
        },
        {
            "audioMixerId": 101,
            "audioMixerDescription": {
                "name": "VoiceMeeter Aux Input (VB-Audio Virtual Cable)",
                "vendor": "Unknown Vendor",
                "description": "Direct Audio Device: DirectSound Playback",
                "version": "Unknown Version",
                "supportedAudioMixerTypes": [
                    "SOURCE"
                ]
            },
            "audioFormat": {
                "encoding": {
                    "name": "PCM_UNSIGNED"
                },
                "sampleRate": -1.0,
                "sampleSizeInBits": 8,
                "channels": 1,
                "frameSize": 1,
                "frameRate": -1.0,
                "bigEndian": false
            },
            "audioMixerTypeForFormat": "SOURCE",
            "dataLineName": "interface SourceDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
        }
    ]
}
```

### Wiring the sink to the source

With both the sink and source setup, we can now wire them together.  This simply tells the sink to send all of its input to the source.  If the sink was a physical microphone and the source was physical speakers, you would hear your own voice playback through your speakers as you spoke through the microphone.  With Virtual Audio Cable, only the receivers of our voice chat will hear our microphone.

To wire the sink to the source, we need the audio mixer IDs of both the sink and source, which we noted in the previous steps.  Note that it's possible that the IDs for yours may be different than this guide.  If you've forgotten the IDs, use `curl -X GET "localhost:8080/audio-mixers"` to list all the sinks and sources you've set up.  

Make this request to wire them together, where our sink (Bose Headphones) has ID `1` and our source (Virtual Audio Cable Input) has ID `101`: 

```shell
curl -X POST "localhost:8080/audio-mixer-wiring" -H "Content-Type: application/json" ^
--data "{\"sinkId\":1, \"sourceId\":101}"
```

This will return no JSON data but the HTTP response code should be a 204.  We can confirm the wiring exists now:

```shell
curl -X GET "localhost:8080/audio-mixer-wiring" | python -m json.tool
```

The JSON response should look like this:

```json
{
    "wirings": [
        {
            "sinkId": 1,
            "sinkName": "Headset (Bose QC35 II Hands-Fre",
            "sourceId": 101,
            "sourceName": "VoiceMeeter Aux Input (VB-Audio Virtual Cable)"
        }
    ]
}
```

The JSON here means the sink with ID `1` will send its input to the source with ID `101`.  

### Setting Virtual Audio Cable Output as microphone input

With our physical microphone now wired to send its input to Virtual Audio Cable Input, we now need to tell our voice chat application to use Virtual Audio Cable as the microphone input.  The steps shown here work for Discord but can be done just as easily for Zoom or other applications.  

1. Open Discord desktop application
2. Open User Settings (gear icon) in lower left
3. Under App Settings select Voice & Video
4. Under INPUT DEVICE select `VoiceMeeter Aux Output (VB Audio Virtual Cable)`

Your audio settings should look similar to this:

![Discord VB Audio Cable Input](examples/discord-vb-audio-cable-as-input.png)

### Send and verify microphone input to Discord

Let's now begin sending microphone input to Discord, and verify the audio wiring is working.  
  
We need to tell the server to start capturing audio input from our sink so that it begins to route audio to the source (Virtual Audio Cable) and ultimately Discord (or whatever voice chat application you're using).  

Referencing the sink we set up for the Bose Headset whose ID is `1`, this command will start capturing all input from it:

```shell
curl -X POST -H "Content-Type: application/json" localhost:8080/sinks/start --data "{\"sinkId\": 1}"
```

We'll get back an empty response (200 response code), but we should see this in the server log:

```shell
127.0.0.1 - - [22/Jan/2022:22:44:04 +0000] "POST /sinks/start HTTP/1.1" 204 0 "-" "curl/7.79.1" 114
WARN  [2022-01-22 22:44:04,143] io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil: The sample rate is not specified for this audio format.  Defaultin
g to 48000.0
WARN  [2022-01-22 22:44:04,143] io.sethmachine.universalsoundboard.core.util.audiomixer.AudioFormatUtil: The frame rate is not specified for this audio format.  Defaulting
 to 48000.0
```

Now when you start speaking, you should see the voice activity bar in Discord light up green.  You can also use the Discord MIC Test and you should hear back your own voice.  Just to be clear, here's an example of what this should look like: https://www.youtube.com/watch?v=h75MK3NBR-E

The sink will run forever until the server is shutdown or your machine turns off.  The sink can also be stopped anytime with this request:

```shell
curl -X POST -H "Content-Type: application/json" localhost:8080/sinks/stop --data "{\"sinkId\": 1}"
```

You should see output in the server log like this:

```shell
INFO  [2022-01-22 22:47:11,461] io.sethmachine.universalsoundboard.core.concurrent.sink.SinkAudioMixerRunnable: Received a request to stop this sink: SinkAudioMixer{audioM
ixerId=1, audioMixerDescription=AudioMixerDescription{name=Headset (Bose QC35 II Hands-Fre, vendor=Unknown Vendor, description=Direct Audio Device: DirectSound Capture, ve
rsion=Unknown Version, supportedAudioMixerTypes=[SINK]}, audioFormat=PCM_UNSIGNED unknown sample rate, 8 bit, mono, 1 bytes/frame, , mixer=com.sun.media.sound.DirectAudioD
evice@1d19d197, dataLineName=interface TargetDataLine supporting 8 audio formats, and buffers of at least 32 bytes}
127.0.0.1 - - [22/Jan/2022:22:47:11 +0000] "POST /sinks/stop HTTP/1.1" 204 0 "-" "curl/7.79.1" 23
```

And Discord should no longer detect any voice activity.  To resume, run the start sink command again.  

### Play an audio file through the microphone

Now that we've confirmed our microphone is routing audio to our voice chat application (Discord in this example), we can begin playing audio files through the microphone as well.  The net effect is that any users on the other end will hear the audio file as if it's coming directly from your microphone.  

We can test this with an audio file.  Here is a sample called [evil-laugh.wav](examples/evil-laugh.wav).  Audio file is taken from: https://freesound.org/people/ZyryTSounds/sounds/219110/

The following command will play `evil-laugh.wav` as if it came through your microphone.  Note you'll need to reference the ID of the source we set up in previous steps, which is 101 in this example but could be a different value on your machine.  

```shell
curl -X POST -H "Content-Type:multipart/form-data" localhost:8080/sources/play -F "audioFile=@evil-laugh.wav" -F "sourceCommandRequest={\"sourceId\":101};type=application/json"
```

Note that we are playing the audio file to the source and not the microphone, but the net effect is that the audio file is played as if it came through the microphone.  

To confirm this you could have another account in a Discord voice chat channel and listen for the sound.  You could also perform [the microphone test we did before](#send-and-verify-microphone-input-to-discord).  

If you aren't hearing anything, there are 2 Discord specific settings we can tune:

#### Disable Discord Advanced Voice Activity

This setting on Discord attempts to only send actual human voice and ignore all other sound.  We can't control what Discord thinks is human voice, so disabling this will make sure our audio file gets through to the voice chat.  

Head to Settings (gear icon) > App Settings > Voice & Video > Advanced.  Then toggle off `Advanced Voice Activity`, as shown below:

![Discord Disable Advanced Voice Activity Detection](examples/discord-disable-advanced-voice-activity.png)

#### Lower Discord Voice Activity Sensitivity

Another option to tune is Discord's sensitivity to voice activity.  This is the threshold where Discord thinks there is input coming to the microphone (as opposed to noise like keyboard strokes, etc.).  Setting this to a lower value (moving the slider to the left) should increase the likelihood that audio clips play through the microphone.  

![Discord Lower Voice Activity Sensitivity](examples/discord-lower-voice-activity-sensitivity.png)

### Playing a sound simultaneously to microphone and speakers

We're now able to use our microphone normally while also being able to play audio files as if they came through the microphone.  However, on our end we won't hear the sound we are playing, only those on the other end of the voice chat application.  The USBD server provides an API to do just that, so both you and your audience will hear the same sound effect being played:

```shell
curl -X POST -H "Content-Type:multipart/form-data" localhost:8080/sound-board/play -F "audioFile=@evil-laugh.wav" -F "playAudioFileToSourceAndSinkRequest={\"sinkId\": YOUR_SINK_ID, \"sourceId\":YOUR_AUDIO_OUTPUT_DEVICE_ID};type=application/json" 
```

* `YOUR_SINK_ID` corresponds to the sink we set up in previous steps (in our case it is ID `1`)
* `YOUR_AUDIO_OUTPUT_DEVICE_ID` would be the ID of a source that corresponds to an audio output device on our computer (e.g. physical speakers).

We can follow [the steps to set up a source](#setting-up-a-source) except this time for an audio output device of our choice, e.g. physical speakers, our headset's output, etc.  

Since I'm already using my headset as a microphone, I'll use it as my speakers.  Here is what I'll be sending to the server as `bose-output-hands-free.json`

```json
{
  "audioMixerDescription": {
    "name": "Headset (Bose QC35 II Hands-Free AG Audio)",
    "vendor": "Unknown Vendor",
    "description": "Direct Audio Device: DirectSound Playback",
    "version": "Unknown Version",
    "supportedAudioMixerTypes": [
      "SOURCE"
    ]
  },
  "audioFormat": {
    "encoding": {
      "name": "PCM_SIGNED"
    },
    "sampleRate": -1.0,
    "sampleSizeInBits": 16,
    "channels": 1,
    "frameSize": 2,
    "frameRate": -1.0,
    "bigEndian": true
  },
  "audioMixerTypeForFormat": "SOURCE",
  "dataLineName": "interface SourceDataLine supporting 8 audio formats, and buffers of at least 32 bytes"
}
```

The actual HTTP request:

```shell
curl -X POST -H "Content-Type: application/json" "localhost:8080/audio-mixers" -d @bose-output-hands-free.json
```

Which returns:

```json
{"audioMixerId":502}
```

Now we can simultaneously play audio through the microphone and our speakers:

```shell
curl -X POST -H "Content-Type:multipart/form-data" localhost:8080/sound-board/play -F "audioFile=@evil-laugh.wav" -F "playAudioFileToSourceAndSinkRequest={\"sinkId\": 1, \"sourceId\":502};type=application/json"
```

* `1` is the ID our sink (the Bose headset microphone)
* `502` is the ID of the source we just set up (the Bose headset output)

## API

This section documents the HTTP based API of the server.  In all examples `curl` is used to perform the HTTP request and Python is used to print out the JSON response in a prettified format.  

### List installed audio devices

Lists all installed audio devices, including any virtual audio devices.  The metadata of each audio device such as name, vendor, description, and version are included.  Use the exact name string to lookup supported audio formats.    

#### Request
```shell
curl -X GET localhost:8080/audio-mixer-metadata/descriptions
```

#### Parameters

* **audioMixerType**: optional string parameter that filters down installed audio devices to those that support `SINK` or `SOURCE` interfaces.  Possible values are `SINK` and `SOURCE`.  Omitting this parameter will list all installed audio devices with their metadata.

#### Examples

List all audio devices regardless of interface:

```shell
curl -X GET localhost:8080/audio-mixer-metadata/descriptions
```

List all audio devices that can act as a sink:

```shell
curl -X GET "localhost:8080/audio-mixer-metadata/descriptions?audioMixerType=SINK"
```

List all audio devices that can act as a source:

```shell
curl -X GET "localhost:8080/audio-mixer-metadata/descriptions?audioMixerType=SOURCE"
```

Note that audio devices that support both interfaces will appear in all listings.  

### List all supported audio formats for a single device 

Lists all supported audio formats for a single device.  Each element in the list output is used as input to install the audio device to USBD so it can be wired to another audio device.  

#### Request
```shell
curl -X GET localhost:8080/audio-mixer-metadata/audio-formats
```

#### Parameters

* **audioMixerName**: required string parameter that specifies the name of the installed audio device.  Use [audio-mixer-metadata/descriptions](#list-installed-audio-devices) to find the exact name
* **audioMixerType**: optional string parameter that restricts the listed audio formats only to the supported audio mixer type.  Possible values are `"SINK"` or `"SOURCE"`.  If unspecified, all formats for the audio device will be listed.

#### Examples

List all audio formats for a built-in Macbook microphone:

```shell
curl -G --data-urlencode "audioMixerName=MacBook Pro Microphone" \
"localhost:8080/audio-mixer-metadata/audio-formats"
```

List all audio formats for a built-in Macbook microphone that support the sink interface:

```shell
curl -G --data-urlencode "audioMixerName=MacBook Pro Microphone" \
--data-urlencode "audioMixerType=SINK" \
"localhost:8080/audio-mixer-metadata/audio-formats"
```

List all audio formats for Virtual Audio Cable that support the source interface:

```shell
curl -G --data-urlencode "audioMixerName=MacBook Pro Microphone" \
--data-urlencode "audioMixerType=SINK" \
"localhost:8080/audio-mixer-metadata/audio-formats"
```

Note that audio devices that support both interfaces will appear in all listings.

## FAQ

### What makes this "universal"?

UniversalSoundBoard (USBD) is named such as it is able to run anywhere due to it being a Java application.  In addition, it has no knowledge or awareness of actual voice chat apps like Zoom or Discord, as USBD is not a plugin.  One simply needs to hook up the right audio devices and then USBD can be used anywhere. Hence it is a universal sound board, as it is both app and operating system agnostic.  

### Why do I have to download a 3rd party virtual audio device?

Unfortunately writing virtual audio devices for macOS and Windows is still a highly specialized area and would be its own large undertaking.  Thankfully [several open source or free virtual audio devices](#virtual-audio-devices) exist which solve this problem already. 

### Why is there no graphical user interface?

This project specifically focuses on the programming the sound rather than being an all-in-one application.  Anyone is welcome to build a GUI on top of the USBD API.  In fact, one of the goals of this project is to lower the barrier for entry for programming sound so developers can focus on the app rather than the idiosyncrasies of programming audio.   

### How do I change the location of the embedded database?

When running the server, you can pass additional command line parameters to change any of the properties in the server YAML file. This includes the location where the embedded database exists. If you already have a database created, you should move it to that location first, otherwise you will need to setup the audio wiring again. The following command illustrates how to change the database to `/Users/sethmachine/usbDerbyDB`. Note you may need to quote the entire JVM argument as done below.  

```shell
java "-Ddw.database.url=jdbc:derby:/Users/sethmachine/usbDerbyDB;create=true" \
-jar target/UniversalSoundBoard-1.0-SNAPSHOT.jar server universal-sound-board.yml
```