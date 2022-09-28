package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFontFaceUriElement;

public class SVGOMFontFaceUriElement extends SVGOMElement implements SVGFontFaceUriElement {
   protected SVGOMFontFaceUriElement() {
   }

   public SVGOMFontFaceUriElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "font-face-uri";
   }

   protected Node newNode() {
      return new SVGOMFontFaceUriElement();
   }
}
