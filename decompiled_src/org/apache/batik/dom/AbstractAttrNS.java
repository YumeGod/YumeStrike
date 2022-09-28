package org.apache.batik.dom;

import org.apache.batik.dom.util.DOMUtilities;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract class AbstractAttrNS extends AbstractAttr {
   protected String namespaceURI;

   protected AbstractAttrNS() {
   }

   protected AbstractAttrNS(String var1, String var2, AbstractDocument var3) throws DOMException {
      super(var2, var3);
      if (var1 != null && var1.length() == 0) {
         var1 = null;
      }

      this.namespaceURI = var1;
      String var4 = DOMUtilities.getPrefix(var2);
      if (var3.getStrictErrorChecking()) {
         if (var4 == null) {
            if ("xmlns".equals(var2) && !"http://www.w3.org/2000/xmlns/".equals(var1)) {
               throw this.createDOMException((short)14, "namespace.uri", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var1});
            }
         } else if (var1 == null || "xml".equals(var4) && !"http://www.w3.org/XML/1998/namespace".equals(var1) || "xmlns".equals(var4) && !"http://www.w3.org/2000/xmlns/".equals(var1)) {
            throw this.createDOMException((short)14, "namespace.uri", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), var1});
         }

      }
   }

   public String getNamespaceURI() {
      return this.namespaceURI;
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      AbstractAttrNS var3 = (AbstractAttrNS)var1;
      var3.namespaceURI = this.namespaceURI;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      AbstractAttrNS var3 = (AbstractAttrNS)var1;
      var3.namespaceURI = this.namespaceURI;
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      AbstractAttrNS var2 = (AbstractAttrNS)var1;
      var2.namespaceURI = this.namespaceURI;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      AbstractAttrNS var2 = (AbstractAttrNS)var1;
      var2.namespaceURI = this.namespaceURI;
      return var1;
   }
}
