package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGDefinitionSrcElement;

public class SVGOMDefinitionSrcElement extends SVGOMElement implements SVGDefinitionSrcElement {
   protected SVGOMDefinitionSrcElement() {
   }

   public SVGOMDefinitionSrcElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "definition-src";
   }

   protected Node newNode() {
      return new SVGOMDefinitionSrcElement();
   }
}
