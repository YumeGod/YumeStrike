package org.apache.xerces.impl.dv.xs;

import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class BooleanDV extends TypeValidator {
   private static final String[] fValueSpace = new String[]{"false", "true", "0", "1"};

   public short getAllowedFacets() {
      return 24;
   }

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      Boolean var3 = null;
      if (!var1.equals(fValueSpace[0]) && !var1.equals(fValueSpace[2])) {
         if (!var1.equals(fValueSpace[1]) && !var1.equals(fValueSpace[3])) {
            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "boolean"});
         }

         var3 = Boolean.TRUE;
      } else {
         var3 = Boolean.FALSE;
      }

      return var3;
   }
}
