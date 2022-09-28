package org.apache.batik.dom.util;

import org.apache.batik.util.XMLConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

public class XLinkSupport implements XMLConstants {
   public static String getXLinkType(Element var0) {
      return var0.getAttributeNS("http://www.w3.org/1999/xlink", "type");
   }

   public static void setXLinkType(Element var0, String var1) {
      if (!"simple".equals(var1) && !"extended".equals(var1) && !"locator".equals(var1) && !"arc".equals(var1)) {
         throw new DOMException((short)12, "xlink:type='" + var1 + "'");
      } else {
         var0.setAttributeNS("http://www.w3.org/1999/xlink", "type", var1);
      }
   }

   public static String getXLinkRole(Element var0) {
      return var0.getAttributeNS("http://www.w3.org/1999/xlink", "role");
   }

   public static void setXLinkRole(Element var0, String var1) {
      var0.setAttributeNS("http://www.w3.org/1999/xlink", "role", var1);
   }

   public static String getXLinkArcRole(Element var0) {
      return var0.getAttributeNS("http://www.w3.org/1999/xlink", "arcrole");
   }

   public static void setXLinkArcRole(Element var0, String var1) {
      var0.setAttributeNS("http://www.w3.org/1999/xlink", "arcrole", var1);
   }

   public static String getXLinkTitle(Element var0) {
      return var0.getAttributeNS("http://www.w3.org/1999/xlink", "title");
   }

   public static void setXLinkTitle(Element var0, String var1) {
      var0.setAttributeNS("http://www.w3.org/1999/xlink", "title", var1);
   }

   public static String getXLinkShow(Element var0) {
      return var0.getAttributeNS("http://www.w3.org/1999/xlink", "show");
   }

   public static void setXLinkShow(Element var0, String var1) {
      if (!"new".equals(var1) && !"replace".equals(var1) && !"embed".equals(var1)) {
         throw new DOMException((short)12, "xlink:show='" + var1 + "'");
      } else {
         var0.setAttributeNS("http://www.w3.org/1999/xlink", "show", var1);
      }
   }

   public static String getXLinkActuate(Element var0) {
      return var0.getAttributeNS("http://www.w3.org/1999/xlink", "actuate");
   }

   public static void setXLinkActuate(Element var0, String var1) {
      if (!"onReplace".equals(var1) && !"onLoad".equals(var1)) {
         throw new DOMException((short)12, "xlink:actuate='" + var1 + "'");
      } else {
         var0.setAttributeNS("http://www.w3.org/1999/xlink", "actuate", var1);
      }
   }

   public static String getXLinkHref(Element var0) {
      return var0.getAttributeNS("http://www.w3.org/1999/xlink", "href");
   }

   public static void setXLinkHref(Element var0, String var1) {
      var0.setAttributeNS("http://www.w3.org/1999/xlink", "href", var1);
   }
}
