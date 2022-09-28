package org.apache.batik.svggen;

import java.util.HashSet;
import java.util.Set;

public class SVGAttribute {
   private String name;
   private Set applicabilitySet;
   private boolean isSetInclusive;

   public SVGAttribute(Set var1, boolean var2) {
      if (var1 == null) {
         var1 = new HashSet();
      }

      this.applicabilitySet = (Set)var1;
      this.isSetInclusive = var2;
   }

   public boolean appliesTo(String var1) {
      boolean var2 = this.applicabilitySet.contains(var1);
      if (this.isSetInclusive) {
         return var2;
      } else {
         return !var2;
      }
   }
}
