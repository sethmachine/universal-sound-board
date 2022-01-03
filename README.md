# UniversalSoundBoard

UniversalSoundBoard (USB) is a Java application that allows wiring inputs of target audio devices (e.g. a physical microphone) to the outputs of source audio devices (e.g. a virtual audio device, physical speaker, etc.).  USB provides a high level abstraction of the [Java Sound API](https://docs.oracle.com/javase/tutorial/sound/) to simplify programming sound and building sound board applications.  USB is a standalone Dropwizard based Java application that is controlled via a RESTful web API.  Because USB written in Java and is app agnostic, it can truly run anywhere and provide a universal sound board experience.      

With the right setup, the USB API can be used to create a sound board application, where one can simultaneously speak through a physical microphone while playing audio files that can be heard in any voice chat app as if the sound came from your microphone (Discord, Zoom, etc.).  USB essentially solves the often misunderstood question: "how do I play audio through my microphone?"

USB is not a plugin to any app and will simply "work" as long as the right audio input device is set and USB is running.  This means you can write code that will work on any voice app rather than learn the detailed specifics of Zoom or Discord plugins, etc.  

### Stack

* Dropwizard
* Derby embedded database
* Jackson
* Guice
* Rosetta
* Liquibase

## Installation

UniversalSoundBoard is a standalone Java application.  All that is needed is the compiled fat JAR, which has been tested on both macOS and Windows operating systems.  That being said, to create an actual sound board experience (e.g. play audio through the microphone), you will need to install a 3rd party virtual audio device that automatically routes its output to input.

For now it is necessary need to build the JAR from source.  In the future I will provide stable builds that can be downloaded.  

### Requirements

* Java 11 runtime
* (Technically optional but highly recommended) Virtual audio device that routes output to input

### Virtual Audio Devices

The following virtual audio devices have been successfully tested with USB to play audio through the microphone.  You will need to install these separately in order to use them with USB.   

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

Exiting the command line or hitting CTRL+C will stop the application.  The server admin page can be visited at http://localhost:8081 where you can view the [healthchecks](http://localhost:8081/healthcheck?pretty=true)

Another side effect is that a new directory called `usbDerbyDB/` is created in the directory wherever the server was run.  This directory is the embedded Apache Derby database that is used to manage and memorize different audio wiring setups.  




How to start the UniversalSoundBoard application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/UniversalSoundBoard-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8080`



