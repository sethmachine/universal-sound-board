package io.sethmachine.universalsoundboard.core.util.audiomixer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenAiAudioFormatConverter {

  private static final Logger LOG = LoggerFactory.getLogger(
    OpenAiAudioFormatConverter.class
  );

  public static byte[] convertOpenAiPcm16ToTargetFormat(
    byte[] openAiPcm16Bytes,
    AudioFormat targetFormat
  ) {
    AudioFormat openAiFormat = createOpenAiRealtimeAudioFormat();

    if (openAiFormat.matches(targetFormat)) {
      LOG.trace("OpenAI format matches target format, no conversion needed");
      return openAiPcm16Bytes;
    }

    try {
      AudioInputStream sourceStream = new AudioInputStream(
        new ByteArrayInputStream(openAiPcm16Bytes),
        openAiFormat,
        openAiPcm16Bytes.length / openAiFormat.getFrameSize()
      );

      if (!AudioSystem.isConversionSupported(targetFormat, openAiFormat)) {
        LOG.warn(
          "Direct conversion from OpenAI format to target format is not supported. " +
          "OpenAI format: {}, Target format: {}. Attempting intermediate conversion.",
          openAiFormat,
          targetFormat
        );
        sourceStream = attemptIntermediateConversion(sourceStream, targetFormat);
      } else {
        sourceStream = AudioSystem.getAudioInputStream(targetFormat, sourceStream);
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int bytesRead;

      while ((bytesRead = sourceStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      sourceStream.close();
      return outputStream.toByteArray();
    } catch (IOException e) {
      LOG.error(
        "Failed to convert OpenAI audio format to target format: {}",
        targetFormat,
        e
      );
      throw new RuntimeException(e);
    }
  }

  public static String convertSourceFormatToOpenAiBase64Pcm16(
    byte[] sourceAudioBytes,
    AudioFormat sourceFormat
  ) {
    byte[] openAiPcm16Bytes = convertSourceFormatToOpenAiPcm16(
      sourceAudioBytes,
      sourceFormat
    );
    return Base64.getEncoder().encodeToString(openAiPcm16Bytes);
  }

  public static byte[] convertSourceFormatToOpenAiPcm16(
    byte[] sourceAudioBytes,
    AudioFormat sourceFormat
  ) {
    AudioFormat openAiFormat = createOpenAiRealtimeAudioFormat();

    if (sourceFormat.matches(openAiFormat)) {
      LOG.trace("Source format matches OpenAI format, no conversion needed");
      return sourceAudioBytes;
    }

    try {
      AudioInputStream sourceStream = new AudioInputStream(
        new ByteArrayInputStream(sourceAudioBytes),
        sourceFormat,
        sourceAudioBytes.length / sourceFormat.getFrameSize()
      );

      if (!AudioSystem.isConversionSupported(openAiFormat, sourceFormat)) {
        LOG.warn(
          "Direct conversion from source format to OpenAI format is not supported. " +
          "Source format: {}, OpenAI format: {}. Attempting intermediate conversion.",
          sourceFormat,
          openAiFormat
        );
        sourceStream = attemptIntermediateConversion(sourceStream, openAiFormat);
      } else {
        sourceStream = AudioSystem.getAudioInputStream(openAiFormat, sourceStream);
      }

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[4096];
      int bytesRead;

      while ((bytesRead = sourceStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }

      sourceStream.close();
      return outputStream.toByteArray();
    } catch (IOException e) {
      LOG.error(
        "Failed to convert source format to OpenAI audio format. Source format: {}",
        sourceFormat,
        e
      );
      throw new RuntimeException(e);
    }
  }

  private static AudioInputStream attemptIntermediateConversion(
    AudioInputStream sourceStream,
    AudioFormat targetFormat
  ) throws IOException {
    AudioFormat sourceFormat = sourceStream.getFormat();
    AudioFormat intermediateFormat = new AudioFormat(
      Encoding.PCM_SIGNED,
      targetFormat.getSampleRate(),
      sourceFormat.getSampleSizeInBits(),
      sourceFormat.getChannels(),
      sourceFormat.getFrameSize(),
      targetFormat.getSampleRate(),
      sourceFormat.isBigEndian()
    );

    if (AudioSystem.isConversionSupported(intermediateFormat, sourceFormat)) {
      LOG.debug("Converting via intermediate format: {}", intermediateFormat);
      AudioInputStream intermediateStream = AudioSystem.getAudioInputStream(
        intermediateFormat,
        sourceStream
      );

      if (AudioSystem.isConversionSupported(targetFormat, intermediateFormat)) {
        return AudioSystem.getAudioInputStream(targetFormat, intermediateStream);
      }
    }

    LOG.warn(
      "Intermediate conversion also not supported. Returning original stream. Audio may be distorted."
    );
    return sourceStream;
  }

  private static AudioFormat createOpenAiRealtimeAudioFormat() {
    return new AudioFormat(
      Encoding.PCM_SIGNED,
      24000.0f,
      16,
      1,
      2,
      24000.0f,
      false
    );
  }
}
