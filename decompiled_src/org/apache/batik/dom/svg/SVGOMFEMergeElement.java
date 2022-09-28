package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFEMergeElement;

public class SVGOMFEMergeElement extends SVGOMFilterPrimitiveStandardAttributes implements SVGFEMergeElement {
   protected SVGOMFEMergeElement() {
   }

   public SVGOMFEMergeElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "feMerge";
   }

   protected Node newNode() {
      return new SVGOMFEMergeElement();
   }
}
