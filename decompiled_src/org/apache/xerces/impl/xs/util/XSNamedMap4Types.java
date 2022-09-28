package org.apache.xerces.impl.xs.util;

import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSTypeDefinition;

public class XSNamedMap4Types extends XSNamedMapImpl {
   short fType;

   public XSNamedMap4Types(String var1, SymbolHash var2, short var3) {
      super(var1, var2);
      this.fType = var3;
   }

   public XSNamedMap4Types(String[] var1, SymbolHash[] var2, int var3, short var4) {
      super(var1, var2, var3);
      this.fType = var4;
   }

   public synchronized int getLength() {
      if (super.fLength == -1) {
         int var1 = 0;

         for(int var2 = 0; var2 < super.fNSNum; ++var2) {
            var1 += super.fMaps[var2].getLength();
         }

         int var3 = 0;
         XSObject[] var4 = new XSObject[var1];

         for(int var5 = 0; var5 < super.fNSNum; ++var5) {
            var3 += super.fMaps[var5].getValues(var4, var3);
         }

         super.fLength = 0;
         super.fArray = new XSObject[var1];

         for(int var7 = 0; var7 < var1; ++var7) {
            XSTypeDefinition var6 = (XSTypeDefinition)var4[var7];
            if (var6.getTypeCategory() == this.fType) {
               super.fArray[super.fLength++] = var6;
            }
         }
      }

      return super.fLength;
   }

   public XSObject itemByName(String var1, String var2) {
      if (var1 != null) {
         var1 = var1.intern();
      }

      for(int var3 = 0; var3 < super.fNSNum; ++var3) {
         if (var1 == super.fNamespaces[var3]) {
            XSTypeDefinition var4 = (XSTypeDefinition)super.fMaps[var3].get(var2);
            if (var4.getTypeCategory() == this.fType) {
               return var4;
            }

            return null;
         }
      }

      return null;
   }

   public synchronized XSObject item(int var1) {
      if (super.fArray == null) {
         this.getLength();
      }

      return var1 >= 0 && var1 < super.fLength ? super.fArray[var1] : null;
   }
}
