package org.apache.xerces.impl.xs.util;

import java.util.Vector;
import org.apache.xerces.xs.StringList;

public class StringListImpl implements StringList {
   public static final StringList EMPTY_LIST = new StringList() {
      public int getLength() {
         return 0;
      }

      public boolean contains(String var1) {
         return false;
      }

      public String item(int var1) {
         return null;
      }
   };
   private String[] fArray = null;
   private int fLength = 0;
   private Vector fVector;

   public StringListImpl(Vector var1) {
      this.fVector = var1;
      this.fLength = var1 == null ? 0 : var1.size();
   }

   public StringListImpl(String[] var1, int var2) {
      this.fArray = var1;
      this.fLength = var2;
   }

   public int getLength() {
      return this.fLength;
   }

   public boolean contains(String var1) {
      if (this.fVector != null) {
         return this.fVector.contains(var1);
      } else {
         int var2;
         if (var1 == null) {
            for(var2 = 0; var2 < this.fLength; ++var2) {
               if (this.fArray[var2] == null) {
                  return true;
               }
            }
         } else {
            for(var2 = 0; var2 < this.fLength; ++var2) {
               if (var1.equals(this.fArray[var2])) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public String item(int var1) {
      if (var1 >= 0 && var1 < this.fLength) {
         return this.fVector != null ? (String)this.fVector.elementAt(var1) : this.fArray[var1];
      } else {
         return null;
      }
   }
}
