package io.sethmachine.universalsoundboard.core.concurrent.source;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileAudioBytesConsumer implements AudioBytesConsumer, Closeable {

  private static final Logger LOG = LoggerFactory.getLogger(FileAudioBytesConsumer.class);

  private final AudioFormat audioFormat;
  private final File outputFile;
  private final ByteArrayOutputStream byteArrayOutputStream;
  private long totalBytesWritten;

  public FileAudioBytesConsumer(AudioFormat audioFormat, File outputFile) {
    this.audioFormat = audioFormat;
    this.outputFile = outputFile;
    this.byteArrayOutputStream = new ByteArrayOutputStream();
    this.totalBytesWritten = 0;
    LOG.info("Created file audio consumer writing to: {}", outputFile.getAbsolutePath());
  }

  @Override
  public void accept(byte[] data, int offset, int length) {
    try {
      byteArrayOutputStream.write(data, offset, length);
      totalBytesWritten += length;
      LOG.debug("Wrote {} bytes to buffer, total: {}", length, totalBytesWritten);
    } catch (Exception e) {
      LOG.error("Failed to write audio bytes to buffer", e);
    }
  }

  @Override
  public void close() {
    try {
      byte[] audioData = byteArrayOutputStream.toByteArray();
      LOG.info(
        "Closing file audio consumer, writing {} bytes to file: {}",
        audioData.length,
        outputFile.getAbsolutePath()
      );

      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
      AudioInputStream audioInputStream = new AudioInputStream(
        byteArrayInputStream,
        audioFormat,
        audioData.length / audioFormat.getFrameSize()
      );

      AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);

      audioInputStream.close();
      byteArrayOutputStream.close();

      LOG.info("Successfully wrote audio file: {}", outputFile.getAbsolutePath());
    } catch (IOException e) {
      LOG.error("Failed to write audio file: {}", outputFile.getAbsolutePath(), e);
    }
  }

  public long getTotalBytesWritten() {
    return totalBytesWritten;
  }

  public File getOutputFile() {
    return outputFile;
  }
}
