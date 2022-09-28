package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGGlyphElement;

public class SVGOMGlyphElement extends SVGStylableElement implements SVGGlyphElement {
   protected SVGOMGlyphElement() {
   }

   public SVGOMGlyphElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "glyph";
   }

   protected Node newNode() {
      return new SVGOMGlyphElement();
   }
}
