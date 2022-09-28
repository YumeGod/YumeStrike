package org.apache.xerces.impl.dv.xs;

import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.impl.dv.InvalidDatatypeValueException;
import org.apache.xerces.impl.dv.ValidationContext;

public class YearDV extends AbstractDateTimeDV {
   public Object getActualValue(String var1, ValidationContext var2) throws InvalidDatatypeValueException {
      try {
         return this.parse(var1);
      } catch (Exception var4) {
         throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{var1, "gYear"});
      }
   }

   protected AbstractDateTimeDV.DateTimeData parse(String var1) throws SchemaDateTimeException {
      AbstractDateTimeDV.DateTimeData var2 = new AbstractDateTimeDV.DateTimeData(var1, this);
      int var3 = var1.length();
      byte var4 = 0;
      if (var1.charAt(0) == '-') {
         var4 = 1;
      }

      int var5 = this.findUTCSign(var1, var4, var3);
      if (var5 == -1) {
         var2.year = this.parseIntYear(var1, var3);
      } else {
         var2.year = this.parseIntYear(var1, var5);
         this.getTimeZone(var1, var2, var5, var3);
      }

      var2.month = 1;
      var2.day = 1;
      this.validateDateTime(var2);
      this.saveUnnormalized(var2);
      if (var2.utc != 0 && var2.utc != 90) {
         this.normalize(var2);
      }

      var2.position = 0;
      return var2;
   }

   protected String dateToString(AbstractDateTimeDV.DateTimeData var1) {
      StringBuffer var2 = new StringBuffer(5);
      this.append(var2, var1.year, 4);
      this.append(var2, (char)var1.utc, 0);
      return var2.toString();
   }

   protected XMLGregorianCalendar getXMLGregorianCalendar(AbstractDateTimeDV.DateTimeData var1) {
      return super.factory.newXMLGregorianCalendar(var1.unNormYear, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, var1.timezoneHr * 60 + var1.timezoneMin);
   }
}
