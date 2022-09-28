package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGVKernElement;

public class SVGOMVKernElement extends SVGOMElement implements SVGVKernElement {
   protected SVGOMVKernElement() {
   }

   public SVGOMVKernElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "vkern";
   }

   protected Node newNode() {
      return new SVGOMVKernElement();
   }
}
