package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFEFuncGElement;

public class SVGOMFEFuncGElement extends SVGOMComponentTransferFunctionElement implements SVGFEFuncGElement {
   protected SVGOMFEFuncGElement() {
   }

   public SVGOMFEFuncGElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "feFuncG";
   }

   protected Node newNode() {
      return new SVGOMFEFuncGElement();
   }
}
