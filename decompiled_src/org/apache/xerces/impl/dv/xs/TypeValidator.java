package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public abstract class TypeValidator {
   public static final short LESS_THAN = -1;
   public static final short EQUAL = 0;
   public static final short GREATER_THAN = 1;
   public static final short INDETERMINATE = 2;

   public abstract short getAllowedFacets();

   public abstract Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException;

   public void checkExtraRules(Object var1, ValidationContext var2) throws InvalidDatatypeValueException {
   }

   public boolean isIdentical(Object var1, Object var2) {
      return var1.equals(var2);
   }

   public int compare(Object var1, Object var2) {
      return -1;
   }

   public int getDataLength(Object var1) {
      return var1 instanceof String ? ((String)var1).length() : -1;
   }

   public int getTotalDigits(Object var1) {
      return -1;
   }

   public int getFractionDigits(Object var1) {
      return -1;
   }

   public static final boolean isDigit(char var0) {
      return var0 >= '0' && var0 <= '9';
   }

   public static final int getDigit(char var0) {
      return isDigit(var0) ? var0 - 48 : -1;
   }
}
