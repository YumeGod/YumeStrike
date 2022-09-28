package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFontFaceFormatElement;

public class SVGOMFontFaceFormatElement extends SVGOMElement implements SVGFontFaceFormatElement {
   protected SVGOMFontFaceFormatElement() {
   }

   public SVGOMFontFaceFormatElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "font-face-format";
   }

   protected Node newNode() {
      return new SVGOMFontFaceFormatElement();
   }
}
