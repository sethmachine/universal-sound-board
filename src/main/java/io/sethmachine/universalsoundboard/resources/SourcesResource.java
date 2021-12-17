package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.SourceCommandRequest;
import io.sethmachine.universalsoundboard.service.PlayAudioToSourceRunnableService;
import java.io.InputStream;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
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

  @Inject
  public SourcesResource(
    PlayAudioToSourceRunnableService playAudioToSourceRunnableService
  ) {
    this.playAudioToSourceRunnableService = playAudioToSourceRunnableService;
  }

  @POST
  @Path("/play")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void playAudioFileToSource(
    @NotNull @FormDataParam(
      "sourceCommandRequest"
    ) SourceCommandRequest sourceCommandRequest,
    @NotNull @FormDataParam("audioFile") InputStream uploadInputStream,
    @NotNull @FormDataParam("audioFile") FormDataContentDisposition fileDetail
  ) {
    playAudioToSourceRunnableService.playAudio(
      sourceCommandRequest.getSourceId(),
      uploadInputStream,
      fileDetail
    );
  }
}
