package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.Duration;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

class DayTimeDurationDV extends DurationDV {
   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return this.parse(var1, 2);
      } catch (Exception var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "dayTimeDuration"});
      }
   }

   protected Duration getDuration(AbstractDateTimeDV.DateTimeData var1) {
      byte var2 = 1;
      if (var1.day < 0 || var1.hour < 0 || var1.minute < 0 || var1.second < 0.0) {
         var2 = -1;
      }

      return super.factory.newDuration(var2 == 1, (BigInteger)null, (BigInteger)null, var1.day != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.day)) : null, var1.hour != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.hour)) : null, var1.minute != Integer.MIN_VALUE ? BigInteger.valueOf((long)(var2 * var1.minute)) : null, var1.second != -2.147483648E9 ? new BigDecimal(String.valueOf((double)var2 * var1.second)) : null);
   }
}
