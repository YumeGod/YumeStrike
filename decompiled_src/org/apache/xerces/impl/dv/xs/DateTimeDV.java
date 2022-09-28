package org.apache.xerces.impl.dv.xs;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class DateTimeDV extends AbstractDateTimeDV {
   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return this.parse(var1);
      } catch (Exception var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "dateTime"});
      }
   }

   protected AbstractDateTimeDV.DateTimeData parse(String var1) throws SchemaDateTimeException {
      AbstractDateTimeDV.DateTimeData var2 = new AbstractDateTimeDV.DateTimeData(var1, this);
      int var3 = var1.length();
      int var4 = this.indexOf(var1, 0, var3, 'T');
      int var5 = this.getDate(var1, 0, var4, var2);
      this.getTime(var1, var4 + 1, var3, var2);
      if (var5 != var4) {
         throw new RuntimeException(var1 + " is an invalid dateTime dataype value. " + "Invalid character(s) seprating date and time values.");
      } else {
         this.validateDateTime(var2);
         this.saveUnnormalized(var2);
         if (var2.utc != 0 && var2.utc != 90) {
            this.normalize(var2);
         }

         return var2;
      }
   }

   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData var1) {
      return super.factory.newXMLGregorianCalendar(BigInteger.valueOf((long)var1.unNormYear), var1.unNormMonth, var1.unNormDay, var1.unNormHour, var1.unNormMinute, (int)var1.unNormSecond, var1.unNormSecond != 0.0 ? new BigDecimal(var1.unNormSecond - (double)((int)var1.unNormSecond)) : null, var1.timezoneHr * 60 + var1.timezoneMin);
   }
}
