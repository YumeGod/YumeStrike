package com.xmlmind.fo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class FileUtil {
   private FileUtil() {
   }

   public static void copyFile(File var0, File var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var0);

      try {
         copyFile((InputStream)var2, (File)var1);
      } finally {
         var2.close();
      }

   }

   public static void copyFile(InputStream var0, File var1) throws IOException {
      FileOutputStream var2 = new FileOutputStream(var1);

      try {
         copyFile((InputStream)var0, (OutputStream)var2);
      } finally {
         var2.close();
      }

   }

   public static void copyFile(File var0, OutputStream var1) throws IOException {
      FileInputStream var2 = new FileInputStream(var0);

      try {
         copyFile((InputStream)var2, (OutputStream)var1);
      } finally {
         var2.close();
      }

   }

   public static final void copyFile(InputStream var0, OutputStream var1) throws IOException {
      byte[] var2 = new byte['\uffff'];

      int var3;
      while((var3 = var0.read(var2)) != -1) {
         var1.write(var2, 0, var3);
      }

      var1.flush();
   }
}
