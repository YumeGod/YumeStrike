package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFontFaceSrcElement;

public class SVGOMFontFaceSrcElement extends SVGOMElement implements SVGFontFaceSrcElement {
   protected SVGOMFontFaceSrcElement() {
   }

   public SVGOMFontFaceSrcElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "font-face-src";
   }

   protected Node newNode() {
      return new SVGOMFontFaceSrcElement();
   }
}
