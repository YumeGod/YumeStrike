package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGMissingGlyphElement;

public class SVGOMMissingGlyphElement extends SVGStylableElement implements SVGMissingGlyphElement {
   protected SVGOMMissingGlyphElement() {
   }

   public SVGOMMissingGlyphElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "missing-glyph";
   }

   protected Node newNode() {
      return new SVGOMMissingGlyphElement();
   }
}
