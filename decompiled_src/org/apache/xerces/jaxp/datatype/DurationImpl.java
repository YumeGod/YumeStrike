package org.apache.xerces.jaxp.datatype;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.xerces.util.DatatypeMessageFormatter;

class DurationImpl extends Duration implements Serializable {
   private static final int FIELD_NUM = 6;
   private static final DatatypeConstants.Field[] FIELDS;
   private static final int[] FIELD_IDS;
   private static final BigDecimal ZERO;
   private final int signum;
   private final BigInteger years;
   private final BigInteger months;
   private final BigInteger days;
   private final BigInteger hours;
   private final BigInteger minutes;
   private final BigDecimal seconds;
   private static final XMLGregorianCalendar[] TEST_POINTS;
   private static final BigDecimal[] FACTORS;
   private static final long serialVersionUID = 1L;

   public int getSign() {
      return this.signum;
   }

   private int calcSignum(boolean var1) {
      if ((this.years == null || this.years.signum() == 0) && (this.months == null || this.months.signum() == 0) && (this.days == null || this.days.signum() == 0) && (this.hours == null || this.hours.signum() == 0) && (this.minutes == null || this.minutes.signum() == 0) && (this.seconds == null || this.seconds.signum() == 0)) {
         return 0;
      } else {
         return var1 ? 1 : -1;
      }
   }

   protected DurationImpl(boolean var1, BigInteger var2, BigInteger var3, BigInteger var4, BigInteger var5, BigInteger var6, BigDecimal var7) {
      this.years = var2;
      this.months = var3;
      this.days = var4;
      this.hours = var5;
      this.minutes = var6;
      this.seconds = var7;
      this.signum = this.calcSignum(var1);
      if (var2 == null && var3 == null && var4 == null && var5 == null && var6 == null && var7 == null) {
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "AllFieldsNull", (Object[])null));
      } else {
         testNonNegative(var2, DatatypeConstants.YEARS);
         testNonNegative(var3, DatatypeConstants.MONTHS);
         testNonNegative(var4, DatatypeConstants.DAYS);
         testNonNegative(var5, DatatypeConstants.HOURS);
         testNonNegative(var6, DatatypeConstants.MINUTES);
         testNonNegative(var7, DatatypeConstants.SECONDS);
      }
   }

   private static void testNonNegative(BigInteger var0, DatatypeConstants.Field var1) {
      if (var0 != null && var0.signum() < 0) {
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "NegativeField", new Object[]{var1.toString()}));
      }
   }

   private static void testNonNegative(BigDecimal var0, DatatypeConstants.Field var1) {
      if (var0 != null && var0.signum() < 0) {
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "NegativeField", new Object[]{var1.toString()}));
      }
   }

   protected DurationImpl(boolean var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      this(var1, wrap(var2), wrap(var3), wrap(var4), wrap(var5), wrap(var6), var7 != 0 ? new BigDecimal(String.valueOf(var7)) : null);
   }

   private static BigInteger wrap(int var0) {
      return var0 == Integer.MIN_VALUE ? null : new BigInteger(String.valueOf(var0));
   }

   protected DurationImpl(long var1) {
      boolean var3 = false;
      long var4 = var1;
      if (var1 > 0L) {
         this.signum = 1;
      } else if (var1 < 0L) {
         this.signum = -1;
         if (var1 == Long.MIN_VALUE) {
            var4 = var1 + 1L;
            var3 = true;
         }

         var4 *= -1L;
      } else {
         this.signum = 0;
      }

      this.years = null;
      this.months = null;
      this.seconds = BigDecimal.valueOf(var4 % 60000L + (var3 ? 1L : 0L), 3);
      var4 /= 60000L;
      this.minutes = var4 == 0L ? null : BigInteger.valueOf(var4 % 60L);
      var4 /= 60L;
      this.hours = var4 == 0L ? null : BigInteger.valueOf(var4 % 24L);
      var4 /= 24L;
      this.days = var4 == 0L ? null : BigInteger.valueOf(var4);
   }

   protected DurationImpl(String var1) throws IllegalArgumentException {
      String var2 = var1;
      int[] var4 = new int[1];
      int var5 = var1.length();
      boolean var6 = false;
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         var4[0] = 0;
         boolean var3;
         if (var5 != var4[0] && var1.charAt(var4[0]) == '-') {
            int var10002 = var4[0]++;
            var3 = false;
         } else {
            var3 = true;
         }

         int var10001;
         int var10004;
         if (var5 != var4[0]) {
            var10004 = var4[0];
            var10001 = var4[0];
            var4[0] = var10004 + 1;
            if (var1.charAt(var10001) != 'P') {
               throw new IllegalArgumentException(var1);
            }
         }

         int var7 = 0;
         String[] var8 = new String[3];

         int[] var9;
         for(var9 = new int[3]; var5 != var4[0] && isDigit(var2.charAt(var4[0])) && var7 < 3; var8[var7++] = parsePiece(var2, var4)) {
            var9[var7] = var4[0];
         }

         if (var5 != var4[0]) {
            var10004 = var4[0];
            var10001 = var4[0];
            var4[0] = var10004 + 1;
            if (var2.charAt(var10001) != 'T') {
               throw new IllegalArgumentException(var2);
            }

            var6 = true;
         }

         int var10 = 0;
         String[] var11 = new String[3];

         int[] var12;
         for(var12 = new int[3]; var5 != var4[0] && isDigitOrPeriod(var2.charAt(var4[0])) && var10 < 3; var11[var10++] = parsePiece(var2, var4)) {
            var12[var10] = var4[0];
         }

         if (var6 && var10 == 0) {
            throw new IllegalArgumentException(var2);
         } else if (var5 != var4[0]) {
            throw new IllegalArgumentException(var2);
         } else if (var7 == 0 && var10 == 0) {
            throw new IllegalArgumentException(var2);
         } else {
            organizeParts(var2, var8, var9, var7, "YMD");
            organizeParts(var2, var11, var12, var10, "HMS");
            this.years = parseBigInteger(var2, var8[0], var9[0]);
            this.months = parseBigInteger(var2, var8[1], var9[1]);
            this.days = parseBigInteger(var2, var8[2], var9[2]);
            this.hours = parseBigInteger(var2, var11[0], var12[0]);
            this.minutes = parseBigInteger(var2, var11[1], var12[1]);
            this.seconds = parseBigDecimal(var2, var11[2], var12[2]);
            this.signum = this.calcSignum(var3);
         }
      }
   }

   private static boolean isDigit(char var0) {
      return '0' <= var0 && var0 <= '9';
   }

   private static boolean isDigitOrPeriod(char var0) {
      return isDigit(var0) || var0 == '.';
   }

   private static String parsePiece(String var0, int[] var1) throws IllegalArgumentException {
      int var2;
      int var10002;
      for(var2 = var1[0]; var1[0] < var0.length() && isDigitOrPeriod(var0.charAt(var1[0])); var10002 = var1[0]++) {
      }

      if (var1[0] == var0.length()) {
         throw new IllegalArgumentException(var0);
      } else {
         var10002 = var1[0]++;
         return var0.substring(var2, var1[0]);
      }
   }

   private static void organizeParts(String var0, String[] var1, int[] var2, int var3, String var4) throws IllegalArgumentException {
      int var5 = var4.length();

      for(int var6 = var3 - 1; var6 >= 0; --var6) {
         int var7 = var4.lastIndexOf(var1[var6].charAt(var1[var6].length() - 1), var5 - 1);
         if (var7 == -1) {
            throw new IllegalArgumentException(var0);
         }

         for(int var8 = var7 + 1; var8 < var5; ++var8) {
            var1[var8] = null;
         }

         var5 = var7;
         var1[var7] = var1[var6];
         var2[var7] = var2[var6];
      }

      --var5;

      while(var5 >= 0) {
         var1[var5] = null;
         --var5;
      }

   }

   private static BigInteger parseBigInteger(String var0, String var1, int var2) throws IllegalArgumentException {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.substring(0, var1.length() - 1);
         return new BigInteger(var1);
      }
   }

   private static BigDecimal parseBigDecimal(String var0, String var1, int var2) throws IllegalArgumentException {
      if (var1 == null) {
         return null;
      } else {
         var1 = var1.substring(0, var1.length() - 1);
         return new BigDecimal(var1);
      }
   }

   public int compare(Duration var1) {
      BigInteger var2 = BigInteger.valueOf(2147483647L);
      BigInteger var3 = BigInteger.valueOf(-2147483648L);
      if (this.years != null && this.years.compareTo(var2) == 1) {
         throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.YEARS.toString(), this.years.toString()}));
      } else if (this.months != null && this.months.compareTo(var2) == 1) {
         throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MONTHS.toString(), this.months.toString()}));
      } else if (this.days != null && this.days.compareTo(var2) == 1) {
         throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.DAYS.toString(), this.days.toString()}));
      } else if (this.hours != null && this.hours.compareTo(var2) == 1) {
         throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.HOURS.toString(), this.hours.toString()}));
      } else if (this.minutes != null && this.minutes.compareTo(var2) == 1) {
         throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MINUTES.toString(), this.minutes.toString()}));
      } else if (this.seconds != null && this.seconds.toBigInteger().compareTo(var2) == 1) {
         throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.SECONDS.toString(), this.seconds.toString()}));
      } else {
         BigInteger var4 = (BigInteger)var1.getField(DatatypeConstants.YEARS);
         if (var4 != null && var4.compareTo(var2) == 1) {
            throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.YEARS.toString(), var4.toString()}));
         } else {
            BigInteger var5 = (BigInteger)var1.getField(DatatypeConstants.MONTHS);
            if (var5 != null && var5.compareTo(var2) == 1) {
               throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MONTHS.toString(), var5.toString()}));
            } else {
               BigInteger var6 = (BigInteger)var1.getField(DatatypeConstants.DAYS);
               if (var6 != null && var6.compareTo(var2) == 1) {
                  throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.DAYS.toString(), var6.toString()}));
               } else {
                  BigInteger var7 = (BigInteger)var1.getField(DatatypeConstants.HOURS);
                  if (var7 != null && var7.compareTo(var2) == 1) {
                     throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.HOURS.toString(), var7.toString()}));
                  } else {
                     BigInteger var8 = (BigInteger)var1.getField(DatatypeConstants.MINUTES);
                     if (var8 != null && var8.compareTo(var2) == 1) {
                        throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.MINUTES.toString(), var8.toString()}));
                     } else {
                        BigDecimal var9 = (BigDecimal)var1.getField(DatatypeConstants.SECONDS);
                        BigInteger var10 = null;
                        if (var9 != null) {
                           var10 = var9.toBigInteger();
                        }

                        if (var10 != null && var10.compareTo(var2) == 1) {
                           throw new UnsupportedOperationException(DatatypeMessageFormatter.formatMessage((Locale)null, "TooLarge", new Object[]{this.getClass().getName() + "#compare(Duration duration)" + DatatypeConstants.SECONDS.toString(), var10.toString()}));
                        } else {
                           GregorianCalendar var11 = new GregorianCalendar(1970, 1, 1, 0, 0, 0);
                           var11.add(1, this.getYears() * this.getSign());
                           var11.add(2, this.getMonths() * this.getSign());
                           var11.add(6, this.getDays() * this.getSign());
                           var11.add(11, this.getHours() * this.getSign());
                           var11.add(12, this.getMinutes() * this.getSign());
                           var11.add(13, this.getSeconds() * this.getSign());
                           GregorianCalendar var12 = new GregorianCalendar(1970, 1, 1, 0, 0, 0);
                           var12.add(1, var1.getYears() * var1.getSign());
                           var12.add(2, var1.getMonths() * var1.getSign());
                           var12.add(6, var1.getDays() * var1.getSign());
                           var12.add(11, var1.getHours() * var1.getSign());
                           var12.add(12, var1.getMinutes() * var1.getSign());
                           var12.add(13, var1.getSeconds() * var1.getSign());
                           return var11.equals(var12) ? 0 : this.compareDates(this, var1);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private int compareDates(Duration var1, Duration var2) {
      boolean var3 = true;
      boolean var4 = true;
      XMLGregorianCalendar var5 = (XMLGregorianCalendar)TEST_POINTS[0].clone();
      XMLGregorianCalendar var6 = (XMLGregorianCalendar)TEST_POINTS[0].clone();
      var5.add(var1);
      var6.add(var2);
      int var7 = var5.compare(var6);
      if (var7 == 2) {
         return 2;
      } else {
         var5 = (XMLGregorianCalendar)TEST_POINTS[1].clone();
         var6 = (XMLGregorianCalendar)TEST_POINTS[1].clone();
         var5.add(var1);
         var6.add(var2);
         int var8 = var5.compare(var6);
         var7 = this.compareResults(var7, var8);
         if (var7 == 2) {
            return 2;
         } else {
            var5 = (XMLGregorianCalendar)TEST_POINTS[2].clone();
            var6 = (XMLGregorianCalendar)TEST_POINTS[2].clone();
            var5.add(var1);
            var6.add(var2);
            var8 = var5.compare(var6);
            var7 = this.compareResults(var7, var8);
            if (var7 == 2) {
               return 2;
            } else {
               var5 = (XMLGregorianCalendar)TEST_POINTS[3].clone();
               var6 = (XMLGregorianCalendar)TEST_POINTS[3].clone();
               var5.add(var1);
               var6.add(var2);
               var8 = var5.compare(var6);
               var7 = this.compareResults(var7, var8);
               return var7;
            }
         }
      }
   }

   private int compareResults(int var1, int var2) {
      if (var2 == 2) {
         return 2;
      } else {
         return var1 != var2 ? 2 : var1;
      }
   }

   public int hashCode() {
      GregorianCalendar var1 = TEST_POINTS[0].toGregorianCalendar();
      this.addTo((Calendar)var1);
      return (int)getCalendarTimeInMillis(var1);
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      if (this.signum < 0) {
         var1.append('-');
      }

      var1.append('P');
      if (this.years != null) {
         var1.append(this.years + "Y");
      }

      if (this.months != null) {
         var1.append(this.months + "M");
      }

      if (this.days != null) {
         var1.append(this.days + "D");
      }

      if (this.hours != null || this.minutes != null || this.seconds != null) {
         var1.append('T');
         if (this.hours != null) {
            var1.append(this.hours + "H");
         }

         if (this.minutes != null) {
            var1.append(this.minutes + "M");
         }

         if (this.seconds != null) {
            var1.append(this.toString(this.seconds) + "S");
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

   public boolean isSet(DatatypeConstants.Field var1) {
      String var2;
      if (var1 == null) {
         var2 = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field)";
         throw new NullPointerException(DatatypeMessageFormatter.formatMessage((Locale)null, "FieldCannotBeNull", new Object[]{var2}));
      } else if (var1 == DatatypeConstants.YEARS) {
         return this.years != null;
      } else if (var1 == DatatypeConstants.MONTHS) {
         return this.months != null;
      } else if (var1 == DatatypeConstants.DAYS) {
         return this.days != null;
      } else if (var1 == DatatypeConstants.HOURS) {
         return this.hours != null;
      } else if (var1 == DatatypeConstants.MINUTES) {
         return this.minutes != null;
      } else if (var1 == DatatypeConstants.SECONDS) {
         return this.seconds != null;
      } else {
         var2 = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field)";
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "UnknownField", new Object[]{var2, var1.toString()}));
      }
   }

   public Number getField(DatatypeConstants.Field var1) {
      String var2;
      if (var1 == null) {
         var2 = "javax.xml.datatype.Duration#isSet(DatatypeConstants.Field field) ";
         throw new NullPointerException(DatatypeMessageFormatter.formatMessage((Locale)null, "FieldCannotBeNull", new Object[]{var2}));
      } else if (var1 == DatatypeConstants.YEARS) {
         return this.years;
      } else if (var1 == DatatypeConstants.MONTHS) {
         return this.months;
      } else if (var1 == DatatypeConstants.DAYS) {
         return this.days;
      } else if (var1 == DatatypeConstants.HOURS) {
         return this.hours;
      } else if (var1 == DatatypeConstants.MINUTES) {
         return this.minutes;
      } else if (var1 == DatatypeConstants.SECONDS) {
         return this.seconds;
      } else {
         var2 = "javax.xml.datatype.Duration#(getSet(DatatypeConstants.Field field)";
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "UnknownField", new Object[]{var2, var1.toString()}));
      }
   }

   public int getYears() {
      return this.getInt(DatatypeConstants.YEARS);
   }

   public int getMonths() {
      return this.getInt(DatatypeConstants.MONTHS);
   }

   public int getDays() {
      return this.getInt(DatatypeConstants.DAYS);
   }

   public int getHours() {
      return this.getInt(DatatypeConstants.HOURS);
   }

   public int getMinutes() {
      return this.getInt(DatatypeConstants.MINUTES);
   }

   public int getSeconds() {
      return this.getInt(DatatypeConstants.SECONDS);
   }

   private int getInt(DatatypeConstants.Field var1) {
      Number var2 = this.getField(var1);
      return var2 == null ? 0 : var2.intValue();
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

   public Duration normalizeWith(Calendar var1) {
      Calendar var2 = (Calendar)var1.clone();
      var2.add(1, this.getYears() * this.signum);
      var2.add(2, this.getMonths() * this.signum);
      var2.add(5, this.getDays() * this.signum);
      long var3 = getCalendarTimeInMillis(var2) - getCalendarTimeInMillis(var1);
      int var5 = (int)(var3 / 86400000L);
      return new DurationImpl(var5 >= 0, (BigInteger)null, (BigInteger)null, wrap(Math.abs(var5)), (BigInteger)this.getField(DatatypeConstants.HOURS), (BigInteger)this.getField(DatatypeConstants.MINUTES), (BigDecimal)this.getField(DatatypeConstants.SECONDS));
   }

   public Duration multiply(int var1) {
      return this.multiply(BigDecimal.valueOf((long)var1));
   }

   public Duration multiply(BigDecimal var1) {
      BigDecimal var2 = ZERO;
      int var3 = var1.signum();
      var1 = var1.abs();
      BigDecimal[] var4 = new BigDecimal[6];

      for(int var5 = 0; var5 < 5; ++var5) {
         BigDecimal var6 = this.getFieldAsBigDecimal(FIELDS[var5]);
         var6 = var6.multiply(var1).add(var2);
         var4[var5] = var6.setScale(0, 1);
         var6 = var6.subtract(var4[var5]);
         if (var5 == 1) {
            if (var6.signum() != 0) {
               throw new IllegalStateException();
            }

            var2 = ZERO;
         } else {
            var2 = var6.multiply(FACTORS[var5]);
         }
      }

      if (this.seconds != null) {
         var4[5] = this.seconds.multiply(var1).add(var2);
      } else {
         var4[5] = var2;
      }

      return new DurationImpl(this.signum * var3 >= 0, toBigInteger(var4[0], null == this.years), toBigInteger(var4[1], null == this.months), toBigInteger(var4[2], null == this.days), toBigInteger(var4[3], null == this.hours), toBigInteger(var4[4], null == this.minutes), var4[5].signum() == 0 && this.seconds == null ? null : var4[5]);
   }

   private BigDecimal getFieldAsBigDecimal(DatatypeConstants.Field var1) {
      if (var1 == DatatypeConstants.SECONDS) {
         return this.seconds != null ? this.seconds : ZERO;
      } else {
         BigInteger var2 = (BigInteger)this.getField(var1);
         return var2 == null ? ZERO : new BigDecimal(var2);
      }
   }

   private static BigInteger toBigInteger(BigDecimal var0, boolean var1) {
      return var1 && var0.signum() == 0 ? null : var0.unscaledValue();
   }

   public Duration add(Duration var1) {
      BigDecimal[] var3 = new BigDecimal[]{sanitize((BigInteger)this.getField(DatatypeConstants.YEARS), this.getSign()).add(sanitize((BigInteger)var1.getField(DatatypeConstants.YEARS), var1.getSign())), sanitize((BigInteger)this.getField(DatatypeConstants.MONTHS), this.getSign()).add(sanitize((BigInteger)var1.getField(DatatypeConstants.MONTHS), var1.getSign())), sanitize((BigInteger)this.getField(DatatypeConstants.DAYS), this.getSign()).add(sanitize((BigInteger)var1.getField(DatatypeConstants.DAYS), var1.getSign())), sanitize((BigInteger)this.getField(DatatypeConstants.HOURS), this.getSign()).add(sanitize((BigInteger)var1.getField(DatatypeConstants.HOURS), var1.getSign())), sanitize((BigInteger)this.getField(DatatypeConstants.MINUTES), this.getSign()).add(sanitize((BigInteger)var1.getField(DatatypeConstants.MINUTES), var1.getSign())), sanitize((BigDecimal)this.getField(DatatypeConstants.SECONDS), this.getSign()).add(sanitize((BigDecimal)var1.getField(DatatypeConstants.SECONDS), var1.getSign()))};
      alignSigns(var3, 0, 2);
      alignSigns(var3, 2, 6);
      int var4 = 0;

      for(int var5 = 0; var5 < 6; ++var5) {
         if (var4 * var3[var5].signum() < 0) {
            throw new IllegalStateException();
         }

         if (var4 == 0) {
            var4 = var3[var5].signum();
         }
      }

      return new DurationImpl(var4 >= 0, toBigInteger(sanitize(var3[0], var4), this.getField(DatatypeConstants.YEARS) == null && var1.getField(DatatypeConstants.YEARS) == null), toBigInteger(sanitize(var3[1], var4), this.getField(DatatypeConstants.MONTHS) == null && var1.getField(DatatypeConstants.MONTHS) == null), toBigInteger(sanitize(var3[2], var4), this.getField(DatatypeConstants.DAYS) == null && var1.getField(DatatypeConstants.DAYS) == null), toBigInteger(sanitize(var3[3], var4), this.getField(DatatypeConstants.HOURS) == null && var1.getField(DatatypeConstants.HOURS) == null), toBigInteger(sanitize(var3[4], var4), this.getField(DatatypeConstants.MINUTES) == null && var1.getField(DatatypeConstants.MINUTES) == null), var3[5].signum() == 0 && this.getField(DatatypeConstants.SECONDS) == null && var1.getField(DatatypeConstants.SECONDS) == null ? null : sanitize(var3[5], var4));
   }

   private static void alignSigns(BigDecimal[] var0, int var1, int var2) {
      boolean var3;
      do {
         var3 = false;
         int var4 = 0;

         for(int var5 = var1; var5 < var2; ++var5) {
            if (var4 * var0[var5].signum() < 0) {
               var3 = true;
               BigDecimal var6 = var0[var5].abs().divide(FACTORS[var5 - 1], 0);
               if (var0[var5].signum() > 0) {
                  var6 = var6.negate();
               }

               var0[var5 - 1] = var0[var5 - 1].subtract(var6);
               var0[var5] = var0[var5].add(var6.multiply(FACTORS[var5 - 1]));
            }

            if (var0[var5].signum() != 0) {
               var4 = var0[var5].signum();
            }
         }
      } while(var3);

   }

   private static BigDecimal sanitize(BigInteger var0, int var1) {
      if (var1 != 0 && var0 != null) {
         return var1 > 0 ? new BigDecimal(var0) : new BigDecimal(var0.negate());
      } else {
         return ZERO;
      }
   }

   static BigDecimal sanitize(BigDecimal var0, int var1) {
      if (var1 != 0 && var0 != null) {
         return var1 > 0 ? var0 : var0.negate();
      } else {
         return ZERO;
      }
   }

   public Duration subtract(Duration var1) {
      return this.add(var1.negate());
   }

   public Duration negate() {
      return new DurationImpl(this.signum <= 0, this.years, this.months, this.days, this.hours, this.minutes, this.seconds);
   }

   public int signum() {
      return this.signum;
   }

   public void addTo(Calendar var1) {
      var1.add(1, this.getYears() * this.signum);
      var1.add(2, this.getMonths() * this.signum);
      var1.add(5, this.getDays() * this.signum);
      var1.add(10, this.getHours() * this.signum);
      var1.add(12, this.getMinutes() * this.signum);
      var1.add(13, this.getSeconds() * this.signum);
      if (this.seconds != null) {
         BigDecimal var2 = this.seconds.subtract(this.seconds.setScale(0, 1));
         int var3 = var2.movePointRight(3).intValue();
         var1.add(14, var3 * this.signum);
      }

   }

   public void addTo(Date var1) {
      GregorianCalendar var2 = new GregorianCalendar();
      var2.setTime(var1);
      this.addTo((Calendar)var2);
      var1.setTime(getCalendarTimeInMillis(var2));
   }

   private Object writeReplace() throws IOException {
      return new DurationStream(this.toString());
   }

   private static long getCalendarTimeInMillis(Calendar var0) {
      return var0.getTime().getTime();
   }

   static {
      FIELDS = new DatatypeConstants.Field[]{DatatypeConstants.YEARS, DatatypeConstants.MONTHS, DatatypeConstants.DAYS, DatatypeConstants.HOURS, DatatypeConstants.MINUTES, DatatypeConstants.SECONDS};
      FIELD_IDS = new int[]{DatatypeConstants.YEARS.getId(), DatatypeConstants.MONTHS.getId(), DatatypeConstants.DAYS.getId(), DatatypeConstants.HOURS.getId(), DatatypeConstants.MINUTES.getId(), DatatypeConstants.SECONDS.getId()};
      ZERO = BigDecimal.valueOf(0L);
      TEST_POINTS = new XMLGregorianCalendar[]{XMLGregorianCalendarImpl.parse("1696-09-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1697-02-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1903-03-01T00:00:00Z"), XMLGregorianCalendarImpl.parse("1903-07-01T00:00:00Z")};
      FACTORS = new BigDecimal[]{BigDecimal.valueOf(12L), null, BigDecimal.valueOf(24L), BigDecimal.valueOf(60L), BigDecimal.valueOf(60L)};
   }

   private static class DurationStream implements Serializable {
      private final String lexical;
      private static final long serialVersionUID = 1L;

      private DurationStream(String var1) {
         this.lexical = var1;
      }

      private Object readResolve() throws ObjectStreamException {
         return new DurationImpl(this.lexical);
      }

      // $FF: synthetic method
      DurationStream(String var1, Object var2) {
         this(var1);
      }
   }
}
