package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;
import org.apache.xerces.xs.datatypes.XSDecimal;

public class DecimalDV extends TypeValidator {
   public final short getAllowedFacets() {
      return 4088;
   }

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return new XDecimal(var1);
      } catch (NumberFormatException var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "decimal"});
      }
   }

   public final int compare(Object var1, Object var2) {
      return ((XDecimal)var1).compareTo((XDecimal)var2);
   }

   public final int getTotalDigits(Object var1) {
      return ((XDecimal)var1).totalDigits;
   }

   public final int getFractionDigits(Object var1) {
      return ((XDecimal)var1).fracDigits;
   }

   static class XDecimal implements XSDecimal {
      int sign = 1;
      int totalDigits = 0;
      int intDigits = 0;
      int fracDigits = 0;
      String ivalue = "";
      String fvalue = "";
      boolean integer = false;
      private String canonical;

      XDecimal(String var1) throws NumberFormatException {
         this.initD(var1);
      }

      XDecimal(String var1, boolean var2) throws NumberFormatException {
         if (var2) {
            this.initI(var1);
         } else {
            this.initD(var1);
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
               if (var1.charAt(var9) != '.') {
                  throw new NumberFormatException();
               }

               var5 = var9 + 1;
               var6 = var2;
            }

            if (var3 == var9 && var5 == var6) {
               throw new NumberFormatException();
            } else {
               while(var6 > var5 && var1.charAt(var6 - 1) == '0') {
                  --var6;
               }

               for(int var8 = var5; var8 < var6; ++var8) {
                  if (!TypeValidator.isDigit(var1.charAt(var8))) {
                     throw new NumberFormatException();
                  }
               }

               this.intDigits = var9 - var7;
               this.fracDigits = var6 - var5;
               this.totalDigits = this.intDigits + this.fracDigits;
               if (this.intDigits > 0) {
                  this.ivalue = var1.substring(var7, var9);
                  if (this.fracDigits > 0) {
                     this.fvalue = var1.substring(var5, var6);
                  }
               } else if (this.fracDigits > 0) {
                  this.fvalue = var1.substring(var5, var6);
               } else {
                  this.sign = 0;
               }

            }
         }
      }

      void initI(String var1) throws NumberFormatException {
         int var2 = var1.length();
         if (var2 == 0) {
            throw new NumberFormatException();
         } else {
            byte var3 = 0;
            boolean var4 = false;
            if (var1.charAt(0) == '+') {
               var3 = 1;
            } else if (var1.charAt(0) == '-') {
               var3 = 1;
               this.sign = -1;
            }

            int var5;
            for(var5 = var3; var5 < var2 && var1.charAt(var5) == '0'; ++var5) {
            }

            int var6;
            for(var6 = var5; var6 < var2 && TypeValidator.isDigit(var1.charAt(var6)); ++var6) {
            }

            if (var6 < var2) {
               throw new NumberFormatException();
            } else if (var3 == var6) {
               throw new NumberFormatException();
            } else {
               this.intDigits = var6 - var5;
               this.fracDigits = 0;
               this.totalDigits = this.intDigits;
               if (this.intDigits > 0) {
                  this.ivalue = var1.substring(var5, var6);
               } else {
                  this.sign = 0;
               }

               this.integer = true;
            }
         }
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else if (!(var1 instanceof XDecimal)) {
            return false;
         } else {
            XDecimal var2 = (XDecimal)var1;
            if (this.sign != var2.sign) {
               return false;
            } else if (this.sign == 0) {
               return true;
            } else {
               return this.intDigits == var2.intDigits && this.fracDigits == var2.fracDigits && this.ivalue.equals(var2.ivalue) && this.fvalue.equals(var2.fvalue);
            }
         }
      }

      public int compareTo(XDecimal var1) {
         if (this.sign != var1.sign) {
            return this.sign > var1.sign ? 1 : -1;
         } else {
            return this.sign == 0 ? 0 : this.sign * this.intComp(var1);
         }
      }

      private int intComp(XDecimal var1) {
         if (this.intDigits != var1.intDigits) {
            return this.intDigits > var1.intDigits ? 1 : -1;
         } else {
            int var2 = this.ivalue.compareTo(var1.ivalue);
            if (var2 != 0) {
               return var2 > 0 ? 1 : -1;
            } else {
               var2 = this.fvalue.compareTo(var1.fvalue);
               return var2 == 0 ? 0 : (var2 > 0 ? 1 : -1);
            }
         }
      }

      public synchronized String toString() {
         if (this.canonical == null) {
            this.makeCanonical();
         }

         return this.canonical;
      }

      private void makeCanonical() {
         if (this.sign == 0) {
            if (this.integer) {
               this.canonical = "0";
            } else {
               this.canonical = "0.0";
            }

         } else if (this.integer && this.sign > 0) {
            this.canonical = this.ivalue;
         } else {
            StringBuffer var1 = new StringBuffer(this.totalDigits + 3);
            if (this.sign == -1) {
               var1.append('-');
            }

            if (this.intDigits != 0) {
               var1.append(this.ivalue);
            } else {
               var1.append('0');
            }

            if (!this.integer) {
               var1.append('.');
               if (this.fracDigits != 0) {
                  var1.append(this.fvalue);
               } else {
                  var1.append('0');
               }
            }

            this.canonical = var1.toString();
         }
      }

      public BigDecimal getBigDecimal() {
         return this.sign == 0 ? new BigDecimal(BigInteger.ZERO) : new BigDecimal(this.toString());
      }

      public BigInteger getBigInteger() throws NumberFormatException {
         if (this.fracDigits != 0) {
            throw new NumberFormatException();
         } else if (this.sign == 0) {
            return BigInteger.ZERO;
         } else {
            return this.sign == 1 ? new BigInteger(this.ivalue) : new BigInteger("-" + this.ivalue);
         }
      }

      public long getLong() throws NumberFormatException {
         if (this.fracDigits != 0) {
            throw new NumberFormatException();
         } else if (this.sign == 0) {
            return 0L;
         } else {
            return this.sign == 1 ? Long.parseLong(this.ivalue) : Long.parseLong("-" + this.ivalue);
         }
      }

      public int getInt() throws NumberFormatException {
         if (this.fracDigits != 0) {
            throw new NumberFormatException();
         } else if (this.sign == 0) {
            return 0;
         } else {
            return this.sign == 1 ? Integer.parseInt(this.ivalue) : Integer.parseInt("-" + this.ivalue);
         }
      }

      public short getShort() throws NumberFormatException {
         if (this.fracDigits != 0) {
            throw new NumberFormatException();
         } else if (this.sign == 0) {
            return 0;
         } else {
            return this.sign == 1 ? Short.parseShort(this.ivalue) : Short.parseShort("-" + this.ivalue);
         }
      }

      public byte getByte() throws NumberFormatException {
         if (this.fracDigits != 0) {
            throw new NumberFormatException();
         } else if (this.sign == 0) {
            return 0;
         } else {
            return this.sign == 1 ? Byte.parseByte(this.ivalue) : Byte.parseByte("-" + this.ivalue);
         }
      }
   }
}
