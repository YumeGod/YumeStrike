package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

class PrecisionDecimalDV extends TypeValidator {
   public short getAllowedFacets() {
      return 4088;
   }

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return new XPrecisionDecimal(var1);
      } catch (NumberFormatException var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "precisionDecimal"});
      }
   }

   public int compare(Object var1, Object var2) {
      return ((XPrecisionDecimal)var1).compareTo((XPrecisionDecimal)var2);
   }

   public int getFractionDigits(Object var1) {
      return ((XPrecisionDecimal)var1).fracDigits;
   }

   public int getTotalDigits(Object var1) {
      return ((XPrecisionDecimal)var1).totalDigits;
   }

   public boolean isIdentical(Object var1, Object var2) {
      return var2 instanceof XPrecisionDecimal && var1 instanceof XPrecisionDecimal ? ((XPrecisionDecimal)var1).isIdentical((XPrecisionDecimal)var2) : false;
   }

   static class XPrecisionDecimal {
      int sign = 1;
      int totalDigits = 0;
      int intDigits = 0;
      int fracDigits = 0;
      String ivalue = "";
      String fvalue = "";
      int pvalue = 0;
      private String canonical;

      XPrecisionDecimal(String var1) throws NumberFormatException {
         if (var1.equals("NaN")) {
            this.ivalue = var1;
            this.sign = 0;
         }

         if (!var1.equals("+INF") && !var1.equals("INF") && !var1.equals("-INF")) {
            this.initD(var1);
         } else {
            this.ivalue = var1.charAt(0) == '+' ? var1.substring(1) : var1;
         }
      }

      void initD(String var1) throws NumberFormatException {
         int var2 = var1.length();
         if (var2 == 0) {
            throw new NumberFormatException();
         } else {
            byte var3 = 0;
            boolean var4 = false;
            int var5 = 0;
            int var6 = 0;
            if (var1.charAt(0) == '+') {
               var3 = 1;
            } else if (var1.charAt(0) == '-') {
               var3 = 1;
               this.sign = -1;
            }

            int var7;
            for(var7 = var3; var7 < var2 && var1.charAt(var7) == '0'; ++var7) {
            }

            int var9;
            for(var9 = var7; var9 < var2 && TypeValidator.isDigit(var1.charAt(var9)); ++var9) {
            }

            if (var9 < var2) {
               if (var1.charAt(var9) != '.' && var1.charAt(var9) != 'E' && var1.charAt(var9) != 'e') {
                  throw new NumberFormatException();
               }

               if (var1.charAt(var9) == '.') {
                  var5 = var9 + 1;

                  for(var6 = var5; var6 < var2 && TypeValidator.isDigit(var1.charAt(var6)); ++var6) {
                  }
               } else {
                  this.pvalue = Integer.parseInt(var1.substring(var9 + 1, var2));
               }
            }

            if (var3 == var9 && var5 == var6) {
               throw new NumberFormatException();
            } else {
               for(int var8 = var5; var8 < var6; ++var8) {
                  if (!TypeValidator.isDigit(var1.charAt(var8))) {
                     throw new NumberFormatException();
                  }
               }

               this.intDigits = var9 - var7;
               this.fracDigits = var6 - var5;
               if (this.intDigits > 0) {
                  this.ivalue = var1.substring(var7, var9);
               }

               if (this.fracDigits > 0) {
                  this.fvalue = var1.substring(var5, var6);
                  if (var6 < var2) {
                     this.pvalue = Integer.parseInt(var1.substring(var6 + 1, var2));
                  }
               }

               this.totalDigits = this.intDigits + this.fracDigits;
            }
         }
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof XPrecisionDecimal)) {
            return false;
         } else {
            XPrecisionDecimal var2 = (XPrecisionDecimal)var1;
            return this.compareTo(var2) == 0;
         }
      }

      private int compareFractionalPart(XPrecisionDecimal var1) {
         if (this.fvalue.equals(var1.fvalue)) {
            return 0;
         } else {
            StringBuffer var2 = new StringBuffer(this.fvalue);
            StringBuffer var3 = new StringBuffer(var1.fvalue);
            this.truncateTrailingZeros(var2, var3);
            return var2.toString().compareTo(var3.toString());
         }
      }

      private void truncateTrailingZeros(StringBuffer var1, StringBuffer var2) {
         for(int var3 = var1.length() - 1; var3 >= 0 && var1.charAt(var3) == '0'; --var3) {
            var1.deleteCharAt(var3);
         }

         for(int var4 = var2.length() - 1; var4 >= 0 && var2.charAt(var4) == '0'; --var4) {
            var2.deleteCharAt(var4);
         }

      }

      public int compareTo(XPrecisionDecimal var1) {
         if (this.sign == 0) {
            return 2;
         } else if (!this.ivalue.equals("INF") && !var1.ivalue.equals("INF")) {
            if (!this.ivalue.equals("-INF") && !var1.ivalue.equals("-INF")) {
               if (this.sign != var1.sign) {
                  return this.sign > var1.sign ? 1 : -1;
               } else {
                  return this.sign * this.compare(var1);
               }
            } else if (this.ivalue.equals(var1.ivalue)) {
               return 0;
            } else {
               return this.ivalue.equals("-INF") ? -1 : 1;
            }
         } else if (this.ivalue.equals(var1.ivalue)) {
            return 0;
         } else {
            return this.ivalue.equals("INF") ? 1 : -1;
         }
      }

      private int compare(XPrecisionDecimal var1) {
         if (this.pvalue == 0 && var1.pvalue == 0) {
            return this.intComp(var1);
         } else if (this.pvalue == var1.pvalue) {
            return this.intComp(var1);
         } else if (this.intDigits + this.pvalue != var1.intDigits + var1.pvalue) {
            return this.intDigits + this.pvalue > var1.intDigits + var1.pvalue ? 1 : -1;
         } else {
            int var2;
            StringBuffer var3;
            StringBuffer var4;
            int var5;
            if (this.pvalue > var1.pvalue) {
               var2 = this.pvalue - var1.pvalue;
               var3 = new StringBuffer(this.ivalue);
               var4 = new StringBuffer(this.fvalue);

               for(var5 = 0; var5 < var2; ++var5) {
                  if (var5 < this.fracDigits) {
                     var3.append(this.fvalue.charAt(var5));
                     var4.deleteCharAt(var5);
                  } else {
                     var3.append('0');
                  }
               }

               return this.compareDecimal(var3.toString(), var1.ivalue, var4.toString(), var1.fvalue);
            } else {
               var2 = var1.pvalue - this.pvalue;
               var3 = new StringBuffer(var1.ivalue);
               var4 = new StringBuffer(var1.fvalue);

               for(var5 = 0; var5 < var2; ++var5) {
                  if (var5 < var1.fracDigits) {
                     var3.append(var1.fvalue.charAt(var5));
                     var4.deleteCharAt(var5);
                  } else {
                     var3.append('0');
                  }
               }

               return this.compareDecimal(this.ivalue, var3.toString(), this.fvalue, var4.toString());
            }
         }
      }

      private int intComp(XPrecisionDecimal var1) {
         if (this.intDigits != var1.intDigits) {
            return this.intDigits > var1.intDigits ? 1 : -1;
         } else {
            return this.compareDecimal(this.ivalue, var1.ivalue, this.fvalue, var1.fvalue);
         }
      }

      private int compareDecimal(String var1, String var2, String var3, String var4) {
         int var5 = var1.compareTo(var3);
         if (var5 != 0) {
            return var5 > 0 ? 1 : -1;
         } else if (var2.equals(var4)) {
            return 0;
         } else {
            StringBuffer var6 = new StringBuffer(var2);
            StringBuffer var7 = new StringBuffer(var4);
            this.truncateTrailingZeros(var6, var7);
            var5 = var6.toString().compareTo(var7.toString());
            return var5 == 0 ? 0 : (var5 > 0 ? 1 : -1);
         }
      }

      public synchronized String toString() {
         if (this.canonical == null) {
            this.makeCanonical();
         }

         return this.canonical;
      }

      private void makeCanonical() {
         this.canonical = "TBD by Working Group";
      }

      public boolean isIdentical(XPrecisionDecimal var1) {
         if (!this.ivalue.equals(var1.ivalue) || !this.ivalue.equals("INF") && !this.ivalue.equals("-INF") && !this.ivalue.equals("NaN")) {
            return this.sign == var1.sign && this.intDigits == var1.intDigits && this.fracDigits == var1.fracDigits && this.pvalue == var1.pvalue && this.ivalue.equals(var1.ivalue) && this.fvalue.equals(var1.fvalue);
         } else {
            return true;
         }
      }
   }
}
