package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class NamedNodeMapImpl implements NamedNodeMap {
   Attr[] attrs;

   public NamedNodeMapImpl(Attr[] var1) {
      this.attrs = var1;
   }

   public Node getNamedItem(String var1) {
      for(int var2 = 0; var2 < this.attrs.length; ++var2) {
         if (this.attrs[var2].getName().equals(var1)) {
            return this.attrs[var2];
         }
      }

      return null;
   }

   public Node item(int var1) {
      return var1 < 0 && var1 > this.getLength() ? null : this.attrs[var1];
   }

   public int getLength() {
      return this.attrs.length;
   }

   public Node getNamedItemNS(String var1, String var2) {
      for(int var3 = 0; var3 < this.attrs.length; ++var3) {
         if (this.attrs[var3].getName().equals(var2) && this.attrs[var3].getNamespaceURI().equals(var1)) {
            return this.attrs[var3];
         }
      }

      return null;
   }

   public Node setNamedItemNS(Node var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Node setNamedItem(Node var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Node removeNamedItem(String var1) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }

   public Node removeNamedItemNS(String var1, String var2) throws DOMException {
      throw new DOMException((short)9, "Method not supported");
   }
}
