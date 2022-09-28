package org.apache.fop.afp.util;

import java.io.ByteArrayOutputStream;

public final class BinaryUtils {
   public static byte[] convert(int integer, int bufsize) {
      StringBuffer buf = new StringBuffer(Integer.toHexString(integer));
      if (buf.length() % 2 != 0) {
         buf.insert(0, "0");
      }

      int size = buf.length() / 2;
      if (size > bufsize) {
         buf.delete(0, buf.length() - bufsize * 2);
      } else {
         while(size < bufsize) {
            buf.insert(0, "00");
            ++size;
         }
      }

      return convert(buf.toString());
   }

   public static byte[] convert(int integer) {
      return convert(Integer.toHexString(integer));
   }

   public static byte[] convert(String digits) {
      if (digits.length() % 2 != 0) {
         digits = "0" + digits;
      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      for(int i = 0; i < digits.length(); i += 2) {
         char c1 = digits.charAt(i);
         char c2 = digits.charAt(i + 1);
         byte b = 0;
         byte b;
         if (c1 >= '0' && c1 <= '9') {
            b = (byte)(b + (c1 - 48) * 16);
         } else if (c1 >= 'a' && c1 <= 'f') {
            b = (byte)(b + (c1 - 97 + 10) * 16);
         } else {
            if (c1 < 'A' || c1 > 'F') {
               throw new IllegalArgumentException("Bad hexadecimal digit");
            }

            b = (byte)(b + (c1 - 65 + 10) * 16);
         }

         if (c2 >= '0' && c2 <= '9') {
            b = (byte)(b + (c2 - 48));
         } else if (c2 >= 'a' && c2 <= 'f') {
            b = (byte)(b + c2 - 97 + 10);
         } else {
            if (c2 < 'A' || c2 > 'F') {
               throw new IllegalArgumentException("Bad hexadecimal digit");
            }

            b = (byte)(b + c2 - 65 + 10);
         }

         baos.write(b);
      }

      return baos.toByteArray();
   }

   public static void shortToByteArray(short value, byte[] array, int offset) {
      array[offset] = (byte)(value >>> 8);
      array[offset + 1] = (byte)value;
   }

   public static byte[] shortToByteArray(short value) {
      byte[] serverValue = new byte[2];
      shortToByteArray(value, serverValue, 0);
      return serverValue;
   }
}
