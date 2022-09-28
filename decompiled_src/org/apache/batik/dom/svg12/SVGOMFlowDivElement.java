package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMTextContentElement;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGTextContentElement;

public class SVGOMFlowDivElement extends SVGOMTextContentElement implements SVGTextContentElement {
   protected SVGOMFlowDivElement() {
   }

   public SVGOMFlowDivElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "flowDiv";
   }

   protected Node newNode() {
      return new SVGOMFlowDivElement();
   }
}
