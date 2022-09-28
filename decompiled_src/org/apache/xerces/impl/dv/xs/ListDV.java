package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.xs.datatypes.ObjectList;

public class ListDV extends TypeValidator {
   public short getAllowedFacets() {
      return 2079;
   }

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      return var1;
   }

   public int getDataLength(Object var1) {
      return ((ListData)var1).getLength();
   }

   static final class ListData implements ObjectList {
      final Object[] data;
      private String canonical;

      public ListData(Object[] var1) {
         this.data = var1;
      }

      public synchronized String toString() {
         if (this.canonical == null) {
            int var1 = this.data.length;
            StringBuffer var2 = new StringBuffer();
            if (var1 > 0) {
               var2.append(this.data[0].toString());
            }

            for(int var3 = 1; var3 < var1; ++var3) {
               var2.append(' ');
               var2.append(this.data[var3].toString());
            }

            this.canonical = var2.toString();
         }

         return this.canonical;
      }

      public int getLength() {
         return this.data.length;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof ListData)) {
            return false;
         } else {
            Object[] var2 = ((ListData)var1).data;
            int var3 = this.data.length;
            if (var3 != var2.length) {
               return false;
            } else {
               for(int var4 = 0; var4 < var3; ++var4) {
                  if (!this.data[var4].equals(var2[var4])) {
                     return false;
                  }
               }

               return true;
            }
         }
      }

      public boolean contains(Object var1) {
         for(int var2 = 0; var2 < this.data.length; ++var2) {
            if (var1 == this.data[var2]) {
               return true;
            }
         }

         return false;
      }

      public Object item(int var1) {
         return var1 >= 0 && var1 < this.data.length ? this.data[var1] : null;
      }
   }
}
