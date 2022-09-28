package org.apache.batik.dom.svg;

import java.awt.geom.AffineTransform;

public class SVGOMMatrix extends AbstractSVGMatrix {
   protected AffineTransform affineTransform;

   public SVGOMMatrix(AffineTransform var1) {
      this.affineTransform = var1;
   }

   protected AffineTransform getAffineTransform() {
      return this.affineTransform;
   }
}
