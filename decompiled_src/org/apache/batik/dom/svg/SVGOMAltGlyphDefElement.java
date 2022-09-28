package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAltGlyphDefElement;

public class SVGOMAltGlyphDefElement extends SVGOMElement implements SVGAltGlyphDefElement {
   protected SVGOMAltGlyphDefElement() {
   }

   public SVGOMAltGlyphDefElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "altGlyphDef";
   }

   protected Node newNode() {
      return new SVGOMAltGlyphDefElement();
   }
}
