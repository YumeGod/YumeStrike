package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Element;

public class DefaultStyleHandler implements StyleHandler, SVGConstants {
   static Map ignoreAttributes = new HashMap();

   public void setStyle(Element var1, Map var2, SVGGeneratorContext var3) {
      String var4 = var1.getTagName();
      Iterator var5 = var2.keySet().iterator();

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         if (var1.getAttributeNS((String)null, var6).length() == 0 && this.appliesTo(var6, var4)) {
            var1.setAttributeNS((String)null, var6, (String)var2.get(var6));
         }
      }

   }

   protected boolean appliesTo(String var1, String var2) {
      Set var3 = (Set)ignoreAttributes.get(var2);
      if (var3 == null) {
         return true;
      } else {
         return !var3.contains(var1);
      }
   }

   static {
      HashSet var0 = new HashSet();
      var0.add("font-size");
      var0.add("font-family");
      var0.add("font-style");
      var0.add("font-weight");
      ignoreAttributes.put("rect", var0);
      ignoreAttributes.put("circle", var0);
      ignoreAttributes.put("ellipse", var0);
      ignoreAttributes.put("polygon", var0);
      ignoreAttributes.put("polygon", var0);
      ignoreAttributes.put("line", var0);
      ignoreAttributes.put("path", var0);
   }
}
