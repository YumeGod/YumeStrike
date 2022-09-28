package org.apache.james.mime4j.codec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CodecUtil {
   static final int DEFAULT_ENCODING_BUFFER_SIZE = 1024;

   public static void copy(InputStream in, OutputStream out) throws IOException {
      byte[] buffer = new byte[1024];

      int inputLength;
      while(-1 != (inputLength = in.read(buffer))) {
         out.write(buffer, 0, inputLength);
      }

   }

   public static void encodeQuotedPrintableBinary(InputStream in, OutputStream out) throws IOException {
      QuotedPrintableOutputStream qpOut = new QuotedPrintableOutputStream(out, true);
      copy(in, qpOut);
      qpOut.close();
   }

   public static void encodeQuotedPrintable(InputStream in, OutputStream out) throws IOException {
      QuotedPrintableOutputStream qpOut = new QuotedPrintableOutputStream(out, false);
      copy(in, qpOut);
      qpOut.close();
   }

   public static void encodeBase64(InputStream in, OutputStream out) throws IOException {
      Base64OutputStream b64Out = new Base64OutputStream(out);
      copy(in, b64Out);
      b64Out.close();
   }

   public static OutputStream wrapQuotedPrintable(OutputStream out, boolean binary) throws IOException {
      return new QuotedPrintableOutputStream(out, binary);
   }

   public static OutputStream wrapBase64(OutputStream out) throws IOException {
      return new Base64OutputStream(out);
   }
}
