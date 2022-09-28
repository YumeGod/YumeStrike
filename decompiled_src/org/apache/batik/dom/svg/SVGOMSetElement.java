package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGSetElement;

public class SVGOMSetElement extends SVGOMAnimationElement implements SVGSetElement {
   protected SVGOMSetElement() {
   }

   public SVGOMSetElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "set";
   }

   protected Node newNode() {
      return new SVGOMSetElement();
   }
}
