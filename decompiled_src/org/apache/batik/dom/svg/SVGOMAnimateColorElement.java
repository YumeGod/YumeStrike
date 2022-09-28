package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGAnimateColorElement;

public class SVGOMAnimateColorElement extends SVGOMAnimationElement implements SVGAnimateColorElement {
   protected SVGOMAnimateColorElement() {
   }

   public SVGOMAnimateColorElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "animateColor";
   }

   protected Node newNode() {
      return new SVGOMAnimateColorElement();
   }
}
