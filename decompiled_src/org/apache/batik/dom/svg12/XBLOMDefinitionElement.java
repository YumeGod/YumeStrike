package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.Node;

public class XBLOMDefinitionElement extends XBLOMElement {
   protected XBLOMDefinitionElement() {
   }

   public XBLOMDefinitionElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "definition";
   }

   protected Node newNode() {
      return new XBLOMDefinitionElement();
   }

   public String getElementNamespaceURI() {
      String var1 = this.getAttributeNS((String)null, "element");
      String var2 = DOMUtilities.getPrefix(var1);
      String var3 = this.lookupNamespaceURI(var2);
      if (var3 == null) {
         throw this.createDOMException((short)14, "prefix", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var2});
      } else {
         return var3;
      }
   }

   public String getElementLocalName() {
      String var1 = this.getAttributeNS((String)null, "element");
      return DOMUtilities.getLocalName(var1);
   }
}
