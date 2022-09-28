package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFontFaceElement;

public class SVGOMFontFaceElement extends SVGOMElement implements SVGFontFaceElement {
   protected SVGOMFontFaceElement() {
   }

   public SVGOMFontFaceElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "font-face";
   }

   protected Node newNode() {
      return new SVGOMFontFaceElement();
   }
}
