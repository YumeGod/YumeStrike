package javax.xml.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.namespace.QName;

public abstract class XMLGregorianCalendar implements Cloneable {
   public abstract void clear();

   public abstract void reset();

   public abstract void setYear(BigInteger var1);

   public abstract void setYear(int var1);

   public abstract void setMonth(int var1);

   public abstract void setDay(int var1);

   public abstract void setTimezone(int var1);

   public void setTime(int var1, int var2, int var3) {
      this.setTime(var1, var2, var3, (BigDecimal)null);
   }

   public abstract void setHour(int var1);

   public abstract void setMinute(int var1);

   public abstract void setSecond(int var1);

   public abstract void setMillisecond(int var1);

   public abstract void setFractionalSecond(BigDecimal var1);

   public void setTime(int var1, int var2, int var3, BigDecimal var4) {
      this.setHour(var1);
      this.setMinute(var2);
      this.setSecond(var3);
      this.setFractionalSecond(var4);
   }

   public void setTime(int var1, int var2, int var3, int var4) {
      this.setHour(var1);
      this.setMinute(var2);
      this.setSecond(var3);
      this.setMillisecond(var4);
   }

   public abstract BigInteger getEon();

   public abstract int getYear();

   public abstract BigInteger getEonAndYear();

   public abstract int getMonth();

   public abstract int getDay();

   public abstract int getTimezone();

   public abstract int getHour();

   public abstract int getMinute();

   public abstract int getSecond();

   public int getMillisecond() {
      BigDecimal var1 = this.getFractionalSecond();
      return var1 == null ? Integer.MIN_VALUE : this.getFractionalSecond().movePointRight(3).intValue();
   }

   public abstract BigDecimal getFractionalSecond();

   public abstract int compare(XMLGregorianCalendar var1);

   public abstract XMLGregorianCalendar normalize();

   public boolean equals(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("Cannot test null for equality with this XMLGregorianCalendar");
      } else {
         boolean var2 = false;
         if (var1 instanceof XMLGregorianCalendar) {
            var2 = this.compare((XMLGregorianCalendar)var1) == 0;
         }

         return var2;
      }
   }

   public int hashCode() {
      int var1 = this.getTimezone();
      if (var1 == Integer.MIN_VALUE) {
         var1 = 0;
      }

      XMLGregorianCalendar var2 = this;
      if (var1 != 0) {
         var2 = this.normalize();
      }

      return var2.getYear() + var2.getMonth() + var2.getDay() + var2.getHour() + var2.getMinute() + var2.getSecond();
   }

   public abstract String toXMLFormat();

   public abstract QName getXMLSchemaType();

   public String toString() {
      return this.toXMLFormat();
   }

   public abstract boolean isValid();

   public abstract void add(Duration var1);

   public abstract GregorianCalendar toGregorianCalendar();

   public abstract GregorianCalendar toGregorianCalendar(TimeZone var1, Locale var2, XMLGregorianCalendar var3);

   public abstract TimeZone getTimeZone(int var1);

   public abstract Object clone();
}
