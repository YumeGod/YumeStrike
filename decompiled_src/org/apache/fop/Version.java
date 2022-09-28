package org.apache.fop;

public final class Version {
   private Version() {
   }

   public static String getVersion() {
      String version = null;
      Package jarinfo = Version.class.getPackage();
      if (jarinfo != null) {
         version = jarinfo.getImplementationVersion();
      }

      if (version == null) {
         String headURL = "$HeadURL: https://svn.apache.org/repos/asf/xmlgraphics/fop/tags/fop-1_0/src/java/org/apache/fop/Version.java $";
         String pathPrefix = "/xmlgraphics/fop/";
         int pos = headURL.indexOf("/xmlgraphics/fop/");
         if (pos >= 0) {
            version = headURL.substring(pos + "/xmlgraphics/fop/".length() - 1, headURL.length() - 2);
            pos = version.indexOf("/src/");
            version = version.substring(1, pos);
            version = " " + version;
         } else {
            version = "";
         }

         version = "SVN" + version;
      }

      return version;
   }
}
