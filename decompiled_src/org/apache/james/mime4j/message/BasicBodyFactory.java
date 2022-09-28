package org.apache.james.mime4j.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.james.mime4j.dom.BinaryBody;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.util.CharsetUtil;

public class BasicBodyFactory implements BodyFactory {
   private static String DEFAULT_CHARSET;

   public BinaryBody binaryBody(InputStream is) throws IOException {
      return new BasicBinaryBody(bufferContent(is));
   }

   public TextBody textBody(InputStream is, String mimeCharset) throws IOException {
      return new BasicTextBody(bufferContent(is), mimeCharset);
   }

   private static byte[] bufferContent(InputStream is) throws IOException {
      if (is == null) {
         throw new IllegalArgumentException("Input stream may not be null");
      } else {
         ByteArrayOutputStream buf = new ByteArrayOutputStream();
         byte[] tmp = new byte[2048];

         int l;
         while((l = is.read(tmp)) != -1) {
            buf.write(tmp, 0, l);
         }

         return buf.toByteArray();
      }
   }

   public TextBody textBody(String text, String mimeCharset) throws UnsupportedEncodingException {
      if (text == null) {
         throw new IllegalArgumentException("Text may not be null");
      } else {
         return new BasicTextBody(text.getBytes(mimeCharset), mimeCharset);
      }
   }

   public TextBody textBody(String text) throws UnsupportedEncodingException {
      return this.textBody(text, DEFAULT_CHARSET);
   }

   public BinaryBody binaryBody(byte[] buf) {
      return new BasicBinaryBody(buf);
   }

   static {
      DEFAULT_CHARSET = CharsetUtil.DEFAULT_CHARSET.name();
   }
}
