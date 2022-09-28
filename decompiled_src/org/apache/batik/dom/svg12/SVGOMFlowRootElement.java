package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGGraphicsElement;
import org.w3c.dom.Node;

public class SVGOMFlowRootElement extends SVGGraphicsElement {
   protected SVGOMFlowRootElement() {
   }

   public SVGOMFlowRootElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "flowRoot";
   }

   protected Node newNode() {
      return new SVGOMFlowRootElement();
   }
}
