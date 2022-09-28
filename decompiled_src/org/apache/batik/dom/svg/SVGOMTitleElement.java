package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGTitleElement;

public class SVGOMTitleElement extends SVGDescriptiveElement implements SVGTitleElement {
   protected SVGOMTitleElement() {
   }

   public SVGOMTitleElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "title";
   }

   protected Node newNode() {
      return new SVGOMTitleElement();
   }
}
