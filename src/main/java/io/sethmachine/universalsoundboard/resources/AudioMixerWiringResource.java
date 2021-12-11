package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.wiring.AudioMixerWiringList;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.wiring.WireSinkToSourceRequest;
import io.sethmachine.universalsoundboard.core.model.audiomixers.wiring.AudioMixerWiringPair;
import io.sethmachine.universalsoundboard.db.model.audiomixer.wiring.AudioMixerWiringRow;
import io.sethmachine.universalsoundboard.service.AudioMixerWiringService;
import io.sethmachine.universalsoundboard.service.api.AudioMixerWiringApiService;
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

  private final AudioMixerWiringApiService audioMixerWiringApiService;

  @Inject
  public AudioMixerWiringResource(AudioMixerWiringApiService audioMixerWiringApiService) {
    this.audioMixerWiringApiService = audioMixerWiringApiService;
  }

  @GET
  public AudioMixerWiringList listWirings() {
    return audioMixerWiringApiService.getAllWirings();
  }

  @POST
  public void wireSinkToSource(WireSinkToSourceRequest wireSinkToSourceRequest) {
    audioMixerWiringApiService.wireSinkToSource(wireSinkToSourceRequest);
  }

  @GET
  @Path("/{sinkId}/{sourceId}")
  public Optional<AudioMixerWiringPair> getWiring(
    @PathParam("sinkId") int sinkId,
    @PathParam("sourceId") int sourceId
  ) {
    return audioMixerWiringApiService.getWiring(sinkId, sourceId);
  }

  @GET
  @Path("/sink/{sinkId}")
  public AudioMixerWiringList getSinkWirings(@PathParam("sinkId") int sinkId) {
    return audioMixerWiringApiService.getSinkWirings(sinkId);
  }

  @GET
  @Path("/source/{sourceId}")
  public AudioMixerWiringList getSourceWirings(@PathParam("sourceId") int sourceId) {
    return audioMixerWiringApiService.getSourceWirings(sourceId);
  }
}
