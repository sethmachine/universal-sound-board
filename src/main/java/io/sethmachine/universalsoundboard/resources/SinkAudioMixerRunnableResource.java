package io.sethmachine.universalsoundboard.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnable;
import io.sethmachine.universalsoundboard.core.concurrent.SinkAudioMixerRunnableFactory;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.AudioMixerId;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.SinkCommandRequest;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.TotalActivesSinksResponse;
import io.sethmachine.universalsoundboard.db.audiomixer.AudioMixerInsert;
import io.sethmachine.universalsoundboard.db.daos.AudioMixerDAO;
import io.sethmachine.universalsoundboard.service.SinkAudioMixerRunnableService;

@Path("/sink")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SinkAudioMixerRunnableResource {

  private final AudioMixerDAO audioMixerDAO;
  private final SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory;
  private final SinkAudioMixerRunnableService sinkAudioMixerRunnableService;

  @Inject
  public SinkAudioMixerRunnableResource(AudioMixerDAO audioMixerDAO,
                                        SinkAudioMixerRunnableFactory sinkAudioMixerRunnableFactory,
                                        SinkAudioMixerRunnableService sinkAudioMixerRunnableService) {
    this.audioMixerDAO = audioMixerDAO;
    this.sinkAudioMixerRunnableFactory = sinkAudioMixerRunnableFactory;
    this.sinkAudioMixerRunnableService = sinkAudioMixerRunnableService;
  }

  @POST
  public AudioMixerId createAudioMixer(AudioMixerInsert audioMixerInsert) {
    return AudioMixerId.builder().setId(audioMixerDAO.insert(audioMixerInsert)).build();
  }

  @POST
  @Path("/start")
  public void startSink(
    SinkCommandRequest sinkCommandRequest
  ) {
    sinkAudioMixerRunnableService.startSink(sinkCommandRequest.getSinkId());
  }

  @POST
  @Path("/stop")
  public void stopSink(
      SinkCommandRequest sinkCommandRequest
  ) {
    sinkAudioMixerRunnableService.stopSink(sinkCommandRequest.getSinkId());
  }

  @GET
  @Path("/total-active")
  public TotalActivesSinksResponse getTotalActiveSinks(){
    return
        TotalActivesSinksResponse.builder().setCount(
        sinkAudioMixerRunnableService.getTotalActiveSinks()).build();
  }

  @GET
  @Path("/foo")
  public void foo(){
    SinkAudioMixerRunnable sinkAudioMixerRunnable = sinkAudioMixerRunnableFactory.create(5);
    int bar = 5 + 6;
  }
}
