package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFontFaceNameElement;

public class SVGOMFontFaceNameElement extends SVGOMElement implements SVGFontFaceNameElement {
   protected SVGOMFontFaceNameElement() {
   }

   public SVGOMFontFaceNameElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "font-face-name";
   }

   protected Node newNode() {
      return new SVGOMFontFaceNameElement();
   }
}
