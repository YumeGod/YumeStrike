package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGSwitchElement;

public class SVGOMSwitchElement extends SVGGraphicsElement implements SVGSwitchElement {
   protected SVGOMSwitchElement() {
   }

   public SVGOMSwitchElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "switch";
   }

   protected Node newNode() {
      return new SVGOMSwitchElement();
   }
}
