package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMTextPositioningElement;
import org.w3c.dom.Node;

public class SVGOMFlowRegionBreakElement extends SVGOMTextPositioningElement {
   protected SVGOMFlowRegionBreakElement() {
   }

   public SVGOMFlowRegionBreakElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "flowRegionBreak";
   }

   protected Node newNode() {
      return new SVGOMFlowRegionBreakElement();
   }
}
