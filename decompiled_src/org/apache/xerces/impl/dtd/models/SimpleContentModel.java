package org.apache.xerces.impl.dtd.models;

import org.apache.xerces.xni.QName;

public class SimpleContentModel implements ContentModelValidator {
   public static final short CHOICE = -1;
   public static final short SEQUENCE = -1;
   private QName fFirstChild = new QName();
   private QName fSecondChild = new QName();
   private int fOperator;

   public SimpleContentModel(short var1, QName var2, QName var3) {
      this.fFirstChild.setValues(var2);
      if (var3 != null) {
         this.fSecondChild.setValues(var3);
      } else {
         this.fSecondChild.clear();
      }

      this.fOperator = var1;
   }

   public int validate(QName[] var1, int var2, int var3) {
      int var4;
      switch (this.fOperator) {
         case 0:
            if (var3 == 0) {
               return 0;
            }

            if (var1[var2].rawname != this.fFirstChild.rawname) {
               return 0;
            }

            if (var3 > 1) {
               return 1;
            }
            break;
         case 1:
            if (var3 == 1 && var1[var2].rawname != this.fFirstChild.rawname) {
               return 0;
            }

            if (var3 > 1) {
               return 1;
            }
            break;
         case 2:
            if (var3 > 0) {
               for(var4 = 0; var4 < var3; ++var4) {
                  if (var1[var2 + var4].rawname != this.fFirstChild.rawname) {
                     return var4;
                  }
               }
            }
            break;
         case 3:
            if (var3 == 0) {
               return 0;
            }

            for(var4 = 0; var4 < var3; ++var4) {
               if (var1[var2 + var4].rawname != this.fFirstChild.rawname) {
                  return var4;
               }
            }

            return -1;
         case 4:
            if (var3 == 0) {
               return 0;
            }

            if (var1[var2].rawname != this.fFirstChild.rawname && var1[var2].rawname != this.fSecondChild.rawname) {
               return 0;
            }

            if (var3 > 1) {
               return 1;
            }
            break;
         case 5:
            if (var3 != 2) {
               if (var3 > 2) {
                  return 2;
               }

               return var3;
            }

            if (var1[var2].rawname != this.fFirstChild.rawname) {
               return 0;
            }

            if (var1[var2 + 1].rawname != this.fSecondChild.rawname) {
               return 1;
            }
            break;
         default:
            throw new RuntimeException("ImplementationMessages.VAL_CST");
      }

      return -1;
   }
}
