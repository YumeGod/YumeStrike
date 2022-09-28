package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFEFuncRElement;

public class SVGOMFEFuncRElement extends SVGOMComponentTransferFunctionElement implements SVGFEFuncRElement {
   protected SVGOMFEFuncRElement() {
   }

   public SVGOMFEFuncRElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "feFuncR";
   }

   protected Node newNode() {
      return new SVGOMFEFuncRElement();
   }
}
