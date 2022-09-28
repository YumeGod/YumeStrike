package com.xmlmind.fo.converter.docx.sdt;

import java.io.File;
import java.io.FileOutputStream;

public final class SdtDefaultImage {
   private static final byte[] data = new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72, 68, 82, 0, 0, 0, 20, 0, 0, 0, 20, 8, 2, 0, 0, 0, 2, -21, -118, 90, 0, 0, 0, 4, 103, 65, 77, 65, 0, 0, -79, -113, 11, -4, 97, 5, 0, 0, 0, 1, 115, 82, 71, 66, 0, -82, -50, 28, -23, 0, 0, 0, 32, 99, 72, 82, 77, 0, 0, 122, 38, 0, 0, -128, -124, 0, 0, -6, 0, 0, 0, -128, -24, 0, 0, 117, 48, 0, 0, -22, 96, 0, 0, 58, -104, 0, 0, 23, 112, -100, -70, 81, 60, 0, 0, 0, 9, 111, 70, 70, 115, 0, 0, 0, 37, 0, 0, 0, 31, 0, 64, 17, 44, -43, 0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 14, -61, 0, 0, 14, -61, 1, -57, 111, -88, 100, 0, 0, 0, 29, 73, 68, 65, 84, 120, -38, 99, 124, -15, -31, 47, 3, -71, -128, -119, 108, -99, -93, -102, 71, 53, -113, 106, 30, -43, 76, 21, -51, 0, -34, 44, 2, -3, 95, -40, 13, 9, 0, 0, 0, 0, 73, 69, 78, 68, -82, 66, 96, -126};

   public static String format() {
      return "image/png";
   }

   public static void write(File var0) throws Exception {
      FileOutputStream var1 = new FileOutputStream(var0);

      try {
         var1.write(data);
         var1.flush();
      } finally {
         var1.close();
      }

   }
}
