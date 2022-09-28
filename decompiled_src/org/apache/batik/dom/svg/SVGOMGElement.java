package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGGElement;

public class SVGOMGElement extends SVGGraphicsElement implements SVGGElement {
   protected SVGOMGElement() {
   }

   public SVGOMGElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "g";
   }

   protected Node newNode() {
      return new SVGOMGElement();
   }
}
