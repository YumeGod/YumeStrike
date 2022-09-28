package server;

import beacon.BeaconPayload;
import c2profile.Profile;
import common.BeaconEntry;
import common.CommonUtils;
import common.GenericEvent;
import common.StringStack;
import java.util.HashMap;
import java.util.Map;

public class ServerUtils {
   public static Profile getProfile(Resources var0) {
      return (Profile)var0.get("c2profile");
   }

   public static String randua(Resources var0) {
      return BeaconPayload.randua(getProfile(var0));
   }

   public static WebCalls getWebCalls(Resources var0) {
      return (WebCalls)var0.get("webcalls");
   }

   public static boolean hasPublicStage(Resources var0) {
      return getProfile(var0).option(".host_stage");
   }

   public static String getMyIP(Resources var0) {
      return (String)var0.get("localip");
   }

   public static String getServerPassword(Resources var0, String var1) {
      return (String)var0.get("password");
   }

   public static BeaconEntry getBeacon(Resources var0, String var1) {
      return ((Beacons)var0.get("beacons")).resolve(var1);
   }

   public static void addToken(Resources var0, String var1, String var2, String var3) {
      HashMap var4 = new HashMap();
      var4.put("token", var1);
      var4.put("email", var2);
      var4.put("cid", var3);
      var0.call("tokens.add", CommonUtils.args(CommonUtils.TokenKey(var4), var4));
   }

   public static void addSession(Resources var0, Map var1) {
      var1.put("opened", System.currentTimeMillis() + "");
      var0.call("sessions.addnew", CommonUtils.args(CommonUtils.SessionKey(var1), var1));
      var0.call("sessions.push");
   }

   public static void addC2Info(Resources var0, Map var1) {
      var0.call("c2info.addnew", CommonUtils.args(CommonUtils.C2InfoKey(var1), var1));
      var0.call("c2info.push");
   }

   public static void addCredential(Resources var0, String var1, String var2, String var3, String var4, String var5, long var6) {
      HashMap var8 = new HashMap();
      var8.put("user", var1);
      var8.put("password", var2);
      var8.put("realm", var3);
      var8.put("source", var4);
      var8.put("host", var5);
      if (var6 > 0L) {
         var8.put("logon", Long.toString(var6));
      }

      String var9 = CommonUtils.CredKey(var8);
      var0.call("credentials.addnew", CommonUtils.args(var9, var8));
   }

   public static void addCredential(Resources var0, String var1, String var2, String var3, String var4, String var5) {
      addCredential(var0, var1, var2, var3, var4, var5, 0L);
   }

   public static void addTarget(Resources var0, String var1, String var2, String var3, String var4, double var5) {
      HashMap var7 = new HashMap();
      var7.put("address", var1);
      if (var2 != null) {
         var7.put("name", var2);
      }

      if (var3 != null) {
         var7.put("note", var3);
      }

      if (var4 != null) {
         var7.put("os", var4);
      }

      if (var5 != 0.0) {
         var7.put("version", var5 + "");
      }

      String var8 = CommonUtils.TargetKey(var7);
      var0.call("targets.update", CommonUtils.args(var8, var7));
      var0.call("targets.push");
   }

   public static void fireEvent(Resources var0, String var1, String var2) {
      var0.broadcast("propagate", new GenericEvent(var1, var2));
   }

   public static String getRemoteAddress(Profile var0, Map var1) {
      boolean var2 = var0.option(".http-config.trust_x_forwarded_for");
      String var3;
      if (var2 && var1.containsKey("X-Forwarded-For")) {
         var3 = (String)var1.get("X-Forwarded-For");
         if (var3.indexOf(",") > -1) {
            var3 = CommonUtils.strrep(var3, " ", "");
            StringStack var4 = new StringStack(var3, ",");
            var3 = var4.shift();
         }

         if (CommonUtils.isIP(var3) || CommonUtils.isIPv6(var3)) {
            return var3;
         }

         CommonUtils.print_error("remote address '" + (String)var1.get("X-Forwarded-For") + "' in X-Forwarded-For header is not valid.");
      }

      var3 = (String)var1.get("REMOTE_ADDRESS");
      return "".equals(var3) ? "" : var3.substring(1);
   }
}
