package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGStylableElement;
import org.w3c.dom.Node;

public class SVGOMMultiImageElement extends SVGStylableElement {
   protected SVGOMMultiImageElement() {
   }

   public SVGOMMultiImageElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "multiImage";
   }

   protected Node newNode() {
      return new SVGOMMultiImageElement();
   }
}
