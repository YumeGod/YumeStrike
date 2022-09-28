package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;

public class XBLOMContentElement extends XBLOMElement {
   protected XBLOMContentElement() {
   }

   public XBLOMContentElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "content";
   }

   protected Node newNode() {
      return new XBLOMContentElement();
   }
}
