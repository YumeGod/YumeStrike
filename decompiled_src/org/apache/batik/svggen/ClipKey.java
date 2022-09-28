package org.apache.batik.svggen;

import java.awt.geom.GeneralPath;

class ClipKey {
   int hashCodeValue = 0;

   public ClipKey(GeneralPath var1, SVGGeneratorContext var2) {
      String var3 = SVGPath.toSVGPathData(var1, var2);
      this.hashCodeValue = var3.hashCode();
   }

   public int hashCode() {
      return this.hashCodeValue;
   }

   public boolean equals(Object var1) {
      return var1 instanceof ClipKey && this.hashCodeValue == ((ClipKey)var1).hashCodeValue;
   }
}
