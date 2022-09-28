package org.apache.xerces.jaxp.datatype;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import org.apache.xerces.util.DatatypeMessageFormatter;

class XMLGregorianCalendarImpl extends XMLGregorianCalendar implements Serializable, Cloneable {
   private BigInteger orig_eon;
   private int orig_year = Integer.MIN_VALUE;
   private int orig_month = Integer.MIN_VALUE;
   private int orig_day = Integer.MIN_VALUE;
   private int orig_hour = Integer.MIN_VALUE;
   private int orig_minute = Integer.MIN_VALUE;
   private int orig_second = Integer.MIN_VALUE;
   private BigDecimal orig_fracSeconds;
   private int orig_timezone = Integer.MIN_VALUE;
   private BigInteger eon = null;
   private int year = Integer.MIN_VALUE;
   private int month = Integer.MIN_VALUE;
   private int day = Integer.MIN_VALUE;
   private int timezone = Integer.MIN_VALUE;
   private int hour = Integer.MIN_VALUE;
   private int minute = Integer.MIN_VALUE;
   private int second = Integer.MIN_VALUE;
   private BigDecimal fractionalSecond = null;
   private static final BigInteger BILLION = new BigInteger("1000000000");
   private static final Date PURE_GREGORIAN_CHANGE = new Date(Long.MIN_VALUE);
   private static final int YEAR = 0;
   private static final int MONTH = 1;
   private static final int DAY = 2;
   private static final int HOUR = 3;
   private static final int MINUTE = 4;
   private static final int SECOND = 5;
   private static final int MILLISECOND = 6;
   private static final int TIMEZONE = 7;
   private static final int[] MIN_FIELD_VALUE = new int[]{Integer.MIN_VALUE, 1, 1, 0, 0, 0, 0, -840};
   private static final int[] MAX_FIELD_VALUE = new int[]{Integer.MAX_VALUE, 12, 31, 23, 59, 60, 999, 840};
   private static final String[] FIELD_NAME = new String[]{"Year", "Month", "Day", "Hour", "Minute", "Second", "Millisecond", "Timezone"};
   private static final long serialVersionUID = 1L;
   public static final XMLGregorianCalendar LEAP_YEAR_DEFAULT = createDateTime(400, 1, 1, 0, 0, 0, Integer.MIN_VALUE, Integer.MIN_VALUE);
   private static final BigInteger FOUR = BigInteger.valueOf(4L);
   private static final BigInteger HUNDRED = BigInteger.valueOf(100L);
   private static final BigInteger FOUR_HUNDRED = BigInteger.valueOf(400L);
   private static final BigInteger SIXTY = BigInteger.valueOf(60L);
   private static final BigInteger TWENTY_FOUR = BigInteger.valueOf(24L);
   private static final BigInteger TWELVE = BigInteger.valueOf(12L);
   private static final BigDecimal DECIMAL_ZERO = new BigDecimal("0");
   private static final BigDecimal DECIMAL_ONE = new BigDecimal("1");
   private static final BigDecimal DECIMAL_SIXTY = new BigDecimal("60");
   private static int[] daysInMonth = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

   protected XMLGregorianCalendarImpl(String var1) throws IllegalArgumentException {
      String var2 = null;
      String var3 = var1;
      int var5 = var1.length();
      Parser var6;
      if (var1.indexOf(84) != -1) {
         var2 = "%Y-%M-%DT%h:%m:%s%z";
      } else if (var5 >= 3 && var1.charAt(2) == ':') {
         var2 = "%h:%m:%s%z";
      } else if (var1.startsWith("--")) {
         if (var5 >= 3 && var1.charAt(2) == '-') {
            var2 = "---%D%z";
         } else if (var5 == 4 || var5 >= 6 && (var1.charAt(4) == '+' || var1.charAt(4) == '-' && (var1.charAt(5) == '-' || var5 == 10))) {
            var2 = "--%M--%z";
            var6 = new Parser(var2, var1);

            try {
               var6.parse();
               if (!this.isValid()) {
                  throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "InvalidXGCRepresentation", new Object[]{var1}));
               }

               this.save();
               return;
            } catch (IllegalArgumentException var9) {
               var2 = "--%M%z";
            }
         } else {
            var2 = "--%M-%D%z";
         }
      } else {
         int var10 = 0;
         int var7 = var1.indexOf(58);
         if (var7 != -1) {
            var5 -= 6;
         }

         for(int var8 = 1; var8 < var5; ++var8) {
            if (var3.charAt(var8) == '-') {
               ++var10;
            }
         }

         if (var10 == 0) {
            var2 = "%Y%z";
         } else if (var10 == 1) {
            var2 = "%Y-%M%z";
         } else {
            var2 = "%Y-%M-%D%z";
         }
      }

      var6 = new Parser(var2, var3);
      var6.parse();
      if (!this.isValid()) {
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "InvalidXGCRepresentation", new Object[]{var1}));
      } else {
         this.save();
      }
   }

   private void save() {
      this.orig_eon = this.eon;
      this.orig_year = this.year;
      this.orig_month = this.month;
      this.orig_day = this.day;
      this.orig_hour = this.hour;
      this.orig_minute = this.minute;
      this.orig_second = this.second;
      this.orig_fracSeconds = this.fractionalSecond;
      this.orig_timezone = this.timezone;
   }

   public XMLGregorianCalendarImpl() {
   }

   protected XMLGregorianCalendarImpl(BigInteger var1, int var2, int var3, int var4, int var5, int var6, BigDecimal var7, int var8) {
      this.setYear(var1);
      this.setMonth(var2);
      this.setDay(var3);
      this.setTime(var4, var5, var6, var7);
      this.setTimezone(var8);
      if (!this.isValid()) {
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "InvalidXGCValue-fractional", new Object[]{var1, new Integer(var2), new Integer(var3), new Integer(var4), new Integer(var5), new Integer(var6), var7, new Integer(var8)}));
      } else {
         this.save();
      }
   }

   private XMLGregorianCalendarImpl(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.setYear(var1);
      this.setMonth(var2);
      this.setDay(var3);
      this.setTime(var4, var5, var6);
      this.setTimezone(var8);
      this.setMillisecond(var7);
      if (!this.isValid()) {
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "InvalidXGCValue-milli", new Object[]{new Integer(var1), new Integer(var2), new Integer(var3), new Integer(var4), new Integer(var5), new Integer(var6), new Integer(var7), new Integer(var8)}));
      } else {
         this.save();
      }
   }

   public XMLGregorianCalendarImpl(GregorianCalendar var1) {
      int var2 = var1.get(1);
      if (var1.get(0) == 0) {
         var2 = -var2;
      }

      this.setYear(var2);
      this.setMonth(var1.get(2) + 1);
      this.setDay(var1.get(5));
      this.setTime(var1.get(11), var1.get(12), var1.get(13), var1.get(14));
      int var3 = (var1.get(15) + var1.get(16)) / '\uea60';
      this.setTimezone(var3);
      this.save();
   }

   public static XMLGregorianCalendar createDateTime(BigInteger var0, int var1, int var2, int var3, int var4, int var5, BigDecimal var6, int var7) {
      return new XMLGregorianCalendarImpl(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static XMLGregorianCalendar createDateTime(int var0, int var1, int var2, int var3, int var4, int var5) {
      return new XMLGregorianCalendarImpl(var0, var1, var2, var3, var4, var5, Integer.MIN_VALUE, Integer.MIN_VALUE);
   }

   public static XMLGregorianCalendar createDateTime(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      return new XMLGregorianCalendarImpl(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   public static XMLGregorianCalendar createDate(int var0, int var1, int var2, int var3) {
      return new XMLGregorianCalendarImpl(var0, var1, var2, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, var3);
   }

   public static XMLGregorianCalendar createTime(int var0, int var1, int var2, int var3) {
      return new XMLGregorianCalendarImpl(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, var0, var1, var2, Integer.MIN_VALUE, var3);
   }

   public static XMLGregorianCalendar createTime(int var0, int var1, int var2, BigDecimal var3, int var4) {
      return new XMLGregorianCalendarImpl((BigInteger)null, Integer.MIN_VALUE, Integer.MIN_VALUE, var0, var1, var2, var3, var4);
   }

   public static XMLGregorianCalendar createTime(int var0, int var1, int var2, int var3, int var4) {
      return new XMLGregorianCalendarImpl(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, var0, var1, var2, var3, var4);
   }

   public BigInteger getEon() {
      return this.eon;
   }

   public int getYear() {
      return this.year;
   }

   public BigInteger getEonAndYear() {
      if (this.year != Integer.MIN_VALUE && this.eon != null) {
         return this.eon.add(BigInteger.valueOf((long)this.year));
      } else {
         return this.year != Integer.MIN_VALUE && this.eon == null ? BigInteger.valueOf((long)this.year) : null;
      }
   }

   public int getMonth() {
      return this.month;
   }

   public int getDay() {
      return this.day;
   }

   public int getTimezone() {
      return this.timezone;
   }

   public int getHour() {
      return this.hour;
   }

   public int getMinute() {
      return this.minute;
   }

   public int getSecond() {
      return this.second;
   }

   private BigDecimal getSeconds() {
      if (this.second == Integer.MIN_VALUE) {
         return DECIMAL_ZERO;
      } else {
         BigDecimal var1 = BigDecimal.valueOf((long)this.second);
         return this.fractionalSecond != null ? var1.add(this.fractionalSecond) : var1;
      }
   }

   public int getMillisecond() {
      return this.fractionalSecond == null ? Integer.MIN_VALUE : this.fractionalSecond.movePointRight(3).intValue();
   }

   public BigDecimal getFractionalSecond() {
      return this.fractionalSecond;
   }

   public void setYear(BigInteger var1) {
      if (var1 == null) {
         this.eon = null;
         this.year = Integer.MIN_VALUE;
      } else {
         BigInteger var2 = var1.remainder(BILLION);
         this.year = var2.intValue();
         this.setEon(var1.subtract(var2));
      }

   }

   public void setYear(int var1) {
      if (var1 == Integer.MIN_VALUE) {
         this.year = Integer.MIN_VALUE;
         this.eon = null;
      } else if (Math.abs(var1) < BILLION.intValue()) {
         this.year = var1;
         this.eon = null;
      } else {
         BigInteger var2 = BigInteger.valueOf((long)var1);
         BigInteger var3 = var2.remainder(BILLION);
         this.year = var3.intValue();
         this.setEon(var2.subtract(var3));
      }

   }

   private void setEon(BigInteger var1) {
      if (var1 != null && var1.compareTo(BigInteger.ZERO) == 0) {
         this.eon = null;
      } else {
         this.eon = var1;
      }

   }

   public void setMonth(int var1) {
      this.checkFieldValueConstraint(1, var1);
      this.month = var1;
   }

   public void setDay(int var1) {
      this.checkFieldValueConstraint(2, var1);
      this.day = var1;
   }

   public void setTimezone(int var1) {
      this.checkFieldValueConstraint(7, var1);
      this.timezone = var1;
   }

   public void setTime(int var1, int var2, int var3) {
      this.setTime(var1, var2, var3, (BigDecimal)null);
   }

   private void checkFieldValueConstraint(int var1, int var2) throws IllegalArgumentException {
      if (var2 < MIN_FIELD_VALUE[var1] && var2 != Integer.MIN_VALUE || var2 > MAX_FIELD_VALUE[var1]) {
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "InvalidFieldValue", new Object[]{new Integer(var2), FIELD_NAME[var1]}));
      }
   }

   public void setHour(int var1) {
      this.checkFieldValueConstraint(3, var1);
      this.hour = var1;
   }

   public void setMinute(int var1) {
      this.checkFieldValueConstraint(4, var1);
      this.minute = var1;
   }

   public void setSecond(int var1) {
      this.checkFieldValueConstraint(5, var1);
      this.second = var1;
   }

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

   public int compare(XMLGregorianCalendar var1) {
      boolean var2 = true;
      XMLGregorianCalendarImpl var3 = this;
      Object var4 = var1;
      if (this.getTimezone() == var1.getTimezone()) {
         return internalCompare(this, var1);
      } else if (this.getTimezone() != Integer.MIN_VALUE && var1.getTimezone() != Integer.MIN_VALUE) {
         var3 = (XMLGregorianCalendarImpl)this.normalize();
         XMLGregorianCalendarImpl var8 = (XMLGregorianCalendarImpl)var1.normalize();
         return internalCompare(var3, var8);
      } else {
         XMLGregorianCalendar var5;
         XMLGregorianCalendar var6;
         int var7;
         if (this.getTimezone() != Integer.MIN_VALUE) {
            if (this.getTimezone() != 0) {
               var3 = (XMLGregorianCalendarImpl)this.normalize();
            }

            var5 = this.normalizeToTimezone(var1, 840);
            var7 = internalCompare(var3, var5);
            if (var7 == -1) {
               return var7;
            } else {
               var6 = this.normalizeToTimezone(var1, -840);
               var7 = internalCompare(var3, var6);
               return var7 == 1 ? var7 : 2;
            }
         } else {
            if (var1.getTimezone() != 0) {
               var4 = (XMLGregorianCalendarImpl)this.normalizeToTimezone(var1, var1.getTimezone());
            }

            var5 = this.normalizeToTimezone(this, -840);
            var7 = internalCompare(var5, (XMLGregorianCalendar)var4);
            if (var7 == -1) {
               return var7;
            } else {
               var6 = this.normalizeToTimezone(this, 840);
               var7 = internalCompare(var6, (XMLGregorianCalendar)var4);
               return var7 == 1 ? var7 : 2;
            }
         }
      }
   }

   public XMLGregorianCalendar normalize() {
      XMLGregorianCalendar var1 = this.normalizeToTimezone(this, this.timezone);
      if (this.getTimezone() == Integer.MIN_VALUE) {
         var1.setTimezone(Integer.MIN_VALUE);
      }

      if (this.getMillisecond() == Integer.MIN_VALUE) {
         var1.setMillisecond(Integer.MIN_VALUE);
      }

      return var1;
   }

   private XMLGregorianCalendar normalizeToTimezone(XMLGregorianCalendar var1, int var2) {
      XMLGregorianCalendar var4 = (XMLGregorianCalendar)var1.clone();
      int var3 = -var2;
      DurationImpl var5 = new DurationImpl(var3 >= 0, 0, 0, 0, 0, var3 < 0 ? -var3 : var3, 0);
      var4.add(var5);
      var4.setTimezone(0);
      return var4;
   }

   private static int internalCompare(XMLGregorianCalendar var0, XMLGregorianCalendar var1) {
      int var2;
      if (var0.getEon() == var1.getEon()) {
         var2 = compareField(var0.getYear(), var1.getYear());
         if (var2 != 0) {
            return var2;
         }
      } else {
         var2 = compareField(var0.getEonAndYear(), var1.getEonAndYear());
         if (var2 != 0) {
            return var2;
         }
      }

      var2 = compareField(var0.getMonth(), var1.getMonth());
      if (var2 != 0) {
         return var2;
      } else {
         var2 = compareField(var0.getDay(), var1.getDay());
         if (var2 != 0) {
            return var2;
         } else {
            var2 = compareField(var0.getHour(), var1.getHour());
            if (var2 != 0) {
               return var2;
            } else {
               var2 = compareField(var0.getMinute(), var1.getMinute());
               if (var2 != 0) {
                  return var2;
               } else {
                  var2 = compareField(var0.getSecond(), var1.getSecond());
                  if (var2 != 0) {
                     return var2;
                  } else {
                     var2 = compareField(var0.getFractionalSecond(), var1.getFractionalSecond());
                     return var2;
                  }
               }
            }
         }
      }
   }

   private static int compareField(int var0, int var1) {
      if (var0 == var1) {
         return 0;
      } else if (var0 != Integer.MIN_VALUE && var1 != Integer.MIN_VALUE) {
         return var0 < var1 ? -1 : 1;
      } else {
         return 2;
      }
   }

   private static int compareField(BigInteger var0, BigInteger var1) {
      if (var0 == null) {
         return var1 == null ? 0 : 2;
      } else {
         return var1 == null ? 2 : var0.compareTo(var1);
      }
   }

   private static int compareField(BigDecimal var0, BigDecimal var1) {
      if (var0 == var1) {
         return 0;
      } else {
         if (var0 == null) {
            var0 = DECIMAL_ZERO;
         }

         if (var1 == null) {
            var1 = DECIMAL_ZERO;
         }

         return var0.compareTo(var1);
      }
   }

   public boolean equals(Object var1) {
      boolean var2 = false;
      if (var1 instanceof XMLGregorianCalendar) {
         var2 = this.compare((XMLGregorianCalendar)var1) == 0;
      }

      return var2;
   }

   public int hashCode() {
      int var1 = this.getTimezone();
      if (var1 == Integer.MIN_VALUE) {
         var1 = 0;
      }

      Object var2 = this;
      if (var1 != 0) {
         var2 = this.normalizeToTimezone(this, this.getTimezone());
      }

      return ((XMLGregorianCalendar)var2).getYear() + ((XMLGregorianCalendar)var2).getMonth() + ((XMLGregorianCalendar)var2).getDay() + ((XMLGregorianCalendar)var2).getHour() + ((XMLGregorianCalendar)var2).getMinute() + ((XMLGregorianCalendar)var2).getSecond();
   }

   public static XMLGregorianCalendar parse(String var0) {
      return new XMLGregorianCalendarImpl(var0);
   }

   public String toXMLFormat() {
      QName var1 = this.getXMLSchemaType();
      String var2 = null;
      if (var1 == DatatypeConstants.DATETIME) {
         var2 = "%Y-%M-%DT%h:%m:%s%z";
      } else if (var1 == DatatypeConstants.DATE) {
         var2 = "%Y-%M-%D%z";
      } else if (var1 == DatatypeConstants.TIME) {
         var2 = "%h:%m:%s%z";
      } else if (var1 == DatatypeConstants.GMONTH) {
         var2 = "--%M--%z";
      } else if (var1 == DatatypeConstants.GDAY) {
         var2 = "---%D%z";
      } else if (var1 == DatatypeConstants.GYEAR) {
         var2 = "%Y%z";
      } else if (var1 == DatatypeConstants.GYEARMONTH) {
         var2 = "%Y-%M%z";
      } else if (var1 == DatatypeConstants.GMONTHDAY) {
         var2 = "--%M-%D%z";
      }

      return this.format(var2);
   }

   public QName getXMLSchemaType() {
      if (this.year != Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && this.hour != Integer.MIN_VALUE && this.minute != Integer.MIN_VALUE && this.second != Integer.MIN_VALUE) {
         return DatatypeConstants.DATETIME;
      } else if (this.year != Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
         return DatatypeConstants.DATE;
      } else if (this.year == Integer.MIN_VALUE && this.month == Integer.MIN_VALUE && this.day == Integer.MIN_VALUE && this.hour != Integer.MIN_VALUE && this.minute != Integer.MIN_VALUE && this.second != Integer.MIN_VALUE) {
         return DatatypeConstants.TIME;
      } else if (this.year != Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day == Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
         return DatatypeConstants.GYEARMONTH;
      } else if (this.year == Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
         return DatatypeConstants.GMONTHDAY;
      } else if (this.year != Integer.MIN_VALUE && this.month == Integer.MIN_VALUE && this.day == Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
         return DatatypeConstants.GYEAR;
      } else if (this.year == Integer.MIN_VALUE && this.month != Integer.MIN_VALUE && this.day == Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
         return DatatypeConstants.GMONTH;
      } else if (this.year == Integer.MIN_VALUE && this.month == Integer.MIN_VALUE && this.day != Integer.MIN_VALUE && this.hour == Integer.MIN_VALUE && this.minute == Integer.MIN_VALUE && this.second == Integer.MIN_VALUE) {
         return DatatypeConstants.GDAY;
      } else {
         throw new IllegalStateException(this.getClass().getName() + "#getXMLSchemaType() :" + DatatypeMessageFormatter.formatMessage((Locale)null, "InvalidXGCFields", (Object[])null));
      }
   }

   public boolean isValid() {
      if (this.getMonth() == 2) {
         int var1 = Integer.MIN_VALUE;
         BigInteger var2 = this.getEonAndYear();
         if (var2 != null) {
            var1 = maximumDayInMonthFor(this.getEonAndYear(), 2);
         } else {
            var1 = 29;
         }

         if (this.getDay() > var1) {
            return false;
         }
      }

      if (this.getHour() == 24) {
         if (this.getMinute() != 0) {
            return false;
         }

         if (this.getSecond() != 0) {
            return false;
         }
      }

      if (this.eon == null) {
         if (this.year == 0) {
            return false;
         }
      } else {
         BigInteger var3 = this.getEonAndYear();
         if (var3 != null) {
            int var4 = compareField(var3, BigInteger.ZERO);
            if (var4 == 0) {
               return false;
            }
         }
      }

      return true;
   }

   public void add(Duration var1) {
      boolean[] var2 = new boolean[]{false, false, false, false, false, false};
      int var3 = var1.getSign();
      int var4 = this.getMonth();
      if (var4 == Integer.MIN_VALUE) {
         var4 = MIN_FIELD_VALUE[1];
         var2[1] = true;
      }

      BigInteger var5 = sanitize(var1.getField(DatatypeConstants.MONTHS), var3);
      BigInteger var6 = BigInteger.valueOf((long)var4).add(var5);
      this.setMonth(var6.subtract(BigInteger.ONE).mod(TWELVE).intValue() + 1);
      BigInteger var7 = (new BigDecimal(var6.subtract(BigInteger.ONE))).divide(new BigDecimal(TWELVE), 3).toBigInteger();
      BigInteger var8 = this.getEonAndYear();
      if (var8 == null) {
         var2[0] = true;
         var8 = BigInteger.ZERO;
      }

      BigInteger var9 = sanitize(var1.getField(DatatypeConstants.YEARS), var3);
      BigInteger var10 = var8.add(var9).add(var7);
      this.setYear(var10);
      BigDecimal var11;
      if (this.getSecond() == Integer.MIN_VALUE) {
         var2[5] = true;
         var11 = DECIMAL_ZERO;
      } else {
         var11 = this.getSeconds();
      }

      BigDecimal var12 = DurationImpl.sanitize((BigDecimal)var1.getField(DatatypeConstants.SECONDS), var3);
      BigDecimal var13 = var11.add(var12);
      BigDecimal var14 = new BigDecimal((new BigDecimal(var13.toBigInteger())).divide(DECIMAL_SIXTY, 3).toBigInteger());
      BigDecimal var15 = var13.subtract(var14.multiply(DECIMAL_SIXTY));
      var7 = var14.toBigInteger();
      this.setSecond(var15.intValue());
      BigDecimal var16 = var15.subtract(new BigDecimal(BigInteger.valueOf((long)this.getSecond())));
      if (var16.compareTo(DECIMAL_ZERO) < 0) {
         this.setFractionalSecond(DECIMAL_ONE.add(var16));
         if (this.getSecond() == 0) {
            this.setSecond(59);
            var7 = var7.subtract(BigInteger.ONE);
         } else {
            this.setSecond(this.getSecond() - 1);
         }
      } else {
         this.setFractionalSecond(var16);
      }

      int var17 = this.getMinute();
      if (var17 == Integer.MIN_VALUE) {
         var2[4] = true;
         var17 = MIN_FIELD_VALUE[4];
      }

      BigInteger var18 = sanitize(var1.getField(DatatypeConstants.MINUTES), var3);
      var6 = BigInteger.valueOf((long)var17).add(var18).add(var7);
      this.setMinute(var6.mod(SIXTY).intValue());
      var7 = (new BigDecimal(var6)).divide(DECIMAL_SIXTY, 3).toBigInteger();
      int var19 = this.getHour();
      if (var19 == Integer.MIN_VALUE) {
         var2[3] = true;
         var19 = MIN_FIELD_VALUE[3];
      }

      BigInteger var20 = sanitize(var1.getField(DatatypeConstants.HOURS), var3);
      var6 = BigInteger.valueOf((long)var19).add(var20).add(var7);
      this.setHour(var6.mod(TWENTY_FOUR).intValue());
      var7 = (new BigDecimal(var6)).divide(new BigDecimal(TWENTY_FOUR), 3).toBigInteger();
      int var22 = this.getDay();
      if (var22 == Integer.MIN_VALUE) {
         var2[2] = true;
         var22 = MIN_FIELD_VALUE[2];
      }

      BigInteger var23 = sanitize(var1.getField(DatatypeConstants.DAYS), var3);
      int var24 = maximumDayInMonthFor(this.getEonAndYear(), this.getMonth());
      BigInteger var21;
      if (var22 > var24) {
         var21 = BigInteger.valueOf((long)var24);
      } else if (var22 < 1) {
         var21 = BigInteger.ONE;
      } else {
         var21 = BigInteger.valueOf((long)var22);
      }

      BigInteger var25 = var21.add(var23).add(var7);

      while(true) {
         byte var26;
         int var30;
         if (var25.compareTo(BigInteger.ONE) < 0) {
            BigInteger var28 = null;
            if (this.month >= 2) {
               var28 = BigInteger.valueOf((long)maximumDayInMonthFor(this.getEonAndYear(), this.getMonth() - 1));
            } else {
               var28 = BigInteger.valueOf((long)maximumDayInMonthFor(this.getEonAndYear().subtract(BigInteger.valueOf(1L)), 12));
            }

            var25 = var25.add(var28);
            var26 = -1;
         } else {
            if (var25.compareTo(BigInteger.valueOf((long)maximumDayInMonthFor(this.getEonAndYear(), this.getMonth()))) <= 0) {
               this.setDay(var25.intValue());

               for(var30 = 0; var30 <= 5; ++var30) {
                  if (var2[var30]) {
                     switch (var30) {
                        case 0:
                           this.setYear(Integer.MIN_VALUE);
                           break;
                        case 1:
                           this.setMonth(Integer.MIN_VALUE);
                           break;
                        case 2:
                           this.setDay(Integer.MIN_VALUE);
                           break;
                        case 3:
                           this.setHour(Integer.MIN_VALUE);
                           break;
                        case 4:
                           this.setMinute(Integer.MIN_VALUE);
                           break;
                        case 5:
                           this.setSecond(Integer.MIN_VALUE);
                           this.setFractionalSecond((BigDecimal)null);
                     }
                  }
               }

               return;
            }

            var25 = var25.add(BigInteger.valueOf((long)(-maximumDayInMonthFor(this.getEonAndYear(), this.getMonth()))));
            var26 = 1;
         }

         int var27 = this.getMonth() + var26;
         var30 = (var27 - 1) % 12;
         int var29;
         if (var30 < 0) {
            var30 = 12 + var30 + 1;
            var29 = (new BigDecimal((double)(var27 - 1))).divide(new BigDecimal(TWELVE), 0).intValue();
         } else {
            var29 = (var27 - 1) / 12;
            ++var30;
         }

         this.setMonth(var30);
         if (var29 != 0) {
            this.setYear(this.getEonAndYear().add(BigInteger.valueOf((long)var29)));
         }
      }
   }

   private static int maximumDayInMonthFor(BigInteger var0, int var1) {
      if (var1 != 2) {
         return daysInMonth[var1];
      } else {
         return !var0.mod(FOUR_HUNDRED).equals(BigInteger.ZERO) && (var0.mod(HUNDRED).equals(BigInteger.ZERO) || !var0.mod(FOUR).equals(BigInteger.ZERO)) ? daysInMonth[var1] : 29;
      }
   }

   private static int maximumDayInMonthFor(int var0, int var1) {
      if (var1 != 2) {
         return daysInMonth[var1];
      } else {
         return var0 % 400 != 0 && (var0 % 100 == 0 || var0 % 4 != 0) ? daysInMonth[2] : 29;
      }
   }

   public GregorianCalendar toGregorianCalendar() {
      GregorianCalendar var1 = null;
      TimeZone var3 = this.getTimeZone(Integer.MIN_VALUE);
      Locale var4 = Locale.getDefault();
      var1 = new GregorianCalendar(var3, var4);
      var1.clear();
      var1.setGregorianChange(PURE_GREGORIAN_CHANGE);
      BigInteger var5 = this.getEonAndYear();
      if (var5 != null) {
         var1.set(0, var5.signum() == -1 ? 0 : 1);
         var1.set(1, var5.abs().intValue());
      }

      if (this.month != Integer.MIN_VALUE) {
         var1.set(2, this.month - 1);
      }

      if (this.day != Integer.MIN_VALUE) {
         var1.set(5, this.day);
      }

      if (this.hour != Integer.MIN_VALUE) {
         var1.set(11, this.hour);
      }

      if (this.minute != Integer.MIN_VALUE) {
         var1.set(12, this.minute);
      }

      if (this.second != Integer.MIN_VALUE) {
         var1.set(13, this.second);
      }

      if (this.fractionalSecond != null) {
         var1.set(14, this.getMillisecond());
      }

      return var1;
   }

   public GregorianCalendar toGregorianCalendar(TimeZone var1, Locale var2, XMLGregorianCalendar var3) {
      GregorianCalendar var4 = null;
      TimeZone var5 = var1;
      if (var1 == null) {
         int var6 = Integer.MIN_VALUE;
         if (var3 != null) {
            var6 = var3.getTimezone();
         }

         var5 = this.getTimeZone(var6);
      }

      if (var2 == null) {
         var2 = Locale.getDefault();
      }

      var4 = new GregorianCalendar(var5, var2);
      var4.clear();
      var4.setGregorianChange(PURE_GREGORIAN_CHANGE);
      BigInteger var8 = this.getEonAndYear();
      if (var8 != null) {
         var4.set(0, var8.signum() == -1 ? 0 : 1);
         var4.set(1, var8.abs().intValue());
      } else {
         BigInteger var7 = var3 != null ? var3.getEonAndYear() : null;
         if (var7 != null) {
            var4.set(0, var7.signum() == -1 ? 0 : 1);
            var4.set(1, var7.abs().intValue());
         }
      }

      int var9;
      if (this.month != Integer.MIN_VALUE) {
         var4.set(2, this.month - 1);
      } else {
         var9 = var3 != null ? var3.getMonth() : Integer.MIN_VALUE;
         if (var9 != Integer.MIN_VALUE) {
            var4.set(2, var9 - 1);
         }
      }

      if (this.day != Integer.MIN_VALUE) {
         var4.set(5, this.day);
      } else {
         var9 = var3 != null ? var3.getDay() : Integer.MIN_VALUE;
         if (var9 != Integer.MIN_VALUE) {
            var4.set(5, var9);
         }
      }

      if (this.hour != Integer.MIN_VALUE) {
         var4.set(11, this.hour);
      } else {
         var9 = var3 != null ? var3.getHour() : Integer.MIN_VALUE;
         if (var9 != Integer.MIN_VALUE) {
            var4.set(11, var9);
         }
      }

      if (this.minute != Integer.MIN_VALUE) {
         var4.set(12, this.minute);
      } else {
         var9 = var3 != null ? var3.getMinute() : Integer.MIN_VALUE;
         if (var9 != Integer.MIN_VALUE) {
            var4.set(12, var9);
         }
      }

      if (this.second != Integer.MIN_VALUE) {
         var4.set(13, this.second);
      } else {
         var9 = var3 != null ? var3.getSecond() : Integer.MIN_VALUE;
         if (var9 != Integer.MIN_VALUE) {
            var4.set(13, var9);
         }
      }

      if (this.fractionalSecond != null) {
         var4.set(14, this.getMillisecond());
      } else {
         BigDecimal var10 = var3 != null ? var3.getFractionalSecond() : null;
         if (var10 != null) {
            var4.set(14, var3.getMillisecond());
         }
      }

      return var4;
   }

   public TimeZone getTimeZone(int var1) {
      TimeZone var2 = null;
      int var3 = this.getTimezone();
      if (var3 == Integer.MIN_VALUE) {
         var3 = var1;
      }

      if (var3 == Integer.MIN_VALUE) {
         var2 = TimeZone.getDefault();
      } else {
         int var4 = var3 < 0 ? 45 : 43;
         if (var4 == 45) {
            var3 = -var3;
         }

         int var5 = var3 / 60;
         int var6 = var3 - var5 * 60;
         StringBuffer var7 = new StringBuffer(8);
         var7.append("GMT");
         var7.append((char)var4);
         var7.append(var5);
         if (var6 != 0) {
            var7.append(var6);
         }

         var2 = TimeZone.getTimeZone(var7.toString());
      }

      return var2;
   }

   public Object clone() {
      return new XMLGregorianCalendarImpl(this.getEonAndYear(), this.month, this.day, this.hour, this.minute, this.second, this.fractionalSecond, this.timezone);
   }

   public void clear() {
      this.eon = null;
      this.year = Integer.MIN_VALUE;
      this.month = Integer.MIN_VALUE;
      this.day = Integer.MIN_VALUE;
      this.timezone = Integer.MIN_VALUE;
      this.hour = Integer.MIN_VALUE;
      this.minute = Integer.MIN_VALUE;
      this.second = Integer.MIN_VALUE;
      this.fractionalSecond = null;
   }

   public void setMillisecond(int var1) {
      if (var1 == Integer.MIN_VALUE) {
         this.fractionalSecond = null;
      } else {
         this.checkFieldValueConstraint(6, var1);
         this.fractionalSecond = (new BigDecimal((double)((long)var1))).movePointLeft(3);
      }

   }

   public void setFractionalSecond(BigDecimal var1) {
      if (var1 == null || var1.compareTo(DECIMAL_ZERO) >= 0 && var1.compareTo(DECIMAL_ONE) <= 0) {
         this.fractionalSecond = var1;
      } else {
         throw new IllegalArgumentException(DatatypeMessageFormatter.formatMessage((Locale)null, "InvalidFractional", new Object[]{var1}));
      }
   }

   private static boolean isDigit(char var0) {
      return '0' <= var0 && var0 <= '9';
   }

   private String format(String var1) {
      StringBuffer var2 = new StringBuffer();
      int var3 = 0;
      int var4 = var1.length();

      while(var3 < var4) {
         char var5 = var1.charAt(var3++);
         if (var5 != '%') {
            var2.append(var5);
         } else {
            switch (var1.charAt(var3++)) {
               case 'D':
                  this.printNumber(var2, this.getDay(), 2);
                  break;
               case 'M':
                  this.printNumber(var2, this.getMonth(), 2);
                  break;
               case 'Y':
                  this.printNumber(var2, this.getEonAndYear(), 4);
                  break;
               case 'h':
                  this.printNumber(var2, this.getHour(), 2);
                  break;
               case 'm':
                  this.printNumber(var2, this.getMinute(), 2);
                  break;
               case 's':
                  this.printNumber(var2, this.getSecond(), 2);
                  if (this.getFractionalSecond() != null) {
                     String var7 = this.getFractionalSecond().toString();
                     var2.append(var7.substring(1, var7.length()));
                  }
                  break;
               case 'z':
                  int var6 = this.getTimezone();
                  if (var6 == 0) {
                     var2.append('Z');
                  } else if (var6 != Integer.MIN_VALUE) {
                     if (var6 < 0) {
                        var2.append('-');
                        var6 *= -1;
                     } else {
                        var2.append('+');
                     }

                     this.printNumber(var2, var6 / 60, 2);
                     var2.append(':');
                     this.printNumber(var2, var6 % 60, 2);
                  }
                  break;
               default:
                  throw new InternalError();
            }
         }
      }

      return var2.toString();
   }

   private void printNumber(StringBuffer var1, int var2, int var3) {
      String var4 = String.valueOf(var2);

      for(int var5 = var4.length(); var5 < var3; ++var5) {
         var1.append('0');
      }

      var1.append(var4);
   }

   private void printNumber(StringBuffer var1, BigInteger var2, int var3) {
      String var4 = var2.toString();

      for(int var5 = var4.length(); var5 < var3; ++var5) {
         var1.append('0');
      }

      var1.append(var4);
   }

   static BigInteger sanitize(Number var0, int var1) {
      if (var1 != 0 && var0 != null) {
         return var1 < 0 ? ((BigInteger)var0).negate() : (BigInteger)var0;
      } else {
         return BigInteger.ZERO;
      }
   }

   public void reset() {
      this.eon = this.orig_eon;
      this.year = this.orig_year;
      this.month = this.orig_month;
      this.day = this.orig_day;
      this.hour = this.orig_hour;
      this.minute = this.orig_minute;
      this.second = this.orig_second;
      this.fractionalSecond = this.orig_fracSeconds;
      this.timezone = this.orig_timezone;
   }

   private final class Parser {
      private final String format;
      private final String value;
      private final int flen;
      private final int vlen;
      private int fidx;
      private int vidx;

      private Parser(String var2, String var3) {
         this.format = var2;
         this.value = var3;
         this.flen = var2.length();
         this.vlen = var3.length();
      }

      public void parse() throws IllegalArgumentException {
         while(this.fidx < this.flen) {
            char var1 = this.format.charAt(this.fidx++);
            if (var1 != '%') {
               this.skip(var1);
            } else {
               switch (this.format.charAt(this.fidx++)) {
                  case 'D':
                     XMLGregorianCalendarImpl.this.setDay(this.parseInt(2, 2));
                     break;
                  case 'M':
                     XMLGregorianCalendarImpl.this.setMonth(this.parseInt(2, 2));
                     break;
                  case 'Y':
                     XMLGregorianCalendarImpl.this.setYear(this.parseBigInteger(4));
                     break;
                  case 'h':
                     XMLGregorianCalendarImpl.this.setHour(this.parseInt(2, 2));
                     break;
                  case 'm':
                     XMLGregorianCalendarImpl.this.setMinute(this.parseInt(2, 2));
                     break;
                  case 's':
                     XMLGregorianCalendarImpl.this.setSecond(this.parseInt(2, 2));
                     if (this.peek() == '.') {
                        XMLGregorianCalendarImpl.this.setFractionalSecond(this.parseBigDecimal());
                     }
                     break;
                  case 'z':
                     char var2 = this.peek();
                     if (var2 == 'Z') {
                        ++this.vidx;
                        XMLGregorianCalendarImpl.this.setTimezone(0);
                     } else if (var2 == '+' || var2 == '-') {
                        ++this.vidx;
                        int var3 = this.parseInt(2, 2);
                        this.skip(':');
                        int var4 = this.parseInt(2, 2);
                        XMLGregorianCalendarImpl.this.setTimezone((var3 * 60 + var4) * (var2 == '+' ? 1 : -1));
                     }
                     break;
                  default:
                     throw new InternalError();
               }
            }
         }

         if (this.vidx != this.vlen) {
            throw new IllegalArgumentException(this.value);
         }
      }

      private char peek() throws IllegalArgumentException {
         return this.vidx == this.vlen ? '\uffff' : this.value.charAt(this.vidx);
      }

      private char read() throws IllegalArgumentException {
         if (this.vidx == this.vlen) {
            throw new IllegalArgumentException(this.value);
         } else {
            return this.value.charAt(this.vidx++);
         }
      }

      private void skip(char var1) throws IllegalArgumentException {
         if (this.read() != var1) {
            throw new IllegalArgumentException(this.value);
         }
      }

      private int parseInt(int var1, int var2) throws IllegalArgumentException {
         int var3;
         for(var3 = this.vidx; XMLGregorianCalendarImpl.isDigit(this.peek()) && this.vidx - var3 <= var2; ++this.vidx) {
         }

         if (this.vidx - var3 < var1) {
            throw new IllegalArgumentException(this.value);
         } else {
            return Integer.parseInt(this.value.substring(var3, this.vidx));
         }
      }

      private BigInteger parseBigInteger(int var1) throws IllegalArgumentException {
         int var2 = this.vidx;
         if (this.peek() == '-') {
            ++this.vidx;
         }

         while(XMLGregorianCalendarImpl.isDigit(this.peek())) {
            ++this.vidx;
         }

         if (this.vidx - var2 < var1) {
            throw new IllegalArgumentException(this.value);
         } else {
            return new BigInteger(this.value.substring(var2, this.vidx));
         }
      }

      private BigDecimal parseBigDecimal() throws IllegalArgumentException {
         int var1 = this.vidx;
         if (this.peek() != '.') {
            throw new IllegalArgumentException(this.value);
         } else {
            ++this.vidx;

            while(XMLGregorianCalendarImpl.isDigit(this.peek())) {
               ++this.vidx;
            }

            return new BigDecimal(this.value.substring(var1, this.vidx));
         }
      }

      // $FF: synthetic method
      Parser(String var2, String var3, Object var4) {
         this(var2, var3);
      }
   }
}
