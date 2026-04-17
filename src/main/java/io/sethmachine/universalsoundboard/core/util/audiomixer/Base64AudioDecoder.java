package io.sethmachine.universalsoundboard.core.util.audiomixer;

import java.util.Base64;

public class Base64AudioDecoder {

  public static byte[] decodeBase64ToPcm16Bytes(String base64Audio) {
    if (base64Audio == null || base64Audio.isEmpty()) {
      return new byte[0];
    }

    return Base64.getDecoder().decode(base64Audio);
  }

  public static byte[] decodeBase64ToPcm16Bytes(
    String base64Audio,
    int offset,
    int length
  ) {
    byte[] fullBytes = decodeBase64ToPcm16Bytes(base64Audio);

    if (offset == 0 && length >= fullBytes.length) {
      return fullBytes;
    }

    if (offset < 0 || offset >= fullBytes.length) {
      throw new IllegalArgumentException(
        "Offset must be within bounds of decoded bytes"
      );
    }

    int actualLength = Math.min(length, fullBytes.length - offset);
    byte[] result = new byte[actualLength];
    System.arraycopy(fullBytes, offset, result, 0, actualLength);
    return result;
  }
}
