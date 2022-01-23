package io.sethmachine.universalsoundboard.core.util.audiomixer;

import static javax.sound.sampled.AudioSystem.NOT_SPECIFIED;

import io.sethmachine.universalsoundboard.core.model.audiomixers.SourceAudioMixer;
import io.sethmachine.universalsoundboard.core.model.concurrent.source.AudioFileStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AudioFormatUtil {

  private static final Logger LOG = LoggerFactory.getLogger(AudioFormatUtil.class);
  private static final float DEFAULT_SAMPLE_RATE_IF_UNSPECIFIED = 48000;
  private static final float DEFAULT_FRAME_RATE_IF_UNSPECIFIED = 48000;

  public static SourceAudioMixer reformatSourceAudioMixerFormatIfItHasUnspecifiedValues(
    SourceAudioMixer sourceAudioMixer
  ) {
    return SourceAudioMixer
      .builder()
      .from(sourceAudioMixer)
      .setAudioFormat(
        handleUnspecifiedValuesInAudioFormat(sourceAudioMixer.getAudioFormat())
      )
      .build();
  }

  public static AudioFileStream createAudioFileStream(
    InputStream inputStream,
    FormDataContentDisposition fileDetail,
    AudioFormat targetAudioFormat,
    boolean reformat
  ) {
    try {
      byte[] allInputBytes = inputStream.readAllBytes();
      return createAudioFileStream(
        fileDetail,
        allInputBytes,
        targetAudioFormat,
        reformat
      );
    } catch (IOException e) {
      e.printStackTrace();
      LOG.error(
        "Unable to read audio bytes from input file {}",
        fileDetail.getFileName(),
        e
      );
      throw new RuntimeException(e);
    }
  }

  public static AudioFileStream createAudioFileStream(
    FormDataContentDisposition fileDetail,
    byte[] audioFileBytes,
    AudioFormat targetAudioFormat,
    boolean reformat
  ) {
    try {
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
        new ByteArrayInputStream(audioFileBytes)
      );
      if (reformat) {
        if (!audioInputStream.getFormat().matches(targetAudioFormat)) {
          LOG.info(
            "Attempting to reformat audio file input stream to match source audio mixer"
          );
          audioInputStream =
            reformatAudioInputStream(audioInputStream, targetAudioFormat);
          LOG.info(
            "Successfully reformatted audio file input stream to match source audio mixer format"
          );
        }
      }
      return AudioFileStream
        .builder()
        .setFilename(fileDetail.getFileName())
        .setAudioInputStream(audioInputStream)
        .setTotalBytes(audioFileBytes.length)
        .build();
    } catch (UnsupportedAudioFileException | IOException e) {
      e.printStackTrace();
      LOG.error(
        "Unable to read audio bytes from input file {} into an audio input stream",
        fileDetail.getFileName(),
        e
      );
      throw new RuntimeException(e);
    }
  }

  public static AudioFormat handleUnspecifiedValuesInAudioFormat(
    AudioFormat audioFormat
  ) {
    Encoding encoding = audioFormat.getEncoding();
    float sampleRate = audioFormat.getSampleRate();
    int sampleSizeInBits = audioFormat.getSampleSizeInBits();
    int channels = audioFormat.getChannels();
    int frameSize = audioFormat.getFrameSize();
    float frameRate = audioFormat.getFrameRate();
    boolean bigEndian = audioFormat.isBigEndian();
    // this is a shallow copy, hopefully none of the values are mutable
    Map<String, Object> properties = Map.copyOf(audioFormat.properties());
    boolean hasUnspecifiedFormat = false;
    if (sampleRate == NOT_SPECIFIED) {
      LOG.warn(
        "The sample rate is not specified for this audio format.  Defaulting to {}",
        DEFAULT_SAMPLE_RATE_IF_UNSPECIFIED
      );
      sampleRate = DEFAULT_SAMPLE_RATE_IF_UNSPECIFIED;
      hasUnspecifiedFormat = true;
    }
    if (frameRate == NOT_SPECIFIED) {
      LOG.warn(
        "The frame rate is not specified for this audio format.  Defaulting to {}",
        DEFAULT_FRAME_RATE_IF_UNSPECIFIED
      );
      frameRate = DEFAULT_FRAME_RATE_IF_UNSPECIFIED;
      hasUnspecifiedFormat = true;
    }
    if (hasUnspecifiedFormat) {
      return new AudioFormat(
        encoding,
        sampleRate,
        sampleSizeInBits,
        channels,
        frameSize,
        frameRate,
        bigEndian,
        properties
      );
    }
    return audioFormat;
  }

  private static AudioInputStream reformatAudioInputStream(
    AudioInputStream audioInputStream,
    AudioFormat targetFormat
  ) {
    if (AudioSystem.isConversionSupported(targetFormat, audioInputStream.getFormat())) {
      return AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
    }
    LOG.error(
      "Conversion between formats unsupported.  Source format: {},  Target format: {}",
      audioInputStream.getFormat(),
      targetFormat
    );
    return audioInputStream;
  }
}
