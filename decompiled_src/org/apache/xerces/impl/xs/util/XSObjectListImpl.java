package org.apache.xerces.impl.xs.util;

import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;

public class XSObjectListImpl implements XSObjectList {
   public static final XSObjectList EMPTY_LIST = new XSObjectList() {
      public int getLength() {
         return 0;
      }

      public XSObject item(int var1) {
         return null;
      }
   };
   private static final int DEFAULT_SIZE = 4;
   private XSObject[] fArray = null;
   private int fLength = 0;

   public XSObjectListImpl() {
      this.fArray = new XSObject[4];
      this.fLength = 0;
   }

   public XSObjectListImpl(XSObject[] var1, int var2) {
      this.fArray = var1;
      this.fLength = var2;
   }

   public int getLength() {
      return this.fLength;
   }

   public XSObject item(int var1) {
      return var1 >= 0 && var1 < this.fLength ? this.fArray[var1] : null;
   }

   public void clear() {
      for(int var1 = 0; var1 < this.fLength; ++var1) {
         this.fArray[var1] = null;
      }

      this.fArray = null;
      this.fLength = 0;
   }

   public void add(XSObject var1) {
      if (this.fLength == this.fArray.length) {
         XSObject[] var2 = new XSObject[this.fLength + 4];
         System.arraycopy(this.fArray, 0, var2, 0, this.fLength);
         this.fArray = var2;
      }

      this.fArray[this.fLength++] = var1;
   }

   public void add(int var1, XSObject var2) {
      this.fArray[var1] = var2;
   }
}
