package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGPolygonElement;

public class SVGOMPolygonElement extends SVGPointShapeElement implements SVGPolygonElement {
   protected SVGOMPolygonElement() {
   }

   public SVGOMPolygonElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "polygon";
   }

   protected Node newNode() {
      return new SVGOMPolygonElement();
   }
}
