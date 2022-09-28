package org.apache.xerces.impl.xs.util;

import java.util.Vector;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNamespaceItemList;

public class NSItemListImpl implements XSNamespaceItemList {
   private XSNamespaceItem[] fArray = null;
   private int fLength = 0;
   private Vector fVector;

   public NSItemListImpl(Vector var1) {
      this.fVector = var1;
      this.fLength = var1.size();
   }

   public NSItemListImpl(XSNamespaceItem[] var1, int var2) {
      this.fArray = var1;
      this.fLength = var2;
   }

   public int getLength() {
      return this.fLength;
   }

   public XSNamespaceItem item(int var1) {
      if (var1 >= 0 && var1 < this.fLength) {
         return this.fVector != null ? (XSNamespaceItem)this.fVector.elementAt(var1) : this.fArray[var1];
      } else {
         return null;
      }
   }
}
