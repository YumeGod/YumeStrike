package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.xni.QName;

public class MixedContentModel implements ContentModelValidator {
   private int fCount;
   private QName[] fChildren;
   private int[] fChildrenType;
   private boolean fOrdered;

   public MixedContentModel(QName[] var1, int[] var2, int var3, int var4, boolean var5) {
      this.fCount = var4;
      this.fChildren = new QName[this.fCount];
      this.fChildrenType = new int[this.fCount];

      for(int var6 = 0; var6 < this.fCount; ++var6) {
         this.fChildren[var6] = new QName(var1[var3 + var6]);
         this.fChildrenType[var6] = var2[var3 + var6];
      }

      this.fOrdered = var5;
   }

   public int validate(QName[] var1, int var2, int var3) {
      int var4;
      int var7;
      String var8;
      if (this.fOrdered) {
         var4 = 0;

         for(int var5 = 0; var5 < var3; ++var5) {
            QName var6 = var1[var2 + var5];
            if (var6.localpart != null) {
               var7 = this.fChildrenType[var4];
               if (var7 == 0) {
                  if (this.fChildren[var4].rawname != var1[var2 + var5].rawname) {
                     return var5;
                  }
               } else if (var7 == 6) {
                  var8 = this.fChildren[var4].uri;
                  if (var8 != null && var8 != var1[var5].uri) {
                     return var5;
                  }
               } else if (var7 == 8) {
                  if (var1[var5].uri != null) {
                     return var5;
                  }
               } else if (var7 == 7 && this.fChildren[var4].uri == var1[var5].uri) {
                  return var5;
               }

               ++var4;
            }
         }
      } else {
         for(var4 = 0; var4 < var3; ++var4) {
            QName var9 = var1[var2 + var4];
            if (var9.localpart != null) {
               int var10;
               for(var10 = 0; var10 < this.fCount; ++var10) {
                  var7 = this.fChildrenType[var10];
                  if (var7 == 0) {
                     if (var9.rawname == this.fChildren[var10].rawname) {
                        break;
                     }
                  } else if (var7 == 6) {
                     var8 = this.fChildren[var10].uri;
                     if (var8 == null || var8 == var1[var4].uri) {
                        break;
                     }
                  } else if (var7 == 8) {
                     if (var1[var4].uri == null) {
                        break;
                     }
                  } else if (var7 == 7 && this.fChildren[var10].uri != var1[var4].uri) {
                     break;
                  }
               }

               if (var10 == this.fCount) {
                  return var4;
               }
            }
         }
      }

      return -1;
   }
}
