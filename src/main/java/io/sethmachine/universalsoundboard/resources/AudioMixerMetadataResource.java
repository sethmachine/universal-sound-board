package io.sethmachine.universalsoundboard.resources;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerDescriptions;
import io.sethmachine.universalsoundboard.core.model.audiomixers.AudioMixerType;
import io.sethmachine.universalsoundboard.service.AudioMixerMetadataService;

@Path("/audio-mixer-metadata")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AudioMixerMetadataResource {
  private final AudioMixerMetadataService audioMixerMetadataService;


  @Inject
  public AudioMixerMetadataResource(AudioMixerMetadataService audioMixerMetadataService) {
    this.audioMixerMetadataService = audioMixerMetadataService;
  }

  @GET
  @Path("/descriptions")
  public AudioMixerDescriptions getAudioMixerDescriptions(
      @QueryParam("audioMixerType") Optional<AudioMixerType> audioMixerType
  ) {
    return audioMixerMetadataService.getAudioMixerDescriptions(audioMixerType);
  }
}
