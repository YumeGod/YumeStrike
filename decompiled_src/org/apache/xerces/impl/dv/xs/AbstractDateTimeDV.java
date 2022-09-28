package org.apache.xerces.impl.dv.xs;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.jaxp.datatype.DatatypeFactoryImpl;
import org.apache.xerces.xs.datatypes.XSDateTime;

public abstract class AbstractDateTimeDV extends TypeValidator {
   private static final boolean DEBUG = false;
   protected static final int YEAR = 2000;
   protected static final int MONTH = 1;
   protected static final int DAY = 1;
   protected DatatypeFactory factory = new DatatypeFactoryImpl();

   public short getAllowedFacets() {
      return 2552;
   }

   public boolean isIdentical(Object var1, Object var2) {
      if (var1 instanceof DateTimeData && var2 instanceof DateTimeData) {
         DateTimeData var3 = (DateTimeData)var1;
         DateTimeData var4 = (DateTimeData)var2;
         return var3.timezoneHr == var4.timezoneHr && var3.timezoneMin == var4.timezoneMin ? var3.equals(var4) : false;
      } else {
         return false;
      }
   }

   public int compare(Object var1, Object var2) {
      return this.compareDates((DateTimeData)var1, (DateTimeData)var2, true);
   }

   protected short compareDates(DateTimeData var1, DateTimeData var2, boolean var3) {
      if (var1.utc == var2.utc) {
         return this.compareOrder(var1, var2);
      } else {
         DateTimeData var6 = new DateTimeData((String)null, this);
         short var4;
         short var5;
         if (var1.utc == 90) {
            this.cloneDate(var2, var6);
            var6.timezoneHr = 14;
            var6.timezoneMin = 0;
            var6.utc = 43;
            this.normalize(var6);
            var4 = this.compareOrder(var1, var6);
            if (var4 == -1) {
               return var4;
            } else {
               this.cloneDate(var2, var6);
               var6.timezoneHr = -14;
               var6.timezoneMin = 0;
               var6.utc = 45;
               this.normalize(var6);
               var5 = this.compareOrder(var1, var6);
               return var5 == 1 ? var5 : 2;
            }
         } else if (var2.utc == 90) {
            this.cloneDate(var1, var6);
            var6.timezoneHr = -14;
            var6.timezoneMin = 0;
            var6.utc = 45;
            this.normalize(var6);
            var4 = this.compareOrder(var6, var2);
            if (var4 == -1) {
               return var4;
            } else {
               this.cloneDate(var1, var6);
               var6.timezoneHr = 14;
               var6.timezoneMin = 0;
               var6.utc = 43;
               this.normalize(var6);
               var5 = this.compareOrder(var6, var2);
               return var5 == 1 ? var5 : 2;
            }
         } else {
            return 2;
         }
      }
   }

   protected short compareOrder(DateTimeData var1, DateTimeData var2) {
      if (var1.position < 1) {
         if (var1.year < var2.year) {
            return -1;
         }

         if (var1.year > var2.year) {
            return 1;
         }
      }

      if (var1.position < 2) {
         if (var1.month < var2.month) {
            return -1;
         }

         if (var1.month > var2.month) {
            return 1;
         }
      }

      if (var1.day < var2.day) {
         return -1;
      } else if (var1.day > var2.day) {
         return 1;
      } else if (var1.hour < var2.hour) {
         return -1;
      } else if (var1.hour > var2.hour) {
         return 1;
      } else if (var1.minute < var2.minute) {
         return -1;
      } else if (var1.minute > var2.minute) {
         return 1;
      } else if (var1.second < var2.second) {
         return -1;
      } else if (var1.second > var2.second) {
         return 1;
      } else if (var1.utc < var2.utc) {
         return -1;
      } else {
         return (short)(var1.utc > var2.utc ? 1 : 0);
      }
   }

   protected void getTime(String var1, int var2, int var3, DateTimeData var4) throws RuntimeException {
      int var5 = var2 + 2;
      var4.hour = this.parseInt(var1, var2, var5);
      if (var1.charAt(var5++) != ':') {
         throw new RuntimeException("Error in parsing time zone");
      } else {
         var2 = var5;
         var5 += 2;
         var4.minute = this.parseInt(var1, var2, var5);
         if (var1.charAt(var5++) != ':') {
            throw new RuntimeException("Error in parsing time zone");
         } else {
            int var6 = this.findUTCSign(var1, var2, var3);
            var2 = var5;
            var5 = var6 < 0 ? var3 : var6;
            var4.second = this.parseSecond(var1, var2, var5);
            if (var6 > 0) {
               this.getTimeZone(var1, var4, var6, var3);
            }

         }
      }
   }

   protected int getDate(String var1, int var2, int var3, DateTimeData var4) throws RuntimeException {
      var2 = this.getYearMonth(var1, var2, var3, var4);
      if (var1.charAt(var2++) != '-') {
         throw new RuntimeException("CCYY-MM must be followed by '-' sign");
      } else {
         int var5 = var2 + 2;
         var4.day = this.parseInt(var1, var2, var5);
         return var5;
      }
   }

   protected int getYearMonth(String var1, int var2, int var3, DateTimeData var4) throws RuntimeException {
      if (var1.charAt(0) == '-') {
         ++var2;
      }

      int var5 = this.indexOf(var1, var2, var3, '-');
      if (var5 == -1) {
         throw new RuntimeException("Year separator is missing or misplaced");
      } else {
         int var6 = var5 - var2;
         if (var6 < 4) {
            throw new RuntimeException("Year must have 'CCYY' format");
         } else if (var6 > 4 && var1.charAt(var2) == '0') {
            throw new RuntimeException("Leading zeros are required if the year value would otherwise have fewer than four digits; otherwise they are forbidden");
         } else {
            var4.year = this.parseIntYear(var1, var5);
            if (var1.charAt(var5) != '-') {
               throw new RuntimeException("CCYY must be followed by '-' sign");
            } else {
               ++var5;
               var2 = var5;
               var5 += 2;
               var4.month = this.parseInt(var1, var2, var5);
               return var5;
            }
         }
      }
   }

   protected void parseTimeZone(String var1, int var2, int var3, DateTimeData var4) throws RuntimeException {
      if (var2 < var3) {
         if (!this.isNextCharUTCSign(var1, var2, var3)) {
            throw new RuntimeException("Error in month parsing");
         }

         this.getTimeZone(var1, var4, var2, var3);
      }

   }

   protected void getTimeZone(String var1, DateTimeData var2, int var3, int var4) throws RuntimeException {
      var2.utc = var1.charAt(var3);
      if (var1.charAt(var3) == 'Z') {
         ++var3;
         if (var4 > var3) {
            throw new RuntimeException("Error in parsing time zone");
         }
      } else if (var3 <= var4 - 6) {
         int var5 = var1.charAt(var3) == '-' ? -1 : 1;
         ++var3;
         int var6 = var3 + 2;
         var2.timezoneHr = var5 * this.parseInt(var1, var3, var6);
         if (var1.charAt(var6++) != ':') {
            throw new RuntimeException("Error in parsing time zone");
         } else {
            var2.timezoneMin = var5 * this.parseInt(var1, var6, var6 + 2);
            if (var6 + 2 != var4) {
               throw new RuntimeException("Error in parsing time zone");
            } else {
               if (var2.timezoneHr != 0 || var2.timezoneMin != 0) {
                  var2.normalized = false;
               }

            }
         }
      } else {
         throw new RuntimeException("Error in parsing time zone");
      }
   }

   protected int indexOf(String var1, int var2, int var3, char var4) {
      for(int var5 = var2; var5 < var3; ++var5) {
         if (var1.charAt(var5) == var4) {
            return var5;
         }
      }

      return -1;
   }

   protected void validateDateTime(DateTimeData var1) {
      if (var1.year == 0) {
         throw new RuntimeException("The year \"0000\" is an illegal year value");
      } else if (var1.month >= 1 && var1.month <= 12) {
         if (var1.day <= this.maxDayInMonthFor(var1.year, var1.month) && var1.day >= 1) {
            if (var1.hour > 23 || var1.hour < 0) {
               if (var1.hour != 24 || var1.minute != 0 || var1.second != 0.0) {
                  throw new RuntimeException("Hour must have values 0-23, unless 24:00:00");
               }

               var1.hour = 0;
               if (++var1.day > this.maxDayInMonthFor(var1.year, var1.month)) {
                  var1.day = 1;
                  if (++var1.month > 12) {
                     var1.month = 1;
                     if (++var1.year == 0) {
                        var1.year = 1;
                     }
                  }
               }
            }

            if (var1.minute <= 59 && var1.minute >= 0) {
               if (!(var1.second >= 60.0) && !(var1.second < 0.0)) {
                  if (var1.timezoneHr <= 14 && var1.timezoneHr >= -14) {
                     if ((var1.timezoneHr == 14 || var1.timezoneHr == -14) && var1.timezoneMin != 0) {
                        throw new RuntimeException("Time zone should have range -14:00 to +14:00");
                     } else if (var1.timezoneMin > 59 || var1.timezoneMin < -59) {
                        throw new RuntimeException("Minute must have values 0-59");
                     }
                  } else {
                     throw new RuntimeException("Time zone should have range -14:00 to +14:00");
                  }
               } else {
                  throw new RuntimeException("Second must have values 0-59");
               }
            } else {
               throw new RuntimeException("Minute must have values 0-59");
            }
         } else {
            throw new RuntimeException("The day must have values 1 to 31");
         }
      } else {
         throw new RuntimeException("The month must have values 1 to 12");
      }
   }

   protected int findUTCSign(String var1, int var2, int var3) {
      for(int var5 = var2; var5 < var3; ++var5) {
         char var4 = var1.charAt(var5);
         if (var4 == 'Z' || var4 == '+' || var4 == '-') {
            return var5;
         }
      }

      return -1;
   }

   protected final boolean isNextCharUTCSign(String var1, int var2, int var3) {
      if (var2 >= var3) {
         return false;
      } else {
         char var4 = var1.charAt(var2);
         return var4 == 'Z' || var4 == '+' || var4 == '-';
      }
   }

   protected int parseInt(String var1, int var2, int var3) throws NumberFormatException {
      byte var4 = 10;
      int var5 = 0;
      boolean var6 = false;
      int var7 = -2147483647;
      int var8 = var7 / var4;
      int var9 = var2;

      do {
         int var10 = TypeValidator.getDigit(var1.charAt(var9));
         if (var10 < 0) {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }

         if (var5 < var8) {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }

         var5 *= var4;
         if (var5 < var7 + var10) {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }

         var5 -= var10;
         ++var9;
      } while(var9 < var3);

      return -var5;
   }

   protected int parseIntYear(String var1, int var2) {
      byte var3 = 10;
      int var4 = 0;
      boolean var5 = false;
      int var6 = 0;
      boolean var9 = false;
      int var7;
      if (var1.charAt(0) == '-') {
         var5 = true;
         var7 = Integer.MIN_VALUE;
         ++var6;
      } else {
         var7 = -2147483647;
      }

      int var10;
      for(int var8 = var7 / var3; var6 < var2; var4 -= var10) {
         var10 = TypeValidator.getDigit(var1.charAt(var6++));
         if (var10 < 0) {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }

         if (var4 < var8) {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }

         var4 *= var3;
         if (var4 < var7 + var10) {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }
      }

      if (var5) {
         if (var6 > 1) {
            return var4;
         } else {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }
      } else {
         return -var4;
      }
   }

   protected void normalize(DateTimeData var1) {
      byte var2 = -1;
      int var3 = var1.minute + var2 * var1.timezoneMin;
      int var4 = this.fQuotient(var3, 60);
      var1.minute = this.mod(var3, 60, var4);
      var3 = var1.hour + var2 * var1.timezoneHr + var4;
      var4 = this.fQuotient(var3, 24);
      var1.hour = this.mod(var3, 24, var4);
      var1.day += var4;

      while(true) {
         do {
            var3 = this.maxDayInMonthFor(var1.year, var1.month);
            byte var5;
            if (var1.day < 1) {
               var1.day += this.maxDayInMonthFor(var1.year, var1.month - 1);
               var5 = -1;
            } else {
               if (var1.day <= var3) {
                  var1.utc = 90;
                  return;
               }

               var1.day -= var3;
               var5 = 1;
            }

            var3 = var1.month + var5;
            var1.month = this.modulo(var3, 1, 13);
            var1.year += this.fQuotient(var3, 1, 13);
         } while(var1.year != 0);

         var1.year = var1.timezoneHr >= 0 && var1.timezoneMin >= 0 ? -1 : 1;
      }
   }

   protected void saveUnnormalized(DateTimeData var1) {
      var1.unNormYear = var1.year;
      var1.unNormMonth = var1.month;
      var1.unNormDay = var1.day;
      var1.unNormHour = var1.hour;
      var1.unNormMinute = var1.minute;
      var1.unNormSecond = var1.second;
   }

   protected void resetDateObj(DateTimeData var1) {
      var1.year = 0;
      var1.month = 0;
      var1.day = 0;
      var1.hour = 0;
      var1.minute = 0;
      var1.second = 0.0;
      var1.utc = 0;
      var1.timezoneHr = 0;
      var1.timezoneMin = 0;
   }

   protected int maxDayInMonthFor(int var1, int var2) {
      if (var2 != 4 && var2 != 6 && var2 != 9 && var2 != 11) {
         if (var2 == 2) {
            return this.isLeapYear(var1) ? 29 : 28;
         } else {
            return 31;
         }
      } else {
         return 30;
      }
   }

   private boolean isLeapYear(int var1) {
      return var1 % 4 == 0 && (var1 % 100 != 0 || var1 % 400 == 0);
   }

   protected int mod(int var1, int var2, int var3) {
      return var1 - var3 * var2;
   }

   protected int fQuotient(int var1, int var2) {
      return (int)Math.floor((double)((float)var1 / (float)var2));
   }

   protected int modulo(int var1, int var2, int var3) {
      int var4 = var1 - var2;
      int var5 = var3 - var2;
      return this.mod(var4, var5, this.fQuotient(var4, var5)) + var2;
   }

   protected int fQuotient(int var1, int var2, int var3) {
      return this.fQuotient(var1 - var2, var3 - var2);
   }

   protected String dateToString(DateTimeData var1) {
      StringBuffer var2 = new StringBuffer(25);
      this.append(var2, var1.year, 4);
      var2.append('-');
      this.append(var2, var1.month, 2);
      var2.append('-');
      this.append(var2, var1.day, 2);
      var2.append('T');
      this.append(var2, var1.hour, 2);
      var2.append(':');
      this.append(var2, var1.minute, 2);
      var2.append(':');
      this.append(var2, var1.second);
      this.append(var2, (char)var1.utc, 0);
      return var2.toString();
   }

   protected void append(StringBuffer var1, int var2, int var3) {
      if (var2 == Integer.MIN_VALUE) {
         var1.append(var2);
      } else {
         if (var2 < 0) {
            var1.append('-');
            var2 = -var2;
         }

         if (var3 == 4) {
            if (var2 < 10) {
               var1.append("000");
            } else if (var2 < 100) {
               var1.append("00");
            } else if (var2 < 1000) {
               var1.append("0");
            }

            var1.append(var2);
         } else if (var3 == 2) {
            if (var2 < 10) {
               var1.append('0');
            }

            var1.append(var2);
         } else if (var2 != 0) {
            var1.append((char)var2);
         }

      }
   }

   protected void append(StringBuffer var1, double var2) {
      if (var2 < 0.0) {
         var1.append('-');
         var2 = -var2;
      }

      if (var2 < 10.0) {
         var1.append('0');
      }

      var1.append(var2);
   }

   protected double parseSecond(String var1, int var2, int var3) throws NumberFormatException {
      int var4 = -1;

      for(int var5 = var2; var5 < var3; ++var5) {
         char var6 = var1.charAt(var5);
         if (var6 == '.') {
            var4 = var5;
         } else if (var6 > '9' || var6 < '0') {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }
      }

      if (var4 == -1) {
         if (var2 + 2 != var3) {
            throw new NumberFormatException("'" + var1 + "' has wrong format");
         }
      } else if (var2 + 2 != var4 || var4 + 1 == var3) {
         throw new NumberFormatException("'" + var1 + "' has wrong format");
      }

      return Double.parseDouble(var1.substring(var2, var3));
   }

   private void cloneDate(DateTimeData var1, DateTimeData var2) {
      var2.year = var1.year;
      var2.month = var1.month;
      var2.day = var1.day;
      var2.hour = var1.hour;
      var2.minute = var1.minute;
      var2.second = var1.second;
      var2.utc = var1.utc;
      var2.timezoneHr = var1.timezoneHr;
      var2.timezoneMin = var1.timezoneMin;
   }

   protected XMLGregorianCalendar getXMLGregorianCalendar(DateTimeData var1) {
      return null;
   }

   protected Duration getDuration(DateTimeData var1) {
      return null;
   }

   static final class DateTimeData implements XSDateTime {
      int year;
      int month;
      int day;
      int hour;
      int minute;
      int utc;
      double second;
      int timezoneHr;
      int timezoneMin;
      private String originalValue;
      boolean normalized = true;
      int unNormYear;
      int unNormMonth;
      int unNormDay;
      int unNormHour;
      int unNormMinute;
      double unNormSecond;
      int position;
      final AbstractDateTimeDV type;
      private String canonical;

      public DateTimeData(String var1, AbstractDateTimeDV var2) {
         this.originalValue = var1;
         this.type = var2;
      }

      public DateTimeData(int var1, int var2, int var3, int var4, int var5, double var6, int var8, String var9, boolean var10, AbstractDateTimeDV var11) {
         this.year = var1;
         this.month = var2;
         this.day = var3;
         this.hour = var4;
         this.minute = var5;
         this.second = var6;
         this.utc = var8;
         this.type = var11;
         this.originalValue = var9;
      }

      public boolean equals(Object var1) {
         if (!(var1 instanceof DateTimeData)) {
            return false;
         } else {
            return this.type.compareDates(this, (DateTimeData)var1, true) == 0;
         }
      }

      public synchronized String toString() {
         if (this.canonical == null) {
            this.canonical = this.type.dateToString(this);
         }

         return this.canonical;
      }

      public int getYears() {
         if (this.type instanceof DurationDV) {
            return 0;
         } else {
            return this.normalized ? this.year : this.unNormYear;
         }
      }

      public int getMonths() {
         if (this.type instanceof DurationDV) {
            return this.year * 12 + this.month;
         } else {
            return this.normalized ? this.month : this.unNormMonth;
         }
      }

      public int getDays() {
         if (this.type instanceof DurationDV) {
            return 0;
         } else {
            return this.normalized ? this.day : this.unNormDay;
         }
      }

      public int getHours() {
         if (this.type instanceof DurationDV) {
            return 0;
         } else {
            return this.normalized ? this.hour : this.unNormHour;
         }
      }

      public int getMinutes() {
         if (this.type instanceof DurationDV) {
            return 0;
         } else {
            return this.normalized ? this.minute : this.unNormMinute;
         }
      }

      public double getSeconds() {
         if (this.type instanceof DurationDV) {
            return (double)(this.day * 24 * 60 * 60 + this.hour * 60 * 60 + this.minute * 60) + this.second;
         } else {
            return this.normalized ? this.second : this.unNormSecond;
         }
      }

      public boolean hasTimeZone() {
         return this.utc != 0;
      }

      public int getTimeZoneHours() {
         return this.timezoneHr;
      }

      public int getTimeZoneMinutes() {
         return this.timezoneMin;
      }

      public String getLexicalValue() {
         return this.originalValue;
      }

      public XSDateTime normalize() {
         if (!this.normalized) {
            DateTimeData var1 = (DateTimeData)this.clone();
            var1.normalized = true;
            return var1;
         } else {
            return this;
         }
      }

      public boolean isNormalized() {
         return this.normalized;
      }

      public Object clone() {
         DateTimeData var1 = new DateTimeData(this.year, this.month, this.day, this.hour, this.minute, this.second, this.utc, this.originalValue, this.normalized, this.type);
         var1.canonical = this.canonical;
         var1.position = this.position;
         var1.timezoneHr = this.timezoneHr;
         var1.timezoneMin = this.timezoneMin;
         var1.unNormYear = this.unNormYear;
         var1.unNormMonth = this.unNormMonth;
         var1.unNormDay = this.unNormDay;
         var1.unNormHour = this.unNormHour;
         var1.unNormMinute = this.unNormMinute;
         var1.unNormSecond = this.unNormSecond;
         return var1;
      }

      public XMLGregorianCalendar getXMLGregorianCalendar() {
         return this.type.getXMLGregorianCalendar(this);
      }

      public Duration getDuration() {
         return this.type.getDuration(this);
      }
   }
}
