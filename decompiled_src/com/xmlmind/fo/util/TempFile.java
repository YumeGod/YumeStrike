package com.xmlmind.fo.util;

import java.io.File;
import java.io.IOException;

public final class TempFile {
   private TempFile() {
   }

   public static File create(String var0, String var1) throws IOException {
      if (var0 == null || var0.length() == 0) {
         var0 = "xfc";
      }

      if (var1 == null || var1.length() == 0) {
         var1 = ".tmp";
      }

      return File.createTempFile(var0, var1);
   }
}
