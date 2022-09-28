package org.apache.xerces.impl.xs.util;

import org.apache.xerces.util.SymbolHash;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;

public class XSNamedMapImpl implements XSNamedMap {
   public static final XSNamedMap EMPTY_MAP = new XSNamedMap() {
      public int getLength() {
         return 0;
      }

      public XSObject itemByName(String var1, String var2) {
         return null;
      }

      public XSObject item(int var1) {
         return null;
      }
   };
   String[] fNamespaces;
   int fNSNum;
   SymbolHash[] fMaps;
   XSObject[] fArray = null;
   int fLength = -1;
   QName fName = new QName();

   public XSNamedMapImpl(String var1, SymbolHash var2) {
      this.fNamespaces = new String[]{var1};
      this.fMaps = new SymbolHash[]{var2};
      this.fNSNum = 1;
   }

   public XSNamedMapImpl(String[] var1, SymbolHash[] var2, int var3) {
      this.fNamespaces = var1;
      this.fMaps = var2;
      this.fNSNum = var3;
   }

   public XSNamedMapImpl(XSObject[] var1, int var2) {
      if (var2 == 0) {
         this.fNSNum = 0;
         this.fLength = 0;
      } else {
         this.fNamespaces = new String[]{var1[0].getNamespace()};
         this.fMaps = null;
         this.fNSNum = 1;
         this.fArray = var1;
         this.fLength = var2;
      }
   }

   public synchronized int getLength() {
      if (this.fLength == -1) {
         this.fLength = 0;

         for(int var1 = 0; var1 < this.fNSNum; ++var1) {
            this.fLength += this.fMaps[var1].getLength();
         }
      }

      return this.fLength;
   }

   public XSObject itemByName(String var1, String var2) {
      if (var1 != null) {
         var1 = var1.intern();
      }

      for(int var3 = 0; var3 < this.fNSNum; ++var3) {
         if (var1 == this.fNamespaces[var3]) {
            if (this.fMaps != null) {
               return (XSObject)this.fMaps[var3].get(var2);
            }

            for(int var5 = 0; var5 < this.fLength; ++var5) {
               XSObject var4 = this.fArray[var5];
               if (var4.getName().equals(var2)) {
                  return var4;
               }
            }

            return null;
         }
      }

      return null;
   }

   public synchronized XSObject item(int var1) {
      if (this.fArray == null) {
         this.getLength();
         this.fArray = new XSObject[this.fLength];
         int var2 = 0;

         for(int var3 = 0; var3 < this.fNSNum; ++var3) {
            var2 += this.fMaps[var3].getValues(this.fArray, var2);
         }
      }

      return var1 >= 0 && var1 < this.fLength ? this.fArray[var1] : null;
   }
}
