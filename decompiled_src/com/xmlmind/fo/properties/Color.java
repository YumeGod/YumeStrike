package com.xmlmind.fo.properties;

import java.util.Hashtable;
import java.util.StringTokenizer;

public final class Color {
   public static final int AQUA = 0;
   public static final int BLACK = 1;
   public static final int BLUE = 2;
   public static final int FUCHSIA = 3;
   public static final int GRAY = 4;
   public static final int GREEN = 5;
   public static final int LIME = 6;
   public static final int MAROON = 7;
   public static final int NAVY = 8;
   public static final int OLIVE = 9;
   public static final int PURPLE = 10;
   public static final int RED = 11;
   public static final int SILVER = 12;
   public static final int TEAL = 13;
   public static final int WHITE = 14;
   public static final int YELLOW = 15;
   public static final int ORANGE = 16;
   public static final Color[] list = new Color[]{new Color("aqua", 127, 255, 212), new Color("black", 0, 0, 0), new Color("blue", 0, 0, 255), new Color("fuchsia", 0, 0, 0), new Color("gray", 190, 190, 190), new Color("green", 0, 255, 0), new Color("lime", 50, 205, 50), new Color("maroon", 176, 48, 96), new Color("navy", 0, 0, 128), new Color("olive", 85, 107, 47), new Color("purple", 160, 32, 240), new Color("red", 255, 0, 0), new Color("silver", 0, 0, 0), new Color("teal", 0, 0, 0), new Color("white", 255, 255, 255), new Color("yellow", 255, 255, 0), new Color("orange", 255, 165, 0)};
   private static final Hashtable indexes = new Hashtable();
   public String name;
   public int red;
   public int green;
   public int blue;

   public Color(String var1, int var2, int var3, int var4) {
      this.name = var1;
      this.red = var2;
      this.green = var3;
      this.blue = var4;
   }

   public Color(int var1, int var2, int var3) {
      this((String)null, var1, var2, var3);
   }

   public static Color parse(String var0) {
      int var1 = index(var0);
      if (var1 >= 0) {
         return list[var1];
      } else {
         int var2;
         int var3;
         int var4;
         if (var0.startsWith("#")) {
            try {
               switch (var0.length()) {
                  case 4:
                     var2 = Integer.parseInt(var0.substring(1, 2), 16);
                     var2 += var2 << 4;
                     var3 = Integer.parseInt(var0.substring(2, 3), 16);
                     var3 += var3 << 4;
                     var4 = Integer.parseInt(var0.substring(3, 4), 16);
                     var4 += var4 << 4;
                     break;
                  case 7:
                     var2 = Integer.parseInt(var0.substring(1, 3), 16);
                     var3 = Integer.parseInt(var0.substring(3, 5), 16);
                     var4 = Integer.parseInt(var0.substring(5, 7), 16);
                     break;
                  default:
                     return null;
               }
            } catch (NumberFormatException var8) {
               return null;
            }
         } else {
            if (!var0.startsWith("rgb(") && !var0.startsWith("rgb-icc(")) {
               return null;
            }

            var0 = var0.substring(var0.startsWith("rgb-icc(") ? 8 : 4, var0.length());
            StringTokenizer var5 = new StringTokenizer(var0, " ,)");

            try {
               if (!var5.hasMoreTokens()) {
                  return null;
               }

               var2 = Integer.parseInt(var5.nextToken());
               if (!var5.hasMoreTokens()) {
                  return null;
               }

               var3 = Integer.parseInt(var5.nextToken());
               if (!var5.hasMoreTokens()) {
                  return null;
               }

               var4 = Integer.parseInt(var5.nextToken());
            } catch (NumberFormatException var7) {
               return null;
            }
         }

         return var2 >= 0 && var2 <= 255 && var3 >= 0 && var3 <= 255 && var4 >= 0 && var4 <= 255 ? new Color(var2, var3, var4) : null;
      }
   }

   public static int index(String var0) {
      Object var1 = indexes.get(var0);
      return var1 != null ? (Integer)var1 : -1;
   }

   public String toString() {
      if (this.name == null) {
         StringBuffer var1 = new StringBuffer();
         var1.append("rgb(");
         var1.append(this.red + ", " + this.green + ", " + this.blue);
         var1.append(")");
         return var1.toString();
      } else {
         return this.name;
      }
   }

   static {
      for(int var0 = 0; var0 < list.length; ++var0) {
         indexes.put(list[var0].name, new Integer(var0));
      }

   }
}
