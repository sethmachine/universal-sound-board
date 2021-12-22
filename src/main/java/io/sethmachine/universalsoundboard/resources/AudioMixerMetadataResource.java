package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.AudioMixerDescriptionsResponse;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.metadata.SingleAudioMixerDescriptionAndFormatsResponse;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.AudioMixerType;
import io.sethmachine.universalsoundboard.core.model.audiomixers.metadata.query.AudioMixerMetadataQuery;
import io.sethmachine.universalsoundboard.service.api.AudioMixerMetadataApiService;
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

  private final AudioMixerMetadataApiService audioMixerMetadataApiService;

  @Inject
  public AudioMixerMetadataResource(
    AudioMixerMetadataApiService audioMixerMetadataApiService
  ) {
    this.audioMixerMetadataApiService = audioMixerMetadataApiService;
  }

  @GET
  @Path("/descriptions")
  public AudioMixerDescriptionsResponse getAudioMixerDescriptions(
    @QueryParam("audioMixerType") Optional<AudioMixerType> audioMixerType
  ) {
    return audioMixerMetadataApiService.getAudioMixerDescriptions(
      AudioMixerMetadataQuery.builder().setAudioMixerType(audioMixerType).build()
    );
  }

  @GET
  @Path("/audio-formats")
  public SingleAudioMixerDescriptionAndFormatsResponse getSupportedAudioMixerFormats(
    @QueryParam("audioMixerName") String audioMixerName,
    @QueryParam("audioMixerType") Optional<AudioMixerType> audioMixerType
  ) {
    if (audioMixerName == null) {
      throw new IllegalArgumentException(
        "An audio mixer name must be specified when requesting metadata about a specific audio mixer"
      );
    }
    AudioMixerMetadataQuery query = AudioMixerMetadataQuery
      .builder()
      .setAudioMixerName(audioMixerName)
      .setAudioMixerType(audioMixerType)
      .build();
    return audioMixerMetadataApiService.getSingleAudioMixerSupportedFormats(query);
  }
}
