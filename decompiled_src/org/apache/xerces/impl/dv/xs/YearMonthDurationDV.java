package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.Duration;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

class YearMonthDurationDV extends DurationDV {
   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return this.parse(var1, 1);
      } catch (Exception var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "yearMonthDuration"});
      }
   }

   protected Duration getDuration(AbstractDateTimeDV.DateTimeData var1) {
      byte var2 = 1;
      if (var1.year < 0 || var1.month < 0) {
         var2 = -1;
      }

      return super.factory.newDuration(var2 == 1, var1.year != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.year)) : null, var1.month != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.month)) : null, (BigInteger)null, (BigInteger)null, (BigInteger)null, (BigDecimal)null);
   }
}
