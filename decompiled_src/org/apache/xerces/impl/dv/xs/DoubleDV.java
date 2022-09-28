package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.xs.datatypes.XSDouble;

public class DoubleDV extends TypeValidator {
   public short getAllowedFacets() {
      return 2552;
   }

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return new XDouble(var1);
      } catch (NumberFormatException var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "double"});
      }
   }

   public int compare(Object var1, Object var2) {
      return ((XDouble)var1).compareTo((XDouble)var2);
   }

   public boolean isIdentical(Object var1, Object var2) {
      return var2 instanceof XDouble ? ((XDouble)var1).isIdentical((XDouble)var2) : false;
   }

   static boolean isPossibleFP(String var0) {
      int var1 = var0.length();

      for(int var2 = 0; var2 < var1; ++var2) {
         char var3 = var0.charAt(var2);
         if ((var3 < '0' || var3 > '9') && var3 != '.' && var3 != '-' && var3 != '+' && var3 != 'E' && var3 != 'e') {
            return false;
         }
      }

      return true;
   }

   private static final class XDouble implements XSDouble {
      private double value;
      private String canonical;

      public XDouble(String var1) throws NumberFormatException {
         if (DoubleDV.isPossibleFP(var1)) {
            this.value = Double.parseDouble(var1);
         } else if (var1.equals("INF")) {
            this.value = Double.POSITIVE_INFINITY;
         } else if (var1.equals("-INF")) {
            this.value = Double.NEGATIVE_INFINITY;
         } else {
            if (!var1.equals("NaN")) {
               throw new NumberFormatException(var1);
            }

            this.value = Double.NaN;
         }

      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof XDouble)) {
            return false;
         } else {
            XDouble var2 = (XDouble)var1;
            if (this.value == var2.value) {
               return true;
            } else {
               return this.value != this.value && var2.value != var2.value;
            }
         }
      }

      public boolean isIdentical(XDouble var1) {
         if (var1 == this) {
            return true;
         } else if (this.value != var1.value) {
            return this.value != this.value && var1.value != var1.value;
         } else {
            return this.value != 0.0 || Double.doubleToLongBits(this.value) == Double.doubleToLongBits(var1.value);
         }
      }

      private int compareTo(XDouble var1) {
         double var2 = var1.value;
         if (this.value < var2) {
            return -1;
         } else if (this.value > var2) {
            return 1;
         } else if (this.value == var2) {
            return 0;
         } else if (this.value != this.value) {
            return var2 != var2 ? 0 : 2;
         } else {
            return 2;
         }
      }

      public synchronized String toString() {
         if (this.canonical == null) {
            if (this.value == Double.POSITIVE_INFINITY) {
               this.canonical = "INF";
            } else if (this.value == Double.NEGATIVE_INFINITY) {
               this.canonical = "-INF";
            } else if (this.value != this.value) {
               this.canonical = "NaN";
            } else if (this.value == 0.0) {
               this.canonical = "0.0E1";
            } else {
               this.canonical = Double.toString(this.value);
               if (this.canonical.indexOf(69) == -1) {
                  int var1 = this.canonical.length();
                  char[] var2 = new char[var1 + 3];
                  this.canonical.getChars(0, var1, var2, 0);
                  int var3 = var2[0] == '-' ? 2 : 1;
                  int var4;
                  int var5;
                  int var6;
                  if (!(this.value >= 1.0) && !(this.value <= -1.0)) {
                     for(var4 = var3 + 1; var2[var4] == '0'; ++var4) {
                     }

                     var2[var3 - 1] = var2[var4];
                     var2[var3] = '.';
                     var5 = var4 + 1;

                     for(var6 = var3 + 1; var5 < var1; ++var6) {
                        var2[var6] = var2[var5];
                        ++var5;
                     }

                     var1 -= var4 - var3;
                     if (var1 == var3 + 1) {
                        var2[var1++] = '0';
                     }

                     var2[var1++] = 'E';
                     var2[var1++] = '-';
                     int var7 = var4 - var3;
                     var2[var1++] = (char)(var7 + 48);
                  } else {
                     var4 = this.canonical.indexOf(46);

                     for(var5 = var4; var5 > var3; --var5) {
                        var2[var5] = var2[var5 - 1];
                     }

                     for(var2[var3] = '.'; var2[var1 - 1] == '0'; --var1) {
                     }

                     if (var2[var1 - 1] == '.') {
                        ++var1;
                     }

                     var2[var1++] = 'E';
                     var6 = var4 - var3;
                     var2[var1++] = (char)(var6 + 48);
                  }

                  this.canonical = new String(var2, 0, var1);
               }
            }
         }

         return this.canonical;
      }

      public double getValue() {
         return this.value;
      }
   }
}
