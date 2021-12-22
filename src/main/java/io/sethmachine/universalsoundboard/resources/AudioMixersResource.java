package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerId;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerResponse;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixersList;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.CreateAudioMixerRequest;
import io.sethmachine.universalsoundboard.service.api.AudioMixersApiService;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

  private final AudioMixersApiService audioMixersApiService;

  @Inject
  public AudioMixersResource(AudioMixersApiService audioMixersApiService) {
    this.audioMixersApiService = audioMixersApiService;
  }

  @POST
  public AudioMixerId createAudioMixer(CreateAudioMixerRequest createAudioMixerRequest) {
    return audioMixersApiService.createAudioMixer(createAudioMixerRequest);
  }

  @GET
  @Path("/{audioMixerId}")
  public Optional<AudioMixerResponse> getAudioMixer(
    @PathParam("audioMixerId") int audioMixerId
  ) {
    return audioMixersApiService.getAudioMixer(audioMixerId);
  }

  @GET
  public AudioMixersList getAllAudioMixers() {
    return audioMixersApiService.getAllAudioMixers();
  }

  @DELETE
  @Path("/{audioMixerId}")
  public void deleteAudioMixer(@PathParam("audioMixerId") int audioMixerId) {
    audioMixersApiService.deleteAudioMixer(audioMixerId);
  }
}
