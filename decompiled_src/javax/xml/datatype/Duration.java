package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.xml.namespace.QName;

public abstract class Duration {
   public QName getXMLSchemaType() {
      boolean var1 = this.isSet(DatatypeConstants.YEARS);
      boolean var2 = this.isSet(DatatypeConstants.MONTHS);
      boolean var3 = this.isSet(DatatypeConstants.DAYS);
      boolean var4 = this.isSet(DatatypeConstants.HOURS);
      boolean var5 = this.isSet(DatatypeConstants.MINUTES);
      boolean var6 = this.isSet(DatatypeConstants.SECONDS);
      if (var1 && var2 && var3 && var4 && var5 && var6) {
         return DatatypeConstants.DURATION;
      } else if (!var1 && !var2 && var3 && var4 && var5 && var6) {
         return DatatypeConstants.DURATION_DAYTIME;
      } else if (var1 && var2 && !var3 && !var4 && !var5 && !var6) {
         return DatatypeConstants.DURATION_YEARMONTH;
      } else {
         throw new IllegalStateException("javax.xml.datatype.Duration#getXMLSchemaType(): this Duration does not match one of the XML Schema date/time datatypes: year set = " + var1 + " month set = " + var2 + " day set = " + var3 + " hour set = " + var4 + " minute set = " + var5 + " second set = " + var6);
      }
   }

   public abstract int getSign();

   public int getYears() {
      return this.getField(DatatypeConstants.YEARS).intValue();
   }

   public int getMonths() {
      return this.getField(DatatypeConstants.MONTHS).intValue();
   }

   public int getDays() {
      return this.getField(DatatypeConstants.DAYS).intValue();
   }

   public int getHours() {
      return this.getField(DatatypeConstants.HOURS).intValue();
   }

   public int getMinutes() {
      return this.getField(DatatypeConstants.MINUTES).intValue();
   }

   public int getSeconds() {
      return this.getField(DatatypeConstants.SECONDS).intValue();
   }

   public long getTimeInMillis(Calendar var1) {
      Calendar var2 = (Calendar)var1.clone();
      this.addTo(var2);
      return getCalendarTimeInMillis(var2) - getCalendarTimeInMillis(var1);
   }

   public long getTimeInMillis(Date var1) {
      GregorianCalendar var2 = new GregorianCalendar();
      var2.setTime(var1);
      this.addTo((Calendar)var2);
      return getCalendarTimeInMillis(var2) - var1.getTime();
   }

   public abstract Number getField(DatatypeConstants.Field var1);

   public abstract boolean isSet(DatatypeConstants.Field var1);

   public abstract Duration add(Duration var1);

   public abstract void addTo(Calendar var1);

   public void addTo(Date var1) {
      if (var1 == null) {
         throw new NullPointerException("Cannot call " + this.getClass().getName() + "#addTo(Date date) with date == null.");
      } else {
         GregorianCalendar var2 = new GregorianCalendar();
         var2.setTime(var1);
         this.addTo((Calendar)var2);
         var1.setTime(getCalendarTimeInMillis(var2));
      }
   }

   public Duration subtract(Duration var1) {
      return this.add(var1.negate());
   }

   public Duration multiply(int var1) {
      return this.multiply(new BigDecimal(String.valueOf(var1)));
   }

   public abstract Duration multiply(BigDecimal var1);

   public abstract Duration negate();

   public abstract Duration normalizeWith(Calendar var1);

   public abstract int compare(Duration var1);

   public boolean isLongerThan(Duration var1) {
      return this.compare(var1) == 1;
   }

   public boolean isShorterThan(Duration var1) {
      return this.compare(var1) == -1;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else if (!(var1 instanceof Duration)) {
         return false;
      } else {
         return this.compare((Duration)var1) == 0;
      }
   }

   public abstract int hashCode();

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.getSign() < 0) {
         var1.append('-');
      }

      var1.append('P');
      BigInteger var2 = (BigInteger)this.getField(DatatypeConstants.YEARS);
      if (var2 != null) {
         var1.append(var2 + "Y");
      }

      BigInteger var3 = (BigInteger)this.getField(DatatypeConstants.MONTHS);
      if (var3 != null) {
         var1.append(var3 + "M");
      }

      BigInteger var4 = (BigInteger)this.getField(DatatypeConstants.DAYS);
      if (var4 != null) {
         var1.append(var4 + "D");
      }

      BigInteger var5 = (BigInteger)this.getField(DatatypeConstants.HOURS);
      BigInteger var6 = (BigInteger)this.getField(DatatypeConstants.MINUTES);
      BigDecimal var7 = (BigDecimal)this.getField(DatatypeConstants.SECONDS);
      if (var5 != null || var6 != null || var7 != null) {
         var1.append('T');
         if (var5 != null) {
            var1.append(var5 + "H");
         }

         if (var6 != null) {
            var1.append(var6 + "M");
         }

         if (var7 != null) {
            var1.append(this.toString(var7) + "S");
         }
      }

      return var1.toString();
   }

   private String toString(BigDecimal var1) {
      String var2 = var1.unscaledValue().toString();
      int var3 = var1.scale();
      if (var3 == 0) {
         return var2;
      } else {
         int var5 = var2.length() - var3;
         if (var5 == 0) {
            return "0." + var2;
         } else {
            StringBuffer var4;
            if (var5 > 0) {
               var4 = new StringBuffer(var2);
               var4.insert(var5, '.');
            } else {
               var4 = new StringBuffer(3 - var5 + var2.length());
               var4.append("0.");

               for(int var6 = 0; var6 < -var5; ++var6) {
                  var4.append('0');
               }

               var4.append(var2);
            }

            return var4.toString();
         }
      }
   }

   private static long getCalendarTimeInMillis(Calendar var0) {
      return var0.getTime().getTime();
   }
}
