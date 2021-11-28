package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerId;
import io.sethmachine.universalsoundboard.db.audiomixer.AudioMixerInsert;
import io.sethmachine.universalsoundboard.db.audiomixer.AudioMixerRow;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.service.AudioMixerMetadataService;
import java.util.Optional;
import javax.inject.Inject;
import javax.sound.sampled.AudioFormat;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/audio-mixers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AudioMixersResource {

  private final AudioMixerDAO audioMixerDAO;
  private final SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory;

  @Inject
  public AudioMixersResource(AudioMixerDAO audioMixerDAO,
                             SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory) {
    this.audioMixerDAO = audioMixerDAO;
    this.sinkAudioMixerRunnableFactory = sinkAudioMixerRunnableFactory;
  }

  @POST
  public AudioMixerId createAudioMixer(AudioMixerInsert audioMixerInsert) {
    return AudioMixerId.builder().setId(audioMixerDAO.insert(audioMixerInsert)).build();
  }

  @GET
  @Path("/{audioMixerId}")
  public Optional<AudioMixerRow> getAudioMixer(
    @PathParam("audioMixerId") int audioMixerId
  ) {
    return audioMixerDAO.get(audioMixerId);
  }

  @GET
  @Path("/foo")
  public void foo(){
    SinkAudioMixerRunnable sinkAudioMixerRunnable = sinkAudioMixerRunnableFactory.create(5);
    int bar = 5 + 6;
  }
}
