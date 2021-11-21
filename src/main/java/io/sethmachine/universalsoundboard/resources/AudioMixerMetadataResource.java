package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.AudioMixerDescriptionsResponse;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.AudioMixerFormatsResponse;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
import io.sethmachine.universalsoundboard.service.AudioMixerMetadataService;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
  public AudioMixerDescriptionsResponse getAudioMixerDescriptions(
    @QueryParam("audioMixerType") Optional<AudioMixerType> audioMixerType
  ) {
    return audioMixerMetadataService.getAudioMixerDescriptions(
      AudioMixerMetadataQuery.builder().setAudioMixerType(audioMixerType).build()
    );
  }

  @GET
  @Path("/audio-formats")
  public Optional<AudioMixerFormatsResponse> getSupportedAudioMixerFormats(
    @QueryParam("audioMixerName") String audioMixerName
  ) {
    return audioMixerMetadataService.getSingleAudioMixerSupportedFormats(
      AudioMixerMetadataQuery.builder().setAudioMixerName(audioMixerName).build()
    );
  }
}
