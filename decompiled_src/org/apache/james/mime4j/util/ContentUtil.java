package org.apache.james.mime4j.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ContentUtil {
   private ContentUtil() {
   }

   public static ByteSequence encode(String string) {
      if (string == null) {
         return null;
      } else {
         ByteArrayBuffer buf = new ByteArrayBuffer(string.length());

         for(int i = 0; i < string.length(); ++i) {
            buf.append((byte)string.charAt(i));
         }

         return buf;
      }
   }

   public static ByteSequence encode(Charset charset, String string) {
      if (string == null) {
         return null;
      } else {
         if (charset == null) {
            charset = Charset.defaultCharset();
         }

         ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
         ByteArrayBuffer buf = new ByteArrayBuffer(encoded.remaining());
         buf.append(encoded.array(), encoded.position(), encoded.remaining());
         return buf;
      }
   }

   public static String decode(ByteSequence byteSequence) {
      return byteSequence == null ? null : decode(byteSequence, 0, byteSequence.length());
   }

   public static String decode(Charset charset, ByteSequence byteSequence) {
      return decode(charset, (ByteSequence)byteSequence, 0, byteSequence.length());
   }

   public static String decode(ByteSequence byteSequence, int offset, int length) {
      if (byteSequence == null) {
         return null;
      } else {
         StringBuilder buf = new StringBuilder(length);

         for(int i = offset; i < offset + length; ++i) {
            buf.append((char)(byteSequence.byteAt(i) & 255));
         }

         return buf.toString();
      }
   }

   public static String decode(Charset charset, ByteSequence byteSequence, int offset, int length) {
      if (byteSequence == null) {
         return null;
      } else {
         if (charset == null) {
            charset = Charset.defaultCharset();
         }

         if (byteSequence instanceof ByteArrayBuffer) {
            ByteArrayBuffer bab = (ByteArrayBuffer)byteSequence;
            return decode(charset, bab.buffer(), offset, length);
         } else {
            byte[] bytes = byteSequence.toByteArray();
            return decode(charset, bytes, offset, length);
         }
      }
   }

   private static String decode(Charset charset, byte[] buffer, int offset, int length) {
      return charset.decode(ByteBuffer.wrap(buffer, offset, length)).toString();
   }
}
