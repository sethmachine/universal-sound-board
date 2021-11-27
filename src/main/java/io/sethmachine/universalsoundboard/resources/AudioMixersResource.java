package io.sethmachine.universalsoundboard.resources;

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

  @Inject
  public AudioMixersResource(AudioMixerDAO audioMixerDAO) {
    this.audioMixerDAO = audioMixerDAO;
  }

  @POST
  public AudioMixerInsert createAudioMixer(AudioMixerInsert audioMixerInsert) {
    audioMixerDAO.insert(audioMixerInsert);
    return audioMixerInsert;
  }

  @GET
  @Path("/{audioMixerId}")
  public Optional<AudioMixerRow> getAudioMixer(
    @PathParam("audioMixerId") int audioMixerId
  ) {
    return audioMixerDAO.get(audioMixerId);
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
