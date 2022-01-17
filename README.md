# UniversalSoundBoard

UniversalSoundBoard (USB) is a Java application that allows wiring inputs of target audio devices (e.g. a physical microphone) to the outputs of source audio devices (e.g. a virtual audio device, physical speaker, etc.).  USB provides a high level abstraction of the [Java Sound API](https://docs.oracle.com/javase/tutorial/sound/) to simplify programming sound and building sound board applications.  USB is a standalone Dropwizard based Java application that is controlled via a RESTful web API.  Because USB written in Java and is app agnostic, it can run anywhere and provide a universal sound board experience.      

With the right setup, the USB API can be used to create a sound board application, where one can simultaneously speak through a physical microphone while playing audio files that can be heard in any voice chat app as if the sound came from your microphone (Discord, Zoom, etc.).  USB essentially solves the often asked question but rarely answered question: "how do I play audio through my microphone?"

USB is not a plugin to any app and will simply "work" as long as the right audio input device is set and USB is running.  This means you can write code that will work on any voice app rather than learn the detailed specifics of writing plugins for Zoom, Discord, etc.    

### Stack

* Dropwizard
* Derby embedded database
* Jackson
* Guice
* dropwizard-guicey
* Rosetta
* Liquibase

## Installation

UniversalSoundBoard is a standalone Java application.  All that is needed is the compiled fat JAR, which has been tested on both macOS and Windows operating systems.  That being said, to create an actual sound board experience (e.g. play audio through the microphone), you will still need to install a 3rd party virtual audio device that automatically routes its output to input.

For now it is necessary need to build the JAR from source.  In the future I will provide stable builds that can be downloaded.  

### Requirements

* Java 11 runtime
* (Technically optional but highly recommended) Virtual audio device that routes output to input

### Virtual Audio Devices

The following virtual audio devices have been successfully tested with USB to play audio through the microphone.  You will need to install these separately in order to use them with USB.   You only need one them, e.g. macOS has both Blackhole and Virtual Audio Cable, but you only need to install one of these.  

#### Windows
* [Virtual Audio Cable](https://vb-audio.com/Cable)
#### macOS
* [Virtual Audio Cable](https://vb-audio.com/Cable)
* [BlackHole](https://github.com/ExistentialAudio/BlackHole)

### Building

This has been tested with [Apache Maven](https://maven.apache.org/) 3.8.1 and an [OpenJDK 11](https://openjdk.java.net/projects/jdk/11/) runtime on macOS Catalina.

* Clone the repository: `git clone https://github.com/sethmachine/universal-sound-board`
* Open a command line and enter the cloned repo, e.g. `cd universal-sound-board/`
* Build using Maven: `mvn clean verify`
* Verify the fat JAR exists under `universal-sound-board/target/UniversalSoundBoard-1.0-SNAPSHOT.jar`

## Usage

UniversalSoundBoard is started like any Dropwizard application from the commandline.  USB runs as a web server in the background as it routes audio from one device to another.

### Requirements

* Java 11 runtime
* Access to terminal or command line program
* An HTTP client to interact with USB, e.g. [curl](https://curl.se/)

### Running the server

This assumes you've downloaded or have successfully built a stable build of USB and have a JAR.  These examples will all use curl to interact with the local running server to program the audio wiring.  

* [Build](#building) or locate the fat JAR, e.g. `target/UniversalSoundBoard-1.0-SNAPSHOT.jar`
* Locate the [Dropwizard configuration YAML](https://github.com/sethmachine/universal-sound-board/blob/master/universal-sound-board.yml).  This tells Dropwizard how to run the server.  
* Run the server on command line, e.g. `java -jar target/UniversalSoundBoard-1.0-SNAPSHOT.jar server universal-sound-board.yml`

Exiting the command line or hitting CTRL+C will stop the application.  The server admin page can be visited at http://localhost:8081 where you can view the [healthchecks](http://localhost:8081/healthcheck?pretty=true).  The healthchecks can be ignored as they are not yet configured properly.  

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
sethmachine UniversalSoundBoard % 
```

You should never have to modify any files in that directory.  

### Sinks and Sources

We need to specify which audio devices should be wired together in order to create a soundboard experience.  In most cases this means wiring up a physical microphone (built-in microphone, headset, etc.) to a virtual audio device.  Of course, as long as the audio devices are compatible, any input device can be wired to any output device.

There are 2 kinds of audio devices to distinguish.  For clarity, we will refer to these as **sinks** and **sources**.  These notions of sink and source is heavily influenced by [PulseAudio](https://www.freedesktop.org/wiki/Software/PulseAudio/).  

* Sink: an audio device that collects audio data and can be read from.  One example is a microphone.  
* Source: an audio device that plays back audio and can be written to.  One example is speakers.

Most audio devices will either be a sink or a source but some can act as both (e.g. Blackhole).  In general the flow will be to select a sink, select a source, wire these together, and then start the sink in order to pipe it to the source.

### Choosing audio devices

The server provides an API to list all installed audio devices, from which we can choose a sink and source.  Use `GET /`  

 

## API

This section documents the HTTP based API of the server.  In all examples `curl` is used to perform the HTTP request and Python is used to print out the JSON response in a prettified format.  

### List installed audio devices

Lists all installed audio devices, including any virtual audio devices.  The metadata of each audio device such as name, vendor, description, and version are included.  Use the exact name string to lookup supported audio formats.    

#### Request
```shell
curl -X GET localhost:8080/audio-mixer-metadata/descriptions
```

#### Parameters






## FAQ

### What makes this "universal"?

UniversalSoundBoard (USB) is named such as it is able to run anywhere due to it being a Java application.  In addition, it has no knowledge or awareness of actual voice chat apps like Zoom or Discord, as USB is not a plugin.  One simply needs to hook up the right audio devices and then USB can be used anywhere.   Hence it is a universal sound board, as it both app and operating system agnostic.  

### Why do I have to download a 3rd party virtual audio device?

Unfortunately writing virtual audio devices for macOS and Windows is still a highly specialized area and would be its own large undertaking.  Thankfully [several open source or free virtual audio devices](#virtual-audio-devices) exist which solve this problem.  However, it would be amazing if USB was 100% standalone and came with its own virtual audio device or even API to create it.  This may be a future major feature to watch out for.  

### Why is there no graphical user interface?

This project specifically focuses on the programming the sound rather than being an all-in-one application.  Anyone is welcome to build a GUI on top of the USB API.  In fact, one of the goals of this project is to lower the barrier for entry for programming sound so developers can focus on the app rather than the idiosyncrasies of programming audio.   

### How do I change the location of the embedded database?

When running the server, you can pass additional command line parameters to change any of the properties in the server YAML file.  This includes the location where the embedded database exists.  If you already have a database created, you should move it to that location first, otherwise you will need to setup the audio wiring again.  The following command illustrates how to change the database to `/Users/sethmachine/usbDerbyDB`.  Note you may need to quote the entire JVM argument as done below.  

```shell
java "-Ddw.database.url=jdbc:derby:/Users/sethmachine/usbDerbyDB;create=true" \
-jar target/UniversalSoundBoard-1.0-SNAPSHOT.jar server universal-sound-board.yml
```