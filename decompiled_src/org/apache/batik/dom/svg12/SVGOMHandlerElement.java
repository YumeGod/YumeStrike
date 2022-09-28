package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMElement;
import org.w3c.dom.Node;

public class SVGOMHandlerElement extends SVGOMElement {
   protected SVGOMHandlerElement() {
   }

   public SVGOMHandlerElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "handler";
   }

   protected Node newNode() {
      return new SVGOMHandlerElement();
   }
}
