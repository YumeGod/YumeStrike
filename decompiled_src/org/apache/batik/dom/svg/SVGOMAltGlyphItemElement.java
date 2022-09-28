package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAltGlyphItemElement;

public class SVGOMAltGlyphItemElement extends SVGOMElement implements SVGAltGlyphItemElement {
   protected SVGOMAltGlyphItemElement() {
   }

   public SVGOMAltGlyphItemElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "altGlyphItem";
   }

   protected Node newNode() {
      return new SVGOMAltGlyphItemElement();
   }
}
