package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.SinkCommandRequest;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.TotalActivesSinksResponse;
import io.sethmachine.universalsoundboard.service.SinkAudioMixerRunnableService;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sinks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SinksResource {

  private final SinkAudioMixerRunnableService sinkAudioMixerRunnableService;

  @Inject
  public SinksResource(SinkAudioMixerRunnableService sinkAudioMixerRunnableService) {
    this.sinkAudioMixerRunnableService = sinkAudioMixerRunnableService;
  }

  @POST
  @Path("/start")
  public void startSink(SinkCommandRequest sinkCommandRequest) {
    sinkAudioMixerRunnableService.startSink(sinkCommandRequest.getSinkId());
  }

  @POST
  @Path("/stop")
  public void stopSink(SinkCommandRequest sinkCommandRequest) {
    sinkAudioMixerRunnableService.stopSink(sinkCommandRequest.getSinkId());
  }

  @GET
  @Path("/total-active")
  public TotalActivesSinksResponse getTotalActiveSinks() {
    return TotalActivesSinksResponse
      .builder()
      .setCount(sinkAudioMixerRunnableService.getTotalActiveSinks())
      .build();
  }
}
