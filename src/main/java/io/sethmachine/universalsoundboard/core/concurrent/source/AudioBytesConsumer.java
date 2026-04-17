package io.sethmachine.universalsoundboard.core.concurrent.source;

@FunctionalInterface
public interface AudioBytesConsumer {
  void accept(byte[] data, int offset, int length);
}
