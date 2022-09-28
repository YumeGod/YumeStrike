package com.xmlmind.fo.converter.odt;

import com.xmlmind.fo.properties.Color;
import com.xmlmind.fo.util.Encoder;
import java.text.NumberFormat;
import java.util.Locale;

public final class Odt {
   public static final String VERSION = "1.0";
   public static final String OFFICE_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:office:1.0";
   public static final String STYLE_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:style:1.0";
   public static final String FO_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0";
   public static final String SVG_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0";
   public static final String TEXT_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:text:1.0";
   public static final String DRAW_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:drawing:1.0";
   public static final String TABLE_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:table:1.0";
   public static final String XLINK_NAMESPACE_URI = "http://www.w3.org/1999/xlink";
   public static final String MANIFEST_NAMESPACE_URI = "urn:oasis:names:tc:opendocument:xmlns:manifest:1.0";
   private static NumberFormat numberFormat;

   public static synchronized String format(double var0, int var2) {
      numberFormat.setMinimumIntegerDigits(1);
      numberFormat.setMaximumFractionDigits(var2);
      return numberFormat.format(var0);
   }

   public static synchronized String format(long var0, int var2) {
      numberFormat.setMinimumIntegerDigits(var2);
      return numberFormat.format(var0);
   }

   public static String length(double var0, int var2) {
      return format(var0, var2) + "pt";
   }

   public static String relativeLength(double var0, int var2) {
      return format(var0, var2) + "*";
   }

   public static String percent(double var0, int var2) {
      return format(var0, var2) + "%";
   }

   public static int rgb(Color var0) {
      return var0 == null ? -1 : (var0.red << 16) + (var0.green << 8) + var0.blue;
   }

   public static String color(int var0) {
      return var0 == -1 ? "#000000" : "#" + Integer.toHexString(var0 + 16777216).substring(1);
   }

   public static String imageName(int var0, String var1) {
      return "Pictures/" + format((long)var0, 4) + var1;
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

   static {
      numberFormat = NumberFormat.getInstance(Locale.US);
      numberFormat.setGroupingUsed(false);
   }
}
