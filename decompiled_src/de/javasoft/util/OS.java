package de.javasoft.util;

public enum OS {
   Windows,
   Mac,
   Linux,
   FreeBSD,
   Solaris,
   Unknown;

   private static final String OS_NAME = System.getProperty("os.name");
   private static final String OS_VERSION = System.getProperty("os.version");

   public static OS getCurrentOS() {
      if (OS_NAME.startsWith("Windows")) {
         return Windows;
      } else if (OS_NAME.startsWith("Mac")) {
         return Mac;
      } else if (!OS_NAME.startsWith("Linux") && !OS_NAME.toUpperCase().startsWith("LINUX")) {
         if (!OS_NAME.startsWith("FreeBSD") && !OS_NAME.toUpperCase().startsWith("FREEBSD")) {
            return OS_NAME.startsWith("Solaris") ? Solaris : Unknown;
         } else {
            return FreeBSD;
         }
      } else {
         return Linux;
      }
   }

   public static String getName() {
      return OS_NAME;
   }

   public static String getVersion() {
      return OS_VERSION;
   }
}
