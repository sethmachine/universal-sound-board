package io.sethmachine.universalsoundboard.resources;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Provides;

import io.sethmachine.universalsoundboard.db.audio.mixer.AudioMixerRow;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.db.daos.FoobarDAO;

@Path("/example")
@Produces(MediaType.TEXT_PLAIN)
public class ExampleResource {

  private final FoobarDAO foobarDAO;
  private final AudioMixerDAO audioMixerDAO;


  @Inject
  public ExampleResource(FoobarDAO foobarDAO, AudioMixerDAO audioMixerDAO) {
    this.foobarDAO = foobarDAO;
    this.audioMixerDAO = audioMixerDAO;
  }

  @GET
  public String sayHello() {
    return "Hello, World!";
  }

  @GET
  @Path("/insert-random")
  @Produces(MediaType.APPLICATION_JSON)
  public Optional<AudioMixerRow> sayFoobar(){
    audioMixerDAO.insert("foobar");
    return audioMixerDAO.findNameById(1);
  }

  @GET
  @Path("/audio-mixer/{audioMixerId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Optional<AudioMixerRow> getAudioMixer(@PathParam("audioMixerId") int audioMixerId){
    return audioMixerDAO.findNameById(audioMixerId);
  }

  @GET
  @Path("/audio-mixers")
  @Produces(MediaType.APPLICATION_JSON)
  public List<AudioMixerRow> getAudioMixer(){
    return audioMixerDAO.getAllAudioMixers();
  }
}
