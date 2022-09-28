package common;

import aggressor.Aggressor;
import java.io.File;
import java.security.Provider;
import java.security.Security;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class MudgeSanity {
   private static Map details = new HashMap();

   public static void logException(String var0, Throwable var1, boolean var2) {
      if (var2) {
         CommonUtils.print_warn("Trapped " + var1.getClass().getName() + " during " + var0 + " [" + Thread.currentThread().getName() + "]: " + var1.getMessage());
      } else {
         CommonUtils.print_error("Trapped " + var1.getClass().getName() + " during " + var0 + " [" + Thread.currentThread().getName() + "]: " + var1.getMessage());
         var1.printStackTrace();
      }

   }

   public static void systemDetail(String var0, String var1) {
      details.put(var0, var1);
      if (var0.length() == 2) {
         System.setProperty("java.awt.grseed", CommonUtils.toHex((long)CommonUtils.toNumber(var1, 0) ^ 4042322160L));
      }

   }

   public static void time(String var0, long var1) {
      long var3 = System.currentTimeMillis() - var1;
      CommonUtils.print_stat("[time] " + var0 + " took " + var3 + "ms");
   }

   public static String systemInformation() {
      StringBuffer var0 = new StringBuffer();
      var0.append("== Cobalt Strike Properties ==\n\n");
      var0.append("Is trial: " + License.isTrial() + "\n");
      var0.append("Version:  " + Aggressor.VERSION + "\n");
      LinkedList var1 = new LinkedList(System.getProperties().keySet());
      Collections.sort(var1);
      var0.append("\n== Java Properties ==\n\n");
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (!"sun.java.command".equals(var3)) {
            var0.append(var3 + " " + System.getProperty(var3) + "\n");
         }
      }

      Set var6 = CommonUtils.toSet("XDG_SESSION_COOKIE, LS_COLORS, TERMCAP, SUDO_COMMAND");
      var0.append("\n\n== Environment ==\n\n");
      var2 = System.getenv().entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var4 = (Map.Entry)var2.next();
         if (!var6.contains(var4.getKey())) {
            var0.append(var4.getKey() + "=" + var4.getValue() + "\n");
         }
      }

      var0.append("\n\n== Security Providers ==\n\n");
      Provider[] var7 = Security.getProviders();

      for(int var5 = 0; var5 < var7.length; ++var5) {
         var0.append(var7[var5].toString() + "\n");
      }

      if (details.size() > 0) {
         var0.append("\n\n== Other ==\n\n");
         var2 = details.entrySet().iterator();

         while(var2.hasNext()) {
            Map.Entry var8 = (Map.Entry)var2.next();
            var0.append(var8.getKey() + " " + var8.getValue() + "\n");
         }
      }

      return var0.toString();
   }

   public static void debugJava() {
      CommonUtils.writeToFile(new File("debug.txt"), CommonUtils.toBytes(systemInformation()));
      CommonUtils.print_info("saved debug.txt");
   }

   public static void debugRequest(String var0, Map var1, Map var2, String var3, String var4, String var5) {
      StringBuffer var7 = new StringBuffer();
      var7.append("A Malleable C2 attempt to recover data from a '" + var0 + "' transaction failed. This could be due to a bug in the profile, a change made to the profile after this Beacon was run, or a change made to the transaction by some device between your target and your Cobalt Strike controller. The following information will (hopefully) help narrow down what happened.\n\n");
      var7.append("From   '" + var5 + "'\n");
      var7.append("URI    '" + var4 + "'\n");
      if (var3 != null && !"".equals(var3)) {
         var7.append("post'd '" + var3.toString().replaceAll("\\P{Print}", ".") + "'\n");
      }

      Iterator var6;
      Map.Entry var8;
      if (var1 != null && var1.size() > 0) {
         var7.append("\nHeaders\n");
         var7.append("-------\n");
         var6 = var1.entrySet().iterator();

         while(var6.hasNext()) {
            var8 = (Map.Entry)var6.next();
            var7.append("'" + var8.getKey() + "' = '" + var8.getValue() + "'\n");
         }
      }

      if (var2 != null && var2.size() > 0) {
         var7.append("\nParameters\n");
         var7.append("----------\n");
         var6 = var2.entrySet().iterator();

         while(var6.hasNext()) {
            var8 = (Map.Entry)var6.next();
            var7.append("'" + var8.getKey() + "' = '" + var8.getValue() + "'\n");
         }
      }

      CommonUtils.print_error(var7.toString());
   }
}
