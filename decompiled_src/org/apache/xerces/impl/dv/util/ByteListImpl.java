package org.apache.xerces.impl.dv.util;

import org.apache.xerces.xs.XSException;
import org.apache.xerces.xs.datatypes.ByteList;

public class ByteListImpl implements ByteList {
   protected final byte[] data;
   protected String canonical;

   public ByteListImpl(byte[] var1) {
      this.data = var1;
   }

   public int getLength() {
      return this.data.length;
   }

   public boolean contains(byte var1) {
      for(int var2 = 0; var2 < this.data.length; ++var2) {
         if (this.data[var2] == var1) {
            return true;
         }
      }

      return false;
   }

   public byte item(int var1) throws XSException {
      if (var1 >= 0 && var1 <= this.data.length - 1) {
         return this.data[var1];
      } else {
         throw new XSException((short)2, (String)null);
      }
   }
}
