package org.apache.xalan.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class ExsltDatetime {
   static final String dt = "yyyy-MM-dd'T'HH:mm:ss";
   static final String d = "yyyy-MM-dd";
   static final String gym = "yyyy-MM";
   static final String gy = "yyyy";
   static final String gmd = "--MM-dd";
   static final String gm = "--MM--";
   static final String gd = "---dd";
   static final String t = "HH:mm:ss";
   static final String EMPTY_STR = "";

   public static String dateTime() {
      Calendar cal = Calendar.getInstance();
      Date datetime = cal.getTime();
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      StringBuffer buff = new StringBuffer(dateFormat.format(datetime));
      int offset = cal.get(15) + cal.get(16);
      if (offset == 0) {
         buff.append("Z");
      } else {
         int hrs = offset / 3600000;
         int min = offset % 3600000;
         char posneg = hrs < 0 ? 45 : 43;
         buff.append(posneg + formatDigits(hrs) + ':' + formatDigits(min));
      }

      return buff.toString();
   }

   private static String formatDigits(int q) {
      String dd = String.valueOf(Math.abs(q));
      return dd.length() == 1 ? '0' + dd : dd;
   }

   public static String date(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String leader = edz[0];
      String datetime = edz[1];
      String zone = edz[2];
      if (datetime != null && zone != null) {
         String[] formatsIn = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
         String formatOut = "yyyy-MM-dd";
         Date date = testFormats(datetime, formatsIn);
         if (date == null) {
            return "";
         } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut);
            dateFormat.setLenient(false);
            String dateOut = dateFormat.format(date);
            return dateOut.length() == 0 ? "" : leader + dateOut + zone;
         }
      } else {
         return "";
      }
   }

   public static String date() {
      String datetime = dateTime().toString();
      String date = datetime.substring(0, datetime.indexOf("T"));
      String zone = datetime.substring(getZoneStart(datetime));
      return date + zone;
   }

   public static String time(String timeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(timeIn);
      String time = edz[1];
      String zone = edz[2];
      if (time != null && zone != null) {
         String[] formatsIn = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "HH:mm:ss"};
         String formatOut = "HH:mm:ss";
         Date date = testFormats(time, formatsIn);
         if (date == null) {
            return "";
         } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatOut);
            String out = dateFormat.format(date);
            return out + zone;
         }
      } else {
         return "";
      }
   }

   public static String time() {
      String datetime = dateTime().toString();
      String time = datetime.substring(datetime.indexOf("T") + 1);
      return time;
   }

   public static double year(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      boolean ad = edz[0].length() == 0;
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "yyyy"};
         double yr = getNumber(datetime, formats, 1);
         return !ad && yr != Double.NaN ? -yr : yr;
      }
   }

   public static double year() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(1);
   }

   public static double monthInYear(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--", "--MM-dd"};
         return getNumber(datetime, formats, 2) + 1.0;
      }
   }

   public static double monthInYear() {
      Calendar cal = Calendar.getInstance();
      return (double)(cal.get(2) + 1);
   }

   public static double weekInYear(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
         return getNumber(datetime, formats, 3);
      }
   }

   public static double weekInYear() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(3);
   }

   public static double dayInYear(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
         return getNumber(datetime, formats, 6);
      }
   }

   public static double dayInYear() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(6);
   }

   public static double dayInMonth(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "--MM-dd", "---dd"};
      double day = getNumber(datetime, formats, 5);
      return day;
   }

   public static double dayInMonth() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(5);
   }

   public static double dayOfWeekInMonth(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
         return getNumber(datetime, formats, 8);
      }
   }

   public static double dayOfWeekInMonth() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(8);
   }

   public static double dayInWeek(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
         return getNumber(datetime, formats, 7);
      }
   }

   public static double dayInWeek() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(7);
   }

   public static double hourInDay(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss"};
         return getNumber(datetime, formats, 11);
      }
   }

   public static double hourInDay() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(11);
   }

   public static double minuteInHour(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss"};
         return getNumber(datetime, formats, 12);
      }
   }

   public static double minuteInHour() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(12);
   }

   public static double secondInMinute(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return Double.NaN;
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss"};
         return getNumber(datetime, formats, 13);
      }
   }

   public static double secondInMinute() {
      Calendar cal = Calendar.getInstance();
      return (double)cal.get(13);
   }

   public static XObject leapYear(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return new XNumber(Double.NaN);
      } else {
         String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "yyyy"};
         double dbl = getNumber(datetime, formats, 1);
         if (dbl == Double.NaN) {
            return new XNumber(Double.NaN);
         } else {
            int yr = (int)dbl;
            return new XBoolean(yr % 400 == 0 || yr % 100 != 0 && yr % 4 == 0);
         }
      }
   }

   public static boolean leapYear() {
      Calendar cal = Calendar.getInstance();
      int yr = cal.get(1);
      return yr % 400 == 0 || yr % 100 != 0 && yr % 4 == 0;
   }

   public static String monthName(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return "";
      } else {
         String[] formatsIn = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--"};
         String formatOut = "MMMM";
         return getNameOrAbbrev(datetimeIn, formatsIn, formatOut);
      }
   }

   public static String monthName() {
      Calendar cal = Calendar.getInstance();
      String format = "MMMM";
      return getNameOrAbbrev(format);
   }

   public static String monthAbbreviation(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return "";
      } else {
         String[] formatsIn = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd", "yyyy-MM", "--MM--"};
         String formatOut = "MMM";
         return getNameOrAbbrev(datetimeIn, formatsIn, formatOut);
      }
   }

   public static String monthAbbreviation() {
      String format = "MMM";
      return getNameOrAbbrev(format);
   }

   public static String dayName(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return "";
      } else {
         String[] formatsIn = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
         String formatOut = "EEEE";
         return getNameOrAbbrev(datetimeIn, formatsIn, formatOut);
      }
   }

   public static String dayName() {
      String format = "EEEE";
      return getNameOrAbbrev(format);
   }

   public static String dayAbbreviation(String datetimeIn) throws ParseException {
      String[] edz = getEraDatetimeZone(datetimeIn);
      String datetime = edz[1];
      if (datetime == null) {
         return "";
      } else {
         String[] formatsIn = new String[]{"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};
         String formatOut = "EEE";
         return getNameOrAbbrev(datetimeIn, formatsIn, formatOut);
      }
   }

   public static String dayAbbreviation() {
      String format = "EEE";
      return getNameOrAbbrev(format);
   }

   private static String[] getEraDatetimeZone(String in) {
      String leader = "";
      String datetime = in;
      String zone = "";
      if (in.charAt(0) == '-' && !in.startsWith("--")) {
         leader = "-";
         datetime = in.substring(1);
      }

      int z = getZoneStart(datetime);
      if (z > 0) {
         zone = datetime.substring(z);
         datetime = datetime.substring(0, z);
      } else if (z == -2) {
         zone = null;
      }

      return new String[]{leader, datetime, zone};
   }

   private static int getZoneStart(String datetime) {
      if (datetime.indexOf("Z") == datetime.length() - 1) {
         return datetime.length() - 1;
      } else if (datetime.length() >= 6 && datetime.charAt(datetime.length() - 3) == ':' && (datetime.charAt(datetime.length() - 6) == '+' || datetime.charAt(datetime.length() - 6) == '-')) {
         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            dateFormat.setLenient(false);
            dateFormat.parse(datetime.substring(datetime.length() - 5));
            return datetime.length() - 6;
         } catch (ParseException var3) {
            System.out.println("ParseException " + var3.getErrorOffset());
            return -2;
         }
      } else {
         return -1;
      }
   }

   private static Date testFormats(String in, String[] formats) throws ParseException {
      int i = 0;

      while(i < formats.length) {
         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formats[i]);
            dateFormat.setLenient(false);
            return dateFormat.parse(in);
         } catch (ParseException var4) {
            ++i;
         }
      }

      return null;
   }

   private static double getNumber(String in, String[] formats, int calField) throws ParseException {
      Calendar cal = Calendar.getInstance();
      cal.setLenient(false);
      Date date = testFormats(in, formats);
      if (date == null) {
         return Double.NaN;
      } else {
         cal.setTime(date);
         return (double)cal.get(calField);
      }
   }

   private static String getNameOrAbbrev(String in, String[] formatsIn, String formatOut) throws ParseException {
      int i = 0;

      while(i < formatsIn.length) {
         try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(formatsIn[i], Locale.ENGLISH);
            dateFormat.setLenient(false);
            Date dt = dateFormat.parse(in);
            dateFormat.applyPattern(formatOut);
            return dateFormat.format(dt);
         } catch (ParseException var6) {
            ++i;
         }
      }

      return "";
   }

   private static String getNameOrAbbrev(String format) {
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.ENGLISH);
      return dateFormat.format(cal.getTime());
   }

   public static String formatDate(String dateTime, String pattern) {
      String yearSymbols = "Gy";
      String monthSymbols = "M";
      String daySymbols = "dDEFwW";
      TimeZone timeZone;
      String zone;
      if (!dateTime.endsWith("Z") && !dateTime.endsWith("z")) {
         if (dateTime.length() < 6 || dateTime.charAt(dateTime.length() - 3) != ':' || dateTime.charAt(dateTime.length() - 6) != '+' && dateTime.charAt(dateTime.length() - 6) != '-') {
            timeZone = TimeZone.getDefault();
            zone = "";
         } else {
            String offset = dateTime.substring(dateTime.length() - 6);
            if (!"+00:00".equals(offset) && !"-00:00".equals(offset)) {
               timeZone = TimeZone.getTimeZone("GMT" + offset);
            } else {
               timeZone = TimeZone.getTimeZone("GMT");
            }

            zone = "z";
            dateTime = dateTime.substring(0, dateTime.length() - 6) + "GMT" + offset;
         }
      } else {
         timeZone = TimeZone.getTimeZone("GMT");
         dateTime = dateTime.substring(0, dateTime.length() - 1) + "GMT";
         zone = "z";
      }

      String[] formats = new String[]{"yyyy-MM-dd'T'HH:mm:ss" + zone, "yyyy-MM-dd", "yyyy-MM", "yyyy"};

      try {
         SimpleDateFormat inFormat = new SimpleDateFormat("HH:mm:ss" + zone);
         inFormat.setLenient(false);
         Date d = inFormat.parse(dateTime);
         SimpleDateFormat outFormat = new SimpleDateFormat(strip("GyMdDEFwW", pattern));
         outFormat.setTimeZone(timeZone);
         return outFormat.format(d);
      } catch (ParseException var16) {
         int i = 0;

         SimpleDateFormat inFormat;
         Date d;
         SimpleDateFormat outFormat;
         while(i < formats.length) {
            try {
               inFormat = new SimpleDateFormat(formats[i]);
               inFormat.setLenient(false);
               d = inFormat.parse(dateTime);
               outFormat = new SimpleDateFormat(pattern);
               outFormat.setTimeZone(timeZone);
               return outFormat.format(d);
            } catch (ParseException var15) {
               ++i;
            }
         }

         try {
            inFormat = new SimpleDateFormat("--MM-dd");
            inFormat.setLenient(false);
            d = inFormat.parse(dateTime);
            outFormat = new SimpleDateFormat(strip("Gy", pattern));
            outFormat.setTimeZone(timeZone);
            return outFormat.format(d);
         } catch (ParseException var14) {
            try {
               inFormat = new SimpleDateFormat("--MM--");
               inFormat.setLenient(false);
               d = inFormat.parse(dateTime);
               outFormat = new SimpleDateFormat(strip("Gy", pattern));
               outFormat.setTimeZone(timeZone);
               return outFormat.format(d);
            } catch (ParseException var13) {
               try {
                  inFormat = new SimpleDateFormat("---dd");
                  inFormat.setLenient(false);
                  d = inFormat.parse(dateTime);
                  outFormat = new SimpleDateFormat(strip("GyM", pattern));
                  outFormat.setTimeZone(timeZone);
                  return outFormat.format(d);
               } catch (ParseException var12) {
                  return "";
               }
            }
         }
      }
   }

   private static String strip(String symbols, String pattern) {
      int quoteSemaphore = false;
      int i = 0;
      StringBuffer result = new StringBuffer(pattern.length());

      while(i < pattern.length()) {
         char ch = pattern.charAt(i);
         if (ch == '\'') {
            int endQuote = pattern.indexOf(39, i + 1);
            if (endQuote == -1) {
               endQuote = pattern.length();
            }

            result.append(pattern.substring(i, endQuote));
            i = endQuote++;
         } else if (symbols.indexOf(ch) > -1) {
            ++i;
         } else {
            result.append(ch);
            ++i;
         }
      }

      return result.toString();
   }
}
