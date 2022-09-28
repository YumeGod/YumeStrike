package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.IdContainer;
import org.apache.batik.dom.xbl.XBLShadowTreeElement;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XBLOMShadowTreeElement extends XBLOMElement implements XBLShadowTreeElement, IdContainer {
   protected XBLOMShadowTreeElement() {
   }

   public XBLOMShadowTreeElement(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public String getLocalName() {
      return "shadowTree";
   }

   protected Node newNode() {
      return new XBLOMShadowTreeElement();
   }

   public Element getElementById(String var1) {
      return this.getElementById(var1, this);
   }

   protected Element getElementById(String var1, Node var2) {
      if (var2.getNodeType() == 1) {
         Element var3 = (Element)var2;
         if (var3.getAttributeNS((String)null, "id").equals(var1)) {
            return (Element)var2;
         }
      }

      for(Node var5 = var2.getFirstChild(); var5 != null; var5 = var5.getNextSibling()) {
         Element var4 = this.getElementById(var1, var5);
         if (var4 != null) {
            return var4;
         }
      }

      return null;
   }

   public Node getCSSParentNode() {
      return this.ownerDocument.getXBLManager().getXblBoundElement(this);
   }
}
