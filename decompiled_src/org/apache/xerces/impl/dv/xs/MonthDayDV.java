package org.apache.xerces.impl.dv.xs;

import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class MonthDayDV extends AbstractDateTimeDV {
   private static final int MONTHDAY_SIZE = 7;

   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return this.parse(var1);
      } catch (Exception var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "gMonthDay"});
      }
   }

   protected AbstractDateTimeDV.DateTimeData parse(String var1) throws SchemaDateTimeException {
      AbstractDateTimeDV.DateTimeData var2 = new AbstractDateTimeDV.DateTimeData(var1, this);
      int var3 = var1.length();
      var2.year = 2000;
      if (var1.charAt(0) == '-' && var1.charAt(1) == '-') {
         var2.month = this.parseInt(var1, 2, 4);
         int var4 = 4;
         if (var1.charAt(var4++) != '-') {
            throw new SchemaDateTimeException("Invalid format for gMonthDay: " + var1);
         } else {
            var2.day = this.parseInt(var1, var4, var4 + 2);
            if (7 < var3) {
               if (!this.isNextCharUTCSign(var1, 7, var3)) {
                  throw new SchemaDateTimeException("Error in month parsing:" + var1);
               }

               this.getTimeZone(var1, var2, 7, var3);
            }

            this.validateDateTime(var2);
            this.saveUnnormalized(var2);
            if (var2.utc != 0 && var2.utc != 90) {
               this.normalize(var2);
            }

            var2.position = 1;
            return var2;
         }
      } else {
         throw new SchemaDateTimeException("Invalid format for gMonthDay: " + var1);
      }
   }

   protected String dateToString(AbstractDateTimeDV.DateTimeData var1) {
      StringBuffer var2 = new StringBuffer(8);
      var2.append('-');
      var2.append('-');
      this.append(var2, var1.month, 2);
      var2.append('-');
      this.append(var2, var1.day, 2);
      this.append(var2, (char)var1.utc, 0);
      return var2.toString();
   }

   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData var1) {
      return super.factory.newXMLGregorianCalendar(Integer.MIN_VALUE, var1.unNormMonth, var1.unNormDay, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, var1.timezoneHr * 60 + var1.timezoneMin);
   }
}
