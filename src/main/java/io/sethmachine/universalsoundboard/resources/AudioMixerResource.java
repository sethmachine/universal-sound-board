package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.service.AudioMixerMetadataService;
import javax.inject.Inject;
import javax.sound.sampled.AudioFormat;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/audio-mixer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AudioMixerResource {

  private final AudioMixerMetadataService audioMixerMetadataService;

  @Inject
  public AudioMixerResource(AudioMixerMetadataService audioMixerMetadataService) {
    this.audioMixerMetadataService = audioMixerMetadataService;
  }

  @POST
  @Path("")
  public AudioFormat createAudioMixer(AudioFormat audioFormat) {
    return audioFormat;
  }
  //  @GET
  //  @Path("/descriptions")
  //  public AudioMixerDescriptions getAudioMixerDescriptions(
  //      @QueryParam("audioMixerType") Optional<AudioMixerType> audioMixerType
  //  ) {
  //    return audioMixerMetadataService.getAudioMixerDescriptions(
  //        AudioMixerMetadataQuery.builder().setAudioMixerType(audioMixerType).build()
  //    );
  //  }
  //
  //  @GET
  //  @Path("/audio-formats")
  //  public Optional<AudioMixerFormatsResponse> getSupportedAudioMixerFormats(
  //      @QueryParam("audioMixerName") String audioMixerName
  //  ) {
  //    return audioMixerMetadataService.getSingleAudioMixerSupportedFormats(
  //        AudioMixerMetadataQuery.builder().setAudioMixerName(audioMixerName).build()
  //    );
  //  }
}
