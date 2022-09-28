package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGHKernElement;

public class SVGOMHKernElement extends SVGOMElement implements SVGHKernElement {
   protected SVGOMHKernElement() {
   }

   public SVGOMHKernElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "hkern";
   }

   protected Node newNode() {
      return new SVGOMHKernElement();
   }
}
