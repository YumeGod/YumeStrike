package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFEFuncBElement;

public class SVGOMFEFuncBElement extends SVGOMComponentTransferFunctionElement implements SVGFEFuncBElement {
   protected SVGOMFEFuncBElement() {
   }

   public SVGOMFEFuncBElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "feFuncB";
   }

   protected Node newNode() {
      return new SVGOMFEFuncBElement();
   }
}
