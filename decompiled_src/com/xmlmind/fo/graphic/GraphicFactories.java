package com.xmlmind.fo.graphic;

import com.xmlmind.fo.util.StringUtil;
import com.xmlmind.fo.util.URLUtil;

public final class GraphicFactories {
   private static Object lock = new Object();
   private static GraphicFactory[] factories = new GraphicFactory[0];

   private GraphicFactories() {
   }

   public static void register(GraphicFactory var0) {
      synchronized(lock) {
         int var2 = factories.length;
         GraphicFactory[] var3 = new GraphicFactory[var2 + 1];
         System.arraycopy(factories, 0, var3, 0, var2);
         var3[var2] = var0;
         factories = var3;
      }
   }

   public static GraphicFactory get(String var0) {
      var0 = URLUtil.normalizeMIMEType(var0);
      synchronized(lock) {
         for(int var2 = factories.length - 1; var2 >= 0; --var2) {
            GraphicFactory var3 = factories[var2];
            String[] var4 = var3.getInputFormats();
            if (StringUtil.contains(var4, var0)) {
               return var3;
            }
         }

         return null;
      }
   }

   public static GraphicFactory get(String var0, String var1) {
      var0 = URLUtil.normalizeMIMEType(var0);
      var1 = URLUtil.normalizeMIMEType(var1);
      synchronized(lock) {
         for(int var3 = factories.length - 1; var3 >= 0; --var3) {
            GraphicFactory var4 = factories[var3];
            String[] var5 = var4.getInputFormats();
            if (StringUtil.contains(var5, var0)) {
               var5 = var4.getOutputFormats();
               if (StringUtil.contains(var5, var1)) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   public static Graphic createGraphic(String var0, String var1, Object var2, GraphicEnv var3) throws Exception {
      GraphicFactory var4 = get(var1);
      if (var4 == null) {
         throw new RuntimeException("'" + var1 + "', unsupported graphic format");
      } else {
         return var4.createGraphic(var0, var1, var2, var3);
      }
   }

   public static Graphic convertGraphic(Graphic var0, String var1, double var2, double var4, Object var6, GraphicEnv var7) throws Exception {
      return convertGraphic(var0, new String[]{var1}, var2, var4, var6, var7);
   }

   public static Graphic convertGraphic(Graphic var0, String[] var1, double var2, double var4, Object var6, GraphicEnv var7) throws Exception {
      String var8 = var0.getFormat();
      String var9 = null;
      GraphicFactory var10 = null;
      if ("image/jpeg".equals(var8) && StringUtil.contains(var1, "image/jpeg")) {
         var10 = get("image/jpeg", "image/jpeg");
         if (var10 != null) {
            var9 = "image/jpeg";
         }
      }

      int var12;
      if (var10 == null) {
         int var11 = var1.length;

         for(var12 = 0; var12 < var11; ++var12) {
            if (!"image/jpeg".equals(var1[var12])) {
               var10 = get(var8, var1[var12]);
               if (var10 != null) {
                  var9 = var1[var12];
                  break;
               }
            }
         }
      }

      if (var10 == null) {
         StringBuffer var13 = new StringBuffer();

         for(var12 = 0; var12 < var1.length; ++var12) {
            if (var12 > 0) {
               var13.append(", ");
            }

            var13.append('\'');
            var13.append(var1[var12]);
            var13.append('\'');
         }

         throw new RuntimeException("don't know how to convert a '" + var8 + "' graphic to any of the following formats: " + var13.toString());
      } else {
         return var10.convertGraphic(var0, var9, var2, var4, var6, var7);
      }
   }

   static {
      AutoRegisterFactories.registerAll();
   }
}
