package de.javasoft.util;

import java.text.NumberFormat;
import java.text.ParseException;

public class JavaVersion {
   private static final String VERSION = System.getProperty("java.version");
   public static final boolean JAVA5;
   public static final boolean JAVA6;
   public static final boolean JAVA6U10_OR_ABOVE;
   public static final boolean JAVA7;
   public static final boolean JAVA7_OR_ABOVE;
   public static final boolean JAVA7U8_OR_ABOVE;
   public static final boolean JAVA8;
   public static final boolean JAVA9;
   public static final boolean JAVA8_OR_ABOVE;
   public static final boolean JAVA9_OR_ABOVE;

   static {
      JAVA5 = VERSION.startsWith("1.5.");
      JAVA6 = VERSION.startsWith("1.6.");
      JAVA6U10_OR_ABOVE = isJava6uNOrAbove(10);
      JAVA7 = VERSION.startsWith("1.7.");
      JAVA7_OR_ABOVE = !JAVA5 && !JAVA6;
      JAVA7U8_OR_ABOVE = isJava7uNOrAbove(8);
      JAVA8 = VERSION.startsWith("1.8.");
      JAVA9 = VERSION.startsWith("9");
      JAVA8_OR_ABOVE = JAVA8 || !JAVA7 && !JAVA6 && !JAVA5;
      JAVA9_OR_ABOVE = JAVA9 || !JAVA8 && !JAVA7 && !JAVA6 && !JAVA5;
   }

   public static boolean isJava6uNOrAbove(int var0) {
      if (JAVA5) {
         return false;
      } else if (VERSION.equals("1.6.0")) {
         return false;
      } else if (VERSION.startsWith("1.6.0_")) {
         try {
            int var1 = ((Long)NumberFormat.getIntegerInstance().parse(VERSION.substring(6))).intValue();
            return var1 >= var0;
         } catch (ParseException var2) {
            return false;
         }
      } else {
         return true;
      }
   }

   public static boolean isJava7uNOrAbove(int var0) {
      if (!JAVA5 && !JAVA6) {
         if (VERSION.equals("1.7.0")) {
            return false;
         } else if (VERSION.startsWith("1.7.0_")) {
            try {
               int var1 = ((Long)NumberFormat.getIntegerInstance().parse(VERSION.substring(6))).intValue();
               return var1 >= var0;
            } catch (ParseException var2) {
               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }
}
