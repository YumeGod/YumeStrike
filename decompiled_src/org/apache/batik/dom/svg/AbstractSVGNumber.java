package org.apache.batik.dom.svg;

import org.w3c.dom.svg.SVGNumber;

public abstract class AbstractSVGNumber implements SVGNumber {
   protected float value;

   public float getValue() {
      return this.value;
   }

   public void setValue(float var1) {
      this.value = var1;
   }
}
