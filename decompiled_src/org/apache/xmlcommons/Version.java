package org.apache.xmlcommons;

public class Version {
   public static String getVersion() {
      return getProduct() + " " + getVersionNum();
   }

   public static String getProduct() {
      return "XmlCommonsExternal";
   }

   public static String getVersionNum() {
      return "1.3.04";
   }

   public static void main(String[] var0) {
      System.out.println(getVersion());
   }
}
