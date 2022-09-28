package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGFEFuncAElement;

public class SVGOMFEFuncAElement extends SVGOMComponentTransferFunctionElement implements SVGFEFuncAElement {
   protected SVGOMFEFuncAElement() {
   }

   public SVGOMFEFuncAElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "feFuncA";
   }

   protected Node newNode() {
      return new SVGOMFEFuncAElement();
   }
}
