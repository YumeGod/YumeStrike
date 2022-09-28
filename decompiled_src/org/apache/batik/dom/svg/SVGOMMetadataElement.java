package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGMetadataElement;

public class SVGOMMetadataElement extends SVGOMElement implements SVGMetadataElement {
   protected SVGOMMetadataElement() {
   }

   public SVGOMMetadataElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "metadata";
   }

   protected Node newNode() {
      return new SVGOMMetadataElement();
   }
}
