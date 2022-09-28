package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGStylableElement;
import org.w3c.dom.Node;

public class SVGOMSubImageRefElement extends SVGStylableElement {
   protected SVGOMSubImageRefElement() {
   }

   public SVGOMSubImageRefElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "subImageRef";
   }

   protected Node newNode() {
      return new SVGOMSubImageRefElement();
   }
}
