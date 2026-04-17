package io.sethmachine.universalsoundboard.resources;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.StartOutboundVoiceAgentRequest;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.StopOutboundVoiceAgentRequest;
import io.sethmachine.universalsoundboard.service.OutboundVoiceAgentRunnableService;
import io.sethmachine.universalsoundboard.service.PlayAudioToSourceRunnableService;

@Path("/voice-agent")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OutboundVoiceAgentResource {

  private final PlayAudioToSourceRunnableService playAudioToSourceRunnableService;
  private final OutboundVoiceAgentRunnableService outboundVoiceAgentRunnableService;

  @Inject
  public OutboundVoiceAgentResource(
    PlayAudioToSourceRunnableService playAudioToSourceRunnableService,
    OutboundVoiceAgentRunnableService outboundVoiceAgentRunnableService
  ) {
    this.playAudioToSourceRunnableService = playAudioToSourceRunnableService;
    this.outboundVoiceAgentRunnableService = outboundVoiceAgentRunnableService;
  }

  @POST
  @Path("/start")
  public void startVoiceAgent(
    StartOutboundVoiceAgentRequest startOutboundVoiceAgentRequest
  ) throws InterruptedException {
    outboundVoiceAgentRunnableService.startVoiceAgent(
        startOutboundVoiceAgentRequest
    );
  }

  @POST
  @Path("/stop")
  public void stopVoiceAgent(
      StopOutboundVoiceAgentRequest stopOutboundVoiceAgentRequest
  ) throws InterruptedException {
    outboundVoiceAgentRunnableService.stopVoiceAgent(
        stopOutboundVoiceAgentRequest
    );
  }
}
