package io.sethmachine.universalsoundboard.resources;

import io.sethmachine.universalsoundboard.core.model.api.v1.audiomixers.concurrent.PlayAudioFileToSourceAndSinkRequest;
import io.sethmachine.universalsoundboard.service.SoundBoardService;
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

@Path("/sound-board")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SoundBoardResource {

  private final SoundBoardService soundBoardService;

  @Inject
  public SoundBoardResource(SoundBoardService soundBoardService) {
    this.soundBoardService = soundBoardService;
  }

  @POST
  @Path("/play")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public void playAudioFileToSourceAndSink(
    @NotNull @FormDataParam(
      "playAudioFileToSourceAndSinkRequest"
    ) PlayAudioFileToSourceAndSinkRequest playAudioFileToSourceAndSinkRequest,
    @NotNull @FormDataParam("audioFile") InputStream uploadInputStream,
    @NotNull @FormDataParam("audioFile") FormDataContentDisposition fileDetail
  ) {
    soundBoardService.playAudioToSourceAndSink(
      playAudioFileToSourceAndSinkRequest.getSourceId(),
      playAudioFileToSourceAndSinkRequest.getSinkId(),
      playAudioFileToSourceAndSinkRequest.getReformat(),
      uploadInputStream,
      fileDetail
    );
  }
}
