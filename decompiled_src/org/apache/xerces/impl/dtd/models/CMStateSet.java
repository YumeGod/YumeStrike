package org.apache.xerces.impl.dtd.models;

public class CMStateSet {
   int fBitCount;
   int fByteCount;
   int fBits1;
   int fBits2;
   byte[] fByteArray;

   public CMStateSet(int var1) {
      this.fBitCount = var1;
      if (this.fBitCount < 0) {
         throw new RuntimeException("ImplementationMessages.VAL_CMSI");
      } else {
         if (this.fBitCount > 64) {
            this.fByteCount = this.fBitCount / 8;
            if (this.fBitCount % 8 != 0) {
               ++this.fByteCount;
            }

            this.fByteArray = new byte[this.fByteCount];
         }

         this.zeroBits();
      }
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();

      try {
         var1.append("{");

         for(int var2 = 0; var2 < this.fBitCount; ++var2) {
            if (this.getBit(var2)) {
               var1.append(" " + var2);
            }
         }

         var1.append(" }");
      } catch (RuntimeException var3) {
      }

      return var1.toString();
   }

   public final void intersection(CMStateSet var1) {
      if (this.fBitCount < 65) {
         this.fBits1 &= var1.fBits1;
         this.fBits2 &= var1.fBits2;
      } else {
         for(int var2 = this.fByteCount - 1; var2 >= 0; --var2) {
            byte[] var10000 = this.fByteArray;
            var10000[var2] &= var1.fByteArray[var2];
         }
      }

   }

   public final boolean getBit(int var1) {
      if (var1 >= this.fBitCount) {
         throw new RuntimeException("ImplementationMessages.VAL_CMSI");
      } else {
         int var2;
         if (this.fBitCount < 65) {
            var2 = 1 << var1 % 32;
            if (var1 < 32) {
               return (this.fBits1 & var2) != 0;
            } else {
               return (this.fBits2 & var2) != 0;
            }
         } else {
            var2 = (byte)(1 << var1 % 8);
            int var3 = var1 >> 3;
            return (this.fByteArray[var3] & var2) != 0;
         }
      }
   }

   public final boolean isEmpty() {
      if (this.fBitCount < 65) {
         return this.fBits1 == 0 && this.fBits2 == 0;
      } else {
         for(int var1 = this.fByteCount - 1; var1 >= 0; --var1) {
            if (this.fByteArray[var1] != 0) {
               return false;
            }
         }

         return true;
      }
   }

   final boolean isSameSet(CMStateSet var1) {
      if (this.fBitCount != var1.fBitCount) {
         return false;
      } else if (this.fBitCount < 65) {
         return this.fBits1 == var1.fBits1 && this.fBits2 == var1.fBits2;
      } else {
         for(int var2 = this.fByteCount - 1; var2 >= 0; --var2) {
            if (this.fByteArray[var2] != var1.fByteArray[var2]) {
               return false;
            }
         }

         return true;
      }
   }

   public final void union(CMStateSet var1) {
      if (this.fBitCount < 65) {
         this.fBits1 |= var1.fBits1;
         this.fBits2 |= var1.fBits2;
      } else {
         for(int var2 = this.fByteCount - 1; var2 >= 0; --var2) {
            byte[] var10000 = this.fByteArray;
            var10000[var2] |= var1.fByteArray[var2];
         }
      }

   }

   public final void setBit(int var1) {
      if (var1 >= this.fBitCount) {
         throw new RuntimeException("ImplementationMessages.VAL_CMSI");
      } else {
         if (this.fBitCount < 65) {
            int var2 = 1 << var1 % 32;
            if (var1 < 32) {
               this.fBits1 &= ~var2;
               this.fBits1 |= var2;
            } else {
               this.fBits2 &= ~var2;
               this.fBits2 |= var2;
            }
         } else {
            byte var4 = (byte)(1 << var1 % 8);
            int var3 = var1 >> 3;
            byte[] var10000 = this.fByteArray;
            var10000[var3] = (byte)(var10000[var3] & ~var4);
            var10000 = this.fByteArray;
            var10000[var3] |= var4;
         }

      }
   }

   public final void setTo(CMStateSet var1) {
      if (this.fBitCount != var1.fBitCount) {
         throw new RuntimeException("ImplementationMessages.VAL_CMSI");
      } else {
         if (this.fBitCount < 65) {
            this.fBits1 = var1.fBits1;
            this.fBits2 = var1.fBits2;
         } else {
            for(int var2 = this.fByteCount - 1; var2 >= 0; --var2) {
               this.fByteArray[var2] = var1.fByteArray[var2];
            }
         }

      }
   }

   public final void zeroBits() {
      if (this.fBitCount < 65) {
         this.fBits1 = 0;
         this.fBits2 = 0;
      } else {
         for(int var1 = this.fByteCount - 1; var1 >= 0; --var1) {
            this.fByteArray[var1] = 0;
         }
      }

   }

   public boolean equals(Object var1) {
      return !(var1 instanceof CMStateSet) ? false : this.isSameSet((CMStateSet)var1);
   }

   public int hashCode() {
      if (this.fBitCount < 65) {
         return this.fBits1 + this.fBits2 * 31;
      } else {
         int var1 = 0;

         for(int var2 = this.fByteCount - 1; var2 >= 0; --var2) {
            var1 = this.fByteArray[var2] + var1 * 31;
         }

         return var1;
      }
   }
}
