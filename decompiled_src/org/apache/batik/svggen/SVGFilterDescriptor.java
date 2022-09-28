package org.apache.batik.svggen;

import org.w3c.dom.Element;

public class SVGFilterDescriptor {
   private Element def;
   private String filterValue;

   public SVGFilterDescriptor(String var1) {
      this.filterValue = var1;
   }

   public SVGFilterDescriptor(String var1, Element var2) {
      this(var1);
      this.def = var2;
   }

   public String getFilterValue() {
      return this.filterValue;
   }

   public Element getDef() {
      return this.def;
   }
}
