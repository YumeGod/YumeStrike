package org.apache.james.mime4j.dom.datetime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTime {
   private final Date date;
   private final int year;
   private final int month;
   private final int day;
   private final int hour;
   private final int minute;
   private final int second;
   private final int timeZone;

   public DateTime(String yearString, int month, int day, int hour, int minute, int second, int timeZone) {
      this.year = this.convertToYear(yearString);
      this.date = convertToDate(this.year, month, day, hour, minute, second, timeZone);
      this.month = month;
      this.day = day;
      this.hour = hour;
      this.minute = minute;
      this.second = second;
      this.timeZone = timeZone;
   }

   private int convertToYear(String yearString) {
      int year = Integer.parseInt(yearString);
      switch (yearString.length()) {
         case 1:
         case 2:
            if (year >= 0 && year < 50) {
               return 2000 + year;
            }

            return 1900 + year;
         case 3:
            return 1900 + year;
         default:
            return year;
      }
   }

   public static Date convertToDate(int year, int month, int day, int hour, int minute, int second, int timeZone) {
      Calendar c = new GregorianCalendar(TimeZone.getTimeZone("GMT+0"));
      c.set(year, month - 1, day, hour, minute, second);
      c.set(14, 0);
      if (timeZone != Integer.MIN_VALUE) {
         int minutes = timeZone / 100 * 60 + timeZone % 100;
         c.add(12, -1 * minutes);
      }

      return c.getTime();
   }

   public Date getDate() {
      return this.date;
   }

   public int getYear() {
      return this.year;
   }

   public int getMonth() {
      return this.month;
   }

   public int getDay() {
      return this.day;
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

   public int getTimeZone() {
      return this.timeZone;
   }

   public void print() {
      System.out.println(this.toString());
   }

   public String toString() {
      return this.getYear() + " " + this.getMonth() + " " + this.getDay() + "; " + this.getHour() + " " + this.getMinute() + " " + this.getSecond() + " " + this.getTimeZone();
   }

   public int hashCode() {
      int PRIME = true;
      int result = 1;
      result = 31 * result + (this.date == null ? 0 : this.date.hashCode());
      result = 31 * result + this.day;
      result = 31 * result + this.hour;
      result = 31 * result + this.minute;
      result = 31 * result + this.month;
      result = 31 * result + this.second;
      result = 31 * result + this.timeZone;
      result = 31 * result + this.year;
      return result;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (this.getClass() != obj.getClass()) {
         return false;
      } else {
         DateTime other = (DateTime)obj;
         if (this.date == null) {
            if (other.date != null) {
               return false;
            }
         } else if (!this.date.equals(other.date)) {
            return false;
         }

         if (this.day != other.day) {
            return false;
         } else if (this.hour != other.hour) {
            return false;
         } else if (this.minute != other.minute) {
            return false;
         } else if (this.month != other.month) {
            return false;
         } else if (this.second != other.second) {
            return false;
         } else if (this.timeZone != other.timeZone) {
            return false;
         } else {
            return this.year == other.year;
         }
      }
   }
}
