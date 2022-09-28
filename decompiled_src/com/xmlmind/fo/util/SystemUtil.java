package com.xmlmind.fo.util;

import java.io.File;
import java.io.IOException;

public final class SystemUtil {
   public static final int PLATFORM_WINDOWS = 1;
   public static final int PLATFORM_MAC_OS = 2;
   public static final int PLATFORM_GENERIC_UNIX = 3;
   public static final int PLATFORM = platform();
   public static final boolean IS_WINDOWS;
   public static final boolean IS_UNIX;
   public static final boolean IS_MAC_OS;
   public static final boolean IS_GENERIC_UNIX;

   private SystemUtil() {
   }

   private static final int platform() {
      if (File.pathSeparatorChar == ';') {
         return 1;
      } else {
         String var0 = System.getProperty("os.name");
         return var0 != null && var0.toLowerCase().indexOf("mac") >= 0 ? 2 : 3;
      }
   }

   public static File homeDir() {
      return userDir("user.home");
   }

   public static File currentWorkingDir() {
      return userDir("user.dir");
   }

   private static File userDir(String var0) {
      File var1 = null;
      String var2 = System.getProperty(var0);
      if (var2 != null) {
         try {
            var1 = (new File(var2)).getCanonicalFile();
            if (!var1.isDirectory()) {
               var1 = null;
            }
         } catch (IOException var4) {
         }
      }

      return var1;
   }

   public static File userPreferencesDir() {
      return userPreferencesDir("XMLmind", "FOConverter", "xfc");
   }

   public static File userPreferencesDir(String var0, String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      if (IS_WINDOWS) {
         String var4 = null;

         try {
            var4 = System.getenv("APPDATA");
         } catch (Throwable var7) {
         }

         if (var4 != null && (new File(var4)).isDirectory()) {
            var3.append(var4);
            var3.append('\\');
            var3.append(var0);
            var3.append('\\');
            var3.append(var1);
         }
      }

      File var8;
      if (var3.length() == 0) {
         var8 = homeDir();
         if (var8 == null) {
            return null;
         }

         var3.append(var8.getPath());
         if (IS_WINDOWS) {
            String var5 = System.getProperty("os.name");
            if (var5 != null && var5.indexOf("Vista") >= 0) {
               var3.append("\\AppData\\Roaming\\");
            } else {
               var3.append("\\Application Data\\");
            }

            var3.append(var0);
            var3.append('\\');
            var3.append(var1);
         } else if (IS_MAC_OS) {
            var3.append("/Library/Application Support/");
            var3.append(var0);
            var3.append('/');
            var3.append(var1);
         } else {
            var3.append("/.");
            var3.append(var2);
         }
      }

      var8 = null;

      try {
         var8 = (new File(var3.toString())).getCanonicalFile();
      } catch (IOException var6) {
      }

      return var8;
   }

   static {
      IS_WINDOWS = PLATFORM == 1;
      IS_UNIX = PLATFORM != 1;
      IS_MAC_OS = PLATFORM == 2;
      IS_GENERIC_UNIX = PLATFORM == 3;
   }
}
