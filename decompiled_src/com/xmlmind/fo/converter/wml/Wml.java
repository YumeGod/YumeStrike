package com.xmlmind.fo.converter.wml;

import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.util.Encoder;

public final class Wml {
   public static final int CS_ANSI = 0;
   public static final int CS_SYMBOL = 2;
   public static final int CS_RUSSIAN = 204;
   public static final int CS_EASTERN_EUROPEAN = 238;
   public static final int UNIT_MILLIMETER = 1;
   public static final int UNIT_CENTIMETER = 2;
   public static final int UNIT_INCH = 3;
   public static final int UNIT_POINT = 4;

   public static int toTwips(double var0, int var2) {
      double var3;
      switch (var2) {
         case 1:
            var3 = var0 / 25.4 * 72.0;
            break;
         case 2:
            var3 = var0 / 2.54 * 72.0;
            break;
         case 3:
            var3 = var0 * 72.0;
            break;
         case 4:
         default:
            var3 = var0;
      }

      return toTwips(var3);
   }

   public static int toTwips(double var0) {
      return (int)Math.rint(20.0 * var0);
   }

   public static int toEighths(double var0) {
      return (int)Math.rint(8.0 * var0);
   }

   public static String hexNumberType(int var0, int var1) {
      return Integer.toHexString(var0 + (1 << var1)).substring(1);
   }

   public static String hexColorType(Color var0) {
      if (var0.name != null) {
         var0 = ColorTable.get(var0.name);
      }

      int var1 = (var0.red << 16) + (var0.green << 8) + var0.blue;
      return hexNumberType(var1, 24);
   }

   public static String escape(char var0) {
      switch (var0) {
         case '"':
            return "&quot;";
         case '&':
            return "&amp;";
         case '\'':
            return "&apos;";
         case '<':
            return "&lt;";
         case '>':
            return "&gt;";
         default:
            return new String(new char[]{var0});
      }
   }

   public static String escape(String var0) {
      return escape(var0, (Encoder)null);
   }

   public static String escape(String var0, Encoder var1) {
      int var2 = var0.length();
      StringBuffer var3 = new StringBuffer(var2);
      int var4;
      char var5;
      if (var1 != null) {
         for(var4 = 0; var4 < var2; ++var4) {
            var5 = var0.charAt(var4);
            switch (var5) {
               case '"':
                  var3.append("&quot;");
                  break;
               case '&':
                  var3.append("&amp;");
                  break;
               case '<':
                  var3.append("&lt;");
                  break;
               case '>':
                  if (var4 > 1 && var0.charAt(var4 - 1) == ']' && var0.charAt(var4 - 2) == ']') {
                     var3.append("&gt;");
                     break;
                  }

                  var3.append(var5);
                  break;
               default:
                  if (!var1.canEncode(var5)) {
                     var3.append("&#" + Integer.toString(var5) + ";");
                  } else {
                     var3.append(var5);
                  }
            }
         }
      } else {
         for(var4 = 0; var4 < var2; ++var4) {
            var5 = var0.charAt(var4);
            switch (var5) {
               case '"':
                  var3.append("&quot;");
                  break;
               case '&':
                  var3.append("&amp;");
                  break;
               case '<':
                  var3.append("&lt;");
                  break;
               case '>':
                  if (var4 > 1 && var0.charAt(var4 - 1) == ']' && var0.charAt(var4 - 2) == ']') {
                     var3.append("&gt;");
                     break;
                  }

                  var3.append(var5);
                  break;
               default:
                  var3.append(var5);
            }
         }
      }

      return var3.toString();
   }
}
