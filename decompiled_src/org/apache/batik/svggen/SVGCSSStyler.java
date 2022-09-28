package org.apache.batik.svggen;

import java.util.ArrayList;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SVGCSSStyler implements SVGSyntax {
   private static final char CSS_PROPERTY_VALUE_SEPARATOR = ':';
   private static final char CSS_RULE_SEPARATOR = ';';
   private static final char SPACE = ' ';

   public static void style(Node var0) {
      NamedNodeMap var1 = var0.getAttributes();
      int var4;
      if (var1 != null) {
         Element var2 = (Element)var0;
         StringBuffer var3 = new StringBuffer();
         var4 = var1.getLength();
         ArrayList var5 = new ArrayList();

         int var6;
         for(var6 = 0; var6 < var4; ++var6) {
            Attr var7 = (Attr)var1.item(var6);
            String var8 = var7.getName();
            if (SVGStylingAttributes.set.contains(var8)) {
               var3.append(var8);
               var3.append(':');
               var3.append(var7.getValue());
               var3.append(';');
               var3.append(' ');
               var5.add(var8);
            }
         }

         if (var3.length() > 0) {
            var2.setAttributeNS((String)null, "style", var3.toString().trim());
            var6 = var5.size();

            for(int var12 = 0; var12 < var6; ++var12) {
               var2.removeAttribute((String)var5.get(var12));
            }
         }
      }

      NodeList var9 = var0.getChildNodes();
      int var10 = var9.getLength();

      for(var4 = 0; var4 < var10; ++var4) {
         Node var11 = var9.item(var4);
         style(var11);
      }

   }
}
