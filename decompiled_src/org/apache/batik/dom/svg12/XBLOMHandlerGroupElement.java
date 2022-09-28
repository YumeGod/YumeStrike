package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;

public class XBLOMHandlerGroupElement extends XBLOMElement {
   protected XBLOMHandlerGroupElement() {
   }

   public XBLOMHandlerGroupElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "handlerGroup";
   }

   protected Node newNode() {
      return new XBLOMHandlerGroupElement();
   }
}
