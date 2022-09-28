package ui;

import common.CommonUtils;
import common.MudgeSanity;
import graph.Route;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class Sorters {
   private static Set hosts = new HashSet();
   private static Set numbers = new HashSet();

   public static Comparator getStringSorter() {
      return new StringSorter();
   }

   public static Comparator getHostSorter() {
      return new HostSorter();
   }

   public static Comparator getNumberSorter() {
      return new NumberSorter();
   }

   public static Comparator getDateSorter(String var0) {
      return new DateSorter(var0);
   }

   public static Comparator getProperSorter(String var0) {
      if (hosts.contains(var0)) {
         return getHostSorter();
      } else {
         return numbers.contains(var0) ? getNumberSorter() : null;
      }
   }

   static {
      hosts.add("external");
      hosts.add("host");
      hosts.add("Host");
      hosts.add("internal");
      hosts.add("session_host");
      hosts.add("address");
      numbers.add("when");
      numbers.add("last");
      numbers.add("pid");
      numbers.add("port");
      numbers.add("Port");
      numbers.add("sid");
      numbers.add("when");
      numbers.add("date");
      numbers.add("size");
      numbers.add("PID");
      numbers.add("PPID");
      numbers.add("Session");
   }

   private static class StringSorter implements Comparator {
      private StringSorter() {
      }

      public int compare(Object var1, Object var2) {
         if (var1 == null && var2 == null) {
            return this.compare("", "");
         } else if (var1 == null) {
            return this.compare("", var2);
         } else {
            return var2 == null ? this.compare(var1, "") : var1.toString().compareTo(var2.toString());
         }
      }

      // $FF: synthetic method
      StringSorter(Object var1) {
         this();
      }
   }

   private static class NumberSorter implements Comparator {
      private NumberSorter() {
      }

      public int compare(Object var1, Object var2) {
         String var3 = var1.toString();
         String var4 = var2.toString();
         long var5 = CommonUtils.toLongNumber(var3, 0L);
         long var7 = CommonUtils.toLongNumber(var4, 0L);
         if (var5 == var7) {
            return 0;
         } else {
            return var5 > var7 ? 1 : -1;
         }
      }

      // $FF: synthetic method
      NumberSorter(Object var1) {
         this();
      }
   }

   private static class HostSorter implements Comparator {
      private HostSorter() {
      }

      public int compare(Object var1, Object var2) {
         String var3 = var1.toString();
         String var4 = var2.toString();
         if (var3.equals("unknown")) {
            return this.compare("0.0.0.0", var2);
         } else if (var4.equals("unknown")) {
            return this.compare(var1, "0.0.0.0");
         } else {
            long var5 = Route.ipToLong(var3);
            long var7 = Route.ipToLong(var4);
            if (var5 == var7) {
               return 0;
            } else {
               return var5 > var7 ? 1 : -1;
            }
         }
      }

      // $FF: synthetic method
      HostSorter(Object var1) {
         this();
      }
   }

   private static class DateSorter implements Comparator {
      protected SimpleDateFormat parser = null;

      public DateSorter(String var1) {
         try {
            this.parser = new SimpleDateFormat(var1);
         } catch (Exception var3) {
            MudgeSanity.logException("Parser: " + var1, var3, false);
         }

      }

      public int compare(Object var1, Object var2) {
         String var3 = var1.toString();
         String var4 = var2.toString();

         long var5;
         try {
            var5 = this.parser.parse(var3).getTime();
         } catch (Exception var11) {
            var5 = 0L;
         }

         long var7;
         try {
            var7 = this.parser.parse(var4).getTime();
         } catch (Exception var10) {
            var7 = 0L;
         }

         if (var5 == var7) {
            return 0;
         } else {
            return var5 > var7 ? 1 : -1;
         }
      }
   }
}
