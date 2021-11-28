package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.wiring.WireSinkToSourceRequest;
import io.sethmachine.universalsoundboard.db.model.audiomixer.wiring.AudioMixerWiringRow;
import io.sethmachine.universalsoundboard.service.AudioMixerWiringService;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/audio-mixer-wiring")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AudioMixerWiringResource {

  private final AudioMixerWiringService audioMixerWiringService;

  @Inject
  public AudioMixerWiringResource(AudioMixerWiringService audioMixerWiringService) {
    this.audioMixerWiringService = audioMixerWiringService;
  }

  @POST
  public Optional<AudioMixerWiringRow> wireSinkToSource(
    WireSinkToSourceRequest wireSinkToSourceRequest
  ) {
    return audioMixerWiringService.wireSinkToSource(
      wireSinkToSourceRequest.getSinkId(),
      wireSinkToSourceRequest.getSourceId()
    );
  }

  @GET
  @Path("/{sinkId}/{sourceId}")
  public Optional<AudioMixerWiringRow> getWiring(
    @PathParam("sinkId") int sinkId,
    @PathParam("sourceId") int sourceId
  ) {
    return audioMixerWiringService.getWiring(sinkId, sourceId);
  }
}
