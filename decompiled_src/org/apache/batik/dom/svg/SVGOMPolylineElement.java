package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGPolylineElement;

public class SVGOMPolylineElement extends SVGPointShapeElement implements SVGPolylineElement {
   protected SVGOMPolylineElement() {
   }

   public SVGOMPolylineElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "polyline";
   }

   protected Node newNode() {
      return new SVGOMPolylineElement();
   }
}
