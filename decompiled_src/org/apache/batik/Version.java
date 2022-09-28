package org.apache.batik;

public final class Version {
   // $FF: synthetic field
   static Class class$org$apache$batik$Version;

   public static String getVersion() {
      Package var0 = (class$org$apache$batik$Version == null ? (class$org$apache$batik$Version = class$("org.apache.batik.Version")) : class$org$apache$batik$Version).getPackage();
      String var1 = null;
      if (var0 != null) {
         var1 = var0.getImplementationVersion();
      }

      String var2 = "$HeadURL: https://svn.apache.org/repos/asf/xmlgraphics/batik/tags/batik-1_7/sources/org/apache/batik/Version.java $";
      String var3 = "$HeadURL: ";
      String var4 = "/sources/org/apache/batik/Version.java $";
      if (var2.startsWith(var3) && var2.endsWith(var4)) {
         var2 = var2.substring(var3.length(), var2.length() - var4.length());
         if (!var2.endsWith("/trunk")) {
            int var5 = var2.lastIndexOf(47);
            int var6 = var2.lastIndexOf(47, var5 - 1);
            String var7 = var2.substring(var5 + 1);
            String var8 = var2.substring(var6 + 1, var5);
            String var9 = "batik-";
            if (var8.equals("tags") && var7.startsWith(var9)) {
               var1 = var7.substring(var9.length()).replace('_', '.');
            } else if (var8.equals("branches")) {
               var1 = var1 + "; " + var7;
            }
         }
      }

      if (var1 == null) {
         var1 = "development version";
      }

      return var1;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
