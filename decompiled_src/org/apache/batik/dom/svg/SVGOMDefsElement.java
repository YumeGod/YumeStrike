package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDefsElement;

public class SVGOMDefsElement extends SVGGraphicsElement implements SVGDefsElement {
   protected SVGOMDefsElement() {
   }

   public SVGOMDefsElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "defs";
   }

   protected Node newNode() {
      return new SVGOMDefsElement();
   }
}
