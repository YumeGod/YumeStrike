package org.apache.xerces.impl.xs.util;

import org.apache.xerces.xs.ShortList;
import org.apache.xerces.xs.XSException;

public class ShortListImpl implements ShortList {
   private short[] fArray = null;
   private int fLength = 0;

   public ShortListImpl(short[] var1, int var2) {
      this.fArray = var1;
      this.fLength = var2;
   }

   public int getLength() {
      return this.fLength;
   }

   public boolean contains(short var1) {
      for(int var2 = 0; var2 < this.fLength; ++var2) {
         if (this.fArray[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   public short item(int var1) throws XSException {
      if (var1 >= 0 && var1 < this.fLength) {
         return this.fArray[var1];
      } else {
         throw new XSException((short)2, (String)null);
      }
   }

   public boolean equals(Object var1) {
      if (var1 != null && var1 instanceof ShortList) {
         ShortList var2 = (ShortList)var1;
         if (this.fLength != var2.getLength()) {
            return false;
         } else {
            for(int var3 = 0; var3 < this.fLength; ++var3) {
               if (this.fArray[var3] != var2.item(var3)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }
}
