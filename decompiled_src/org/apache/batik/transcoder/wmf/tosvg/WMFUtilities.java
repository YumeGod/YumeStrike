package org.apache.batik.transcoder.wmf.tosvg;

import java.io.UnsupportedEncodingException;

public class WMFUtilities {
   public static String decodeString(WMFFont var0, byte[] var1) {
      try {
         switch (var0.charset) {
            case 0:
               return new String(var1, "ISO-8859-1");
            case 1:
               return new String(var1, "US-ASCII");
            case 128:
               return new String(var1, "Shift_JIS");
            case 129:
               return new String(var1, "cp949");
            case 130:
               return new String(var1, "x-Johab");
            case 134:
               return new String(var1, "GB2312");
            case 136:
               return new String(var1, "Big5");
            case 161:
               return new String(var1, "windows-1253");
            case 162:
               return new String(var1, "cp1254");
            case 163:
               return new String(var1, "cp1258");
            case 177:
               return new String(var1, "windows-1255");
            case 178:
               return new String(var1, "windows-1256");
            case 204:
               return new String(var1, "windows-1251");
            case 222:
               return new String(var1, "cp874");
            case 238:
               return new String(var1, "cp1250");
            case 255:
               return new String(var1, "cp437");
         }
      } catch (UnsupportedEncodingException var3) {
      }

      return new String(var1);
   }

   public static int getHorizontalAlignment(int var0) {
      int var1 = var0 % 24;
      var1 %= 8;
      if (var1 >= 6) {
         return 6;
      } else {
         return var1 >= 2 ? 2 : 0;
      }
   }

   public static int getVerticalAlignment(int var0) {
      if (var0 / 24 != 0) {
         return 24;
      } else {
         int var1 = var0 % 24;
         return var1 / 8 != 0 ? 8 : 0;
      }
   }
}
