package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGTRefElement;

public class SVGOMTRefElement extends SVGURIReferenceTextPositioningElement implements SVGTRefElement {
   protected SVGOMTRefElement() {
   }

   public SVGOMTRefElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "tref";
   }

   protected Node newNode() {
      return new SVGOMTRefElement();
   }
}
