package org.apache.batik.dom.svg12;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.svg.SVGOMElement;
import org.apache.batik.dom.util.DOMUtilities;
import org.apache.batik.util.XBLConstants;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract class XBLOMElement extends SVGOMElement implements XBLConstants {
   protected String prefix;

   protected XBLOMElement() {
   }

   protected XBLOMElement(String var1, AbstractDocument var2) {
      this.ownerDocument = var2;
      this.setPrefix(var1);
   }

   public String getNodeName() {
      return this.prefix != null && !this.prefix.equals("") ? this.prefix + ':' + this.getLocalName() : this.getLocalName();
   }

   public String getNamespaceURI() {
      return "http://www.w3.org/2004/xbl";
   }

   public void setPrefix(String var1) throws DOMException {
      if (this.isReadonly()) {
         throw this.createDOMException((short)7, "readonly.node", new Object[]{new Integer(this.getNodeType()), this.getNodeName()});
      } else if (var1 != null && !var1.equals("") && !DOMUtilities.isValidName(var1)) {
         throw this.createDOMException((short)5, "prefix", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var1});
      } else {
         this.prefix = var1;
      }
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      XBLOMElement var3 = (XBLOMElement)var1;
      var3.prefix = this.prefix;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      XBLOMElement var3 = (XBLOMElement)var1;
      var3.prefix = this.prefix;
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      XBLOMElement var2 = (XBLOMElement)var1;
      var2.prefix = this.prefix;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      XBLOMElement var2 = (XBLOMElement)var1;
      var2.prefix = this.prefix;
      return var1;
   }
}
