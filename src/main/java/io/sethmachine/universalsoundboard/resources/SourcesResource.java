package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.PlayAudioFileToSourceAudioMixerRequest;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.SourceCaptureCommandRequest;
import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.TotalActiveSourceCapturesResponse;
import io.sethmachine.universalsoundboard.service.PlayAudioToSourceRunnableService;
import io.sethmachine.universalsoundboard.service.SourceAudioMixerRunnableService;
import java.io.InputStream;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/sources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SourcesResource {

  private final PlayAudioToSourceRunnableService playAudioToSourceRunnableService;
  private final SourceAudioMixerRunnableService sourceAudioMixerRunnableService;

  @Inject
  public SourcesResource(
    PlayAudioToSourceRunnableService playAudioToSourceRunnableService,
    SourceAudioMixerRunnableService sourceAudioMixerRunnableService
  ) {
    this.playAudioToSourceRunnableService = playAudioToSourceRunnableService;
    this.sourceAudioMixerRunnableService = sourceAudioMixerRunnableService;
  }

  @POST
  @Path("/play")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void playAudioFileToSource(
    @NotNull @FormDataParam(
      "sourceCommandRequest"
    ) PlayAudioFileToSourceAudioMixerRequest playAudioFileToSourceAudioMixerRequest,
    @NotNull @FormDataParam("audioFile") InputStream uploadInputStream,
    @NotNull @FormDataParam("audioFile") FormDataContentDisposition fileDetail
  ) {
    playAudioToSourceRunnableService.playAudio(
      playAudioFileToSourceAudioMixerRequest.getSourceId(),
      playAudioFileToSourceAudioMixerRequest.getReformat(),
      uploadInputStream,
      fileDetail
    );
  }

  @POST
  @Path("/start-capture")
  public void startSourceCapture(
    SourceCaptureCommandRequest sourceCaptureCommandRequest
  ) {
    sourceAudioMixerRunnableService.startOutboundVoiceAgent(
      sourceCaptureCommandRequest.getSourceId()
    );
  }

  @POST
  @Path("/stop-capture")
  public void stopSourceCapture(SourceCaptureCommandRequest sourceCaptureCommandRequest) {
    sourceAudioMixerRunnableService.stopSourceCapture(
      sourceCaptureCommandRequest.getSourceId()
    );
  }

  @GET
  @Path("/total-active-captures")
  public TotalActiveSourceCapturesResponse getTotalActiveSourceCaptures() {
    return TotalActiveSourceCapturesResponse
      .builder()
      .setCount(sourceAudioMixerRunnableService.getTotalActiveSourceCaptures())
      .build();
  }
}
