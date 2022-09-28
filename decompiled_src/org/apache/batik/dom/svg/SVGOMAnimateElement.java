package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimateElement;

public class SVGOMAnimateElement extends SVGOMAnimationElement implements SVGAnimateElement {
   protected SVGOMAnimateElement() {
   }

   public SVGOMAnimateElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "animate";
   }

   protected Node newNode() {
      return new SVGOMAnimateElement();
   }
}
