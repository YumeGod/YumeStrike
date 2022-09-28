package org.apache.batik.svggen;

import java.util.HashMap;
import java.util.Map;

public class SVGIDGenerator {
   private Map prefixMap = new HashMap();

   public String generateID(String var1) {
      Integer var2 = (Integer)this.prefixMap.get(var1);
      if (var2 == null) {
         var2 = new Integer(0);
         this.prefixMap.put(var1, var2);
      }

      var2 = new Integer(var2 + 1);
      this.prefixMap.put(var1, var2);
      return var1 + var2;
   }
}
