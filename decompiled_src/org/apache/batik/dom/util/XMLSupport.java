package org.apache.batik.dom.util;

import org.apache.batik.util.XMLConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class XMLSupport implements XMLConstants {
   private XMLSupport() {
   }

   public static String getXMLLang(Element var0) {
      Attr var1 = var0.getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "lang");
      if (var1 != null) {
         return var1.getNodeValue();
      } else {
         for(Node var2 = var0.getParentNode(); var2 != null; var2 = var2.getParentNode()) {
            if (var2.getNodeType() == 1) {
               var1 = ((Element)var2).getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "lang");
               if (var1 != null) {
                  return var1.getNodeValue();
               }
            }
         }

         return "en";
      }
   }

   public static String getXMLSpace(Element var0) {
      Attr var1 = var0.getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "space");
      if (var1 != null) {
         return var1.getNodeValue();
      } else {
         for(Node var2 = var0.getParentNode(); var2 != null; var2 = var2.getParentNode()) {
            if (var2.getNodeType() == 1) {
               var1 = ((Element)var2).getAttributeNodeNS("http://www.w3.org/XML/1998/namespace", "space");
               if (var1 != null) {
                  return var1.getNodeValue();
               }
            }
         }

         return "default";
      }
   }

   public static String defaultXMLSpace(String var0) {
      int var1 = var0.length();
      StringBuffer var2 = new StringBuffer(var1);
      boolean var3 = false;

      for(int var4 = 0; var4 < var1; ++var4) {
         char var5 = var0.charAt(var4);
         switch (var5) {
            case '\t':
            case ' ':
               if (!var3) {
                  var2.append(' ');
                  var3 = true;
               }
               break;
            case '\n':
            case '\r':
               var3 = false;
               break;
            default:
               var2.append(var5);
               var3 = false;
         }
      }

      return var2.toString().trim();
   }

   public static String preserveXMLSpace(String var0) {
      int var1 = var0.length();
      StringBuffer var2 = new StringBuffer(var1);

      for(int var3 = 0; var3 < var0.length(); ++var3) {
         char var4 = var0.charAt(var3);
         switch (var4) {
            case '\t':
            case '\n':
            case '\r':
               var2.append(' ');
               break;
            case '\u000b':
            case '\f':
            default:
               var2.append(var4);
         }
      }

      return var2.toString();
   }
}
