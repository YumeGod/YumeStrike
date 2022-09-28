package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;

public class XBLOMTemplateElement extends XBLOMElement {
   protected XBLOMTemplateElement() {
   }

   public XBLOMTemplateElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "template";
   }

   protected Node newNode() {
      return new XBLOMTemplateElement();
   }
}
