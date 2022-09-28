package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.w3c.dom.Node;

public class XBLOMImportElement extends XBLOMElement {
   protected XBLOMImportElement() {
   }

   public XBLOMImportElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "import";
   }

   protected Node newNode() {
      return new XBLOMImportElement();
   }
}
