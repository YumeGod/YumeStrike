package com.xmlmind.fo.graphic;

import com.xmlmind.fo.util.FileUtil;
import com.xmlmind.fo.util.URLUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class GraphicUtil {
   private GraphicUtil() {
   }

   public static String extensionToFormat(File var0) {
      return extensionToFormat(var0.getPath());
   }

   public static String extensionToFormat(String var0) {
      var0 = var0.toLowerCase();
      if (var0.endsWith(".png")) {
         return "image/png";
      } else if (!var0.endsWith(".jpg") && !var0.endsWith(".jpeg")) {
         if (var0.endsWith(".gif")) {
            return "image/gif";
         } else if (!var0.endsWith(".tif") && !var0.endsWith(".tiff")) {
            if (var0.endsWith(".bmp")) {
               return "image/bmp";
            } else if (var0.endsWith(".emf")) {
               return "image/x-emf";
            } else if (var0.endsWith(".wmf")) {
               return "image/x-wmf";
            } else if (var0.endsWith(".svg")) {
               return "image/svg+xml";
            } else if (var0.endsWith(".mml")) {
               return "application/mathml+xml";
            } else if (var0.endsWith(".pdf")) {
               return "application/pdf";
            } else if (var0.endsWith(".ps")) {
               return "application/ps";
            } else if (!var0.endsWith(".eps") && !var0.endsWith(".epsi") && !var0.endsWith(".epsf")) {
               int var1 = var0.lastIndexOf(46);
               return var1 >= 0 ? "image/" + var0.substring(var1 + 1) : "image/" + var0;
            } else {
               return "image/x-eps";
            }
         } else {
            return "image/tiff";
         }
      } else {
         return "image/jpeg";
      }
   }

   public static String formatToExtension(String var0) {
      if ("image/jpeg".equals(var0)) {
         return "jpg";
      } else if ("image/tiff".equals(var0)) {
         return "tif";
      } else {
         String var1 = null;
         int var2 = var0.indexOf(47);
         if (var2 >= 0) {
            var1 = var0.substring(var2 + 1);
            if (var1.startsWith("x-")) {
               var1 = var1.substring(2);
            }

            var2 = var1.indexOf(43);
            if (var2 >= 0) {
               var1 = var1.substring(0, var2);
            }
         }

         if (var1 != null && var1.length() == 0) {
            var1 = null;
         }

         return var1;
      }
   }

   public static String formatToSuffix(String var0) {
      String var1 = formatToExtension(var0);
      if (var1 == null) {
         var1 = "img";
      }

      return "." + var1;
   }

   public static void saveGraphic(Graphic var0, File var1) throws IOException {
      InputStream var2 = URLUtil.openStream(var0.getLocation());

      try {
         FileUtil.copyFile(var2, var1);
      } finally {
         var2.close();
      }

   }

   public static String createTempFile(GraphicEnv var0, String var1) throws Exception {
      return var0.createTempFile(var1).getPath();
   }
}
