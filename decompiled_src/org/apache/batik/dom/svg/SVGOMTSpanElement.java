package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGTSpanElement;

public class SVGOMTSpanElement extends SVGOMTextPositioningElement implements SVGTSpanElement {
   protected SVGOMTSpanElement() {
   }

   public SVGOMTSpanElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "tspan";
   }

   protected Node newNode() {
      return new SVGOMTSpanElement();
   }
}
