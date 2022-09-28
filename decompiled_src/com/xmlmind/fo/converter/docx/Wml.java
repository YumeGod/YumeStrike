package com.xmlmind.fo.converter.docx;

import com.xmlmind.fo.properties.Color;

public final class Wml {
   public static final String NS_WORDPROCESSINGML = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";
   public static final String NS_DRAWINGML = "http://schemas.openxmlformats.org/drawingml/2006/main";
   public static final String NS_DRAWINGML_PICTURE = "http://schemas.openxmlformats.org/drawingml/2006/picture";
   public static final String NS_DRAWINGML_WPDRAWING = "http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing";
   public static final String NS_RELATIONSHIPS = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
   public static final String NS_CUSTOMXML = "http://schemas.openxmlformats.org/officeDocument/2006/customXml";
   public static final String NS_VML = "urn:schemas-microsoft-com:vml";

   public static String ucharHexNumberType(int var0) {
      return hexBinary(var0, 1);
   }

   public static int rgb(Color var0) {
      if (var0.name != null) {
         var0 = ColorTable.get(var0.name);
      }

      return (var0.red << 16) + (var0.green << 8) + var0.blue;
   }

   public static String hexColorType(Color var0) {
      return hexBinary(rgb(var0), 3);
   }

   public static String hexBinary(int var0, int var1) {
      return Integer.toHexString(var0 + (1 << 8 * var1)).substring(1);
   }

   public static String escape(String var0) {
      int var1 = var0.length();
      StringBuffer var2 = new StringBuffer(var1);

      for(int var3 = 0; var3 < var1; ++var3) {
         char var4 = var0.charAt(var3);
         switch (var4) {
            case '"':
               var2.append("&quot;");
               break;
            case '&':
               var2.append("&amp;");
               break;
            case '<':
               var2.append("&lt;");
               break;
            case '>':
               if (var3 > 1 && var0.charAt(var3 - 1) == ']' && var0.charAt(var3 - 2) == ']') {
                  var2.append("&gt;");
                  break;
               }

               var2.append(var4);
               break;
            default:
               var2.append(var4);
         }
      }

      return var2.toString();
   }
}
