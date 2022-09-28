package org.apache.xml.serializer;

public final class Version {
   public static String getVersion() {
      return getProduct() + " " + getImplementationLanguage() + " " + getMajorVersionNum() + "." + getReleaseVersionNum() + "." + (getDevelopmentVersionNum() > 0 ? "D" + getDevelopmentVersionNum() : "" + getMaintenanceVersionNum());
   }

   public static void main(String[] argv) {
      System.out.println(getVersion());
   }

   public static String getProduct() {
      return "Serializer";
   }

   public static String getImplementationLanguage() {
      return "Java";
   }

   public static int getMajorVersionNum() {
      return 2;
   }

   public static int getReleaseVersionNum() {
      return 7;
   }

   public static int getMaintenanceVersionNum() {
      return 0;
   }

   public static int getDevelopmentVersionNum() {
      try {
         return (new String("")).length() == 0 ? 0 : Integer.parseInt("");
      } catch (NumberFormatException var1) {
         return 0;
      }
   }
}
