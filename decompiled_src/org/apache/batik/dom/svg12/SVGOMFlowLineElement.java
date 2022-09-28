package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMTextPositioningElement;
import org.w3c.dom.Node;

public class SVGOMFlowLineElement extends SVGOMTextPositioningElement {
   protected SVGOMFlowLineElement() {
   }

   public SVGOMFlowLineElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "flowLine";
   }

   protected Node newNode() {
      return new SVGOMFlowLineElement();
   }
}
