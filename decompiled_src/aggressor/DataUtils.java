package aggressor;

import beacon.BeaconCommands;
import beacon.BeaconElevators;
import beacon.BeaconExploits;
import beacon.BeaconRemoteExecMethods;
import beacon.BeaconRemoteExploits;
import c2profile.Profile;
import common.BeaconEntry;
import common.BeaconOutput;
import common.Callback;
import common.CodeSigner;
import common.CommonUtils;
import common.DataParser;
import common.ListenerUtils;
import common.MudgeSanity;
import dialog.DialogUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class DataUtils {
   protected static Map tokenCache = new HashMap();

   public static final String getNick(DataManager var0) {
      return var0.getMapSafe("metadata").get("nick") + "";
   }

   public static final Map getListenerByName(DataManager var0, String var1) {
      return (Map)((Map)var0.getMapSafe("listeners").get(var1));
   }

   public static final Map getListeners(DataManager var0) {
      return var0.getMapSafe("listeners");
   }

   public static final List getNamedPipes(DataManager var0) {
      Iterator var1 = getListeners(var0).values().iterator();
      LinkedList var2 = new LinkedList();

      while(var1.hasNext()) {
         Map var3 = (Map)var1.next();
         if ("windows/beacon_bind_pipe".equals(DialogUtils.string(var3, "payload"))) {
            var2.add(DialogUtils.string(var3, "port"));
         }
      }

      return var2;
   }

   public static final List getTCPPorts(DataManager var0) {
      Iterator var1 = getListeners(var0).values().iterator();
      LinkedList var2 = new LinkedList();

      while(var1.hasNext()) {
         Map var3 = (Map)var1.next();
         if ("windows/beacon_bind_tcp".equals(DialogUtils.string(var3, "payload"))) {
            var2.add(DialogUtils.string(var3, "port"));
         }
      }

      return var2;
   }

   public static final long AdjustForSkew(DataManager var0, long var1) {
      long var3 = CommonUtils.toLongNumber(var0.getMapSafe("metadata").get("clockskew") + "", 0L);
      return var1 - var3;
   }

   public static String getDefaultPipeName(DataManager var0, String var1) {
      Profile var2 = getProfile(var0);
      byte[] var3 = getPublicKey(var0);
      return getDefaultPipeName(var2, var3, var1);
   }

   public static String getDefaultPipeName(Profile var0, byte[] var1, String var2) {
      String var3 = var0.getString(".pipename");
      if (!CommonUtils.isin("##", var3)) {
         return "\\\\" + var2 + "\\pipe\\" + var3;
      } else {
         try {
            long var4 = 0L;
            long var6 = 0L;
            long var8 = 0L;
            DataParser var11 = new DataParser(var1);
            var11.consume(32);
            var4 = CommonUtils.toUnsignedInt(var11.readInt());
            var6 = CommonUtils.toUnsignedInt(var11.readInt());
            var6 = 36969L * (var6 & 65535L) + (var6 >> 16);
            var4 = 18000L * (var4 & 65553L) + (var4 >> 16);
            var8 = (var6 ^ var4) % 65535L;
            return "\\\\" + var2 + "\\pipe\\" + CommonUtils.strrep(var3, "##", CommonUtils.toHex(var8));
         } catch (Exception var12) {
            MudgeSanity.logException("Could not calculate pipe rand value", var12, false);
            return "\\\\" + var2 + "\\pipe\\" + var3;
         }
      }
   }

   public static byte[] getPublicKey(DataManager var0) {
      return (byte[])((byte[])var0.getMapSafe("metadata").get("pubkey"));
   }

   public static Profile getProfile(DataManager var0) {
      return (Profile)var0.getMapSafe("metadata").get("c2profile");
   }

   public static boolean hasValidSSL(DataManager var0) {
      return "true".equals(var0.getMapSafe("metadata").get("validssl"));
   }

   public static boolean disableAMSI(DataManager var0) {
      return "true".equals(var0.getMapSafe("metadata").get("amsi_disable"));
   }

   public static boolean obfuscatePostEx(DataManager var0) {
      return "true".equals(var0.getMapSafe("metadata").get("postex_obfuscate"));
   }

   public static boolean useSmartInject(DataManager var0) {
      return "true".equals(var0.getMapSafe("metadata").get("postex_smartinject"));
   }

   public static boolean hasImportedPowerShell(DataManager var0, String var1) {
      return getBeaconPowerShellCommands(var0, var1).size() > 0;
   }

   public static final void reportPowerShellImport(DataManager var0, String var1, List var2) {
      var0.put("cmdlets", var1, var2);
   }

   public static final Map getC2Info(DataManager var0) {
      HashMap var1 = new HashMap();
      Iterator var2 = var0.getMapSafe("metadata").entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         if (var3.getKey().toString().startsWith("c2sample.")) {
            var1.put(var3.getKey().toString().substring(9), var3.getValue());
         }
      }

      var1.put("callbacks", var0.getListSafe("c2info"));
      return var1;
   }

   public static final CodeSigner getSigner(DataManager var0) {
      return (CodeSigner)((CodeSigner)var0.getMapSafe("metadata").get("signer"));
   }

   public static final Collection getUsers(DataManager var0) {
      return var0.getSetSafe("users");
   }

   public static long getTime(DataManager var0) {
      return System.currentTimeMillis();
   }

   public static String getBeaconPid(DataManager var0, String var1) {
      BeaconEntry var2 = getBeacon(var0, var1);
      return var2 != null ? var2.getPid() : "";
   }

   public static BeaconEntry getBeacon(DataManager var0, String var1) {
      return getBeaconFromResult(getBeacons(var0), var1);
   }

   public static List getBeaconChain(DataManager var0, String var1) {
      return getBeaconChain(var0, var1, new LinkedList());
   }

   private static List getBeaconChain(DataManager var0, String var1, List var2) {
      BeaconEntry var3 = getBeacon(var0, var1);
      if (var3 != null) {
         var2.add(var3.getInternal());
      }

      return var3.isLinked() ? getBeaconChain(var0, var3.getParentId(), var2) : var2;
   }

   public static byte[] encodeForBeacon(DataManager var0, String var1, String var2) {
      BeaconEntry var3 = getBeacon(var0, var1);
      return var3 != null && !var3.isEmpty() ? CommonUtils.toBytes(var2, var3.getCharset()) : CommonUtils.toBytes(var2);
   }

   public static String decodeForBeacon(DataManager var0, String var1, byte[] var2) {
      BeaconEntry var3 = getBeacon(var0, var1);
      return var3 == null ? CommonUtils.bString(var2) : CommonUtils.bString(var2, var3.getCharset());
   }

   public static BeaconEntry getEgressBeacon(DataManager var0, String var1) {
      BeaconEntry var2 = getBeacon(var0, var1);
      if (var2 == null) {
         return null;
      } else {
         return var2.isLinked() ? getEgressBeacon(var0, var2.getParentId()) : var2;
      }
   }

   public static Map getBeacons(DataManager var0) {
      return var0.getMapSafe("beacons");
   }

   public static List getBeaconChildren(DataManager var0, String var1) {
      Iterator var2 = getBeacons(var0).entrySet().iterator();
      LinkedList var3 = new LinkedList();

      while(var2.hasNext()) {
         Map.Entry var4 = (Map.Entry)var2.next();
         BeaconEntry var5 = (BeaconEntry)var4.getValue();
         if (var1.equals(var5.getParentId())) {
            var3.add(var5);
         }
      }

      return var3;
   }

   public static BeaconEntry getBeaconFromResult(Object var0, String var1) {
      Map var2 = (Map)var0;
      return var2.containsKey(var1) ? (BeaconEntry)var2.get(var1) : null;
   }

   public static List getBeaconModel(DataManager var0) {
      return getBeaconModelFromResult(getBeacons(var0));
   }

   public static List getBeaconModelFromResult(Object var0) {
      LinkedList var1 = new LinkedList();
      Map var2 = (Map)var0;

      Map var5;
      for(Iterator var3 = var2.values().iterator(); var3.hasNext(); var1.add(var5)) {
         BeaconEntry var4 = (BeaconEntry)var3.next();
         var5 = var4.toMap();
         if (var4.isEmpty()) {
            var5.put("image", DialogUtils.TargetVisualizationSmall("unknown", 0.0, false, false));
         } else {
            var5.put("image", DialogUtils.TargetVisualizationSmall(var4.getOperatingSystem().toLowerCase(), var4.getVersion(), var4.isAdmin(), !var4.isAlive()));
         }
      }

      return var1;
   }

   public static List getSites(GenericDataManager var0) {
      return var0.getListSafe("sites");
   }

   public static List getTargetNames(DataManager var0) {
      LinkedList var1 = new LinkedList();
      Iterator var2 = var0.getListSafe("targets").iterator();

      while(var2.hasNext()) {
         Map var3 = (Map)var2.next();
         String var4 = (String)var3.get("name");
         if (var4 != null) {
            var1.add(var4);
         }
      }

      return var1;
   }

   public static List getListenerModel(GenericDataManager var0, DataManager var1) {
      Map var2 = ListenerUtils.filterLocal(var0.getMapSafe("listeners"));
      Map var3 = var1.getMapSafe("listeners");
      var2.putAll(var3);
      return new LinkedList(var2.values());
   }

   public static String getAddressFor(DataManager var0, String var1) {
      List var2 = var0.getListSafe("targets");
      Iterator var3 = var2.iterator();

      Map var4;
      do {
         if (!var3.hasNext()) {
            return var1;
         }

         var4 = (Map)var3.next();
      } while(!var1.equals(var4.get("address")));

      String var5 = (String)var4.get("name");
      if (var5 != null && !"".equals(var5)) {
         return var5;
      } else {
         return var1;
      }
   }

   public static List getListenerModel(DataManager var0) {
      return new LinkedList(var0.getMapSafe("listeners").values());
   }

   public static List getBeaconTranscriptAndSubscribe(DataManager var0, String var1, Callback var2) {
      LinkedList var3 = var0.getTranscriptAndSubscribeSafe("beaconlog", var2);
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         BeaconOutput var5 = (BeaconOutput)var4.next();
         if (!var5.is(var1)) {
            var4.remove();
         }
      }

      return var3;
   }

   public static List getScreenshotTranscript(DataManager var0) {
      return var0.getTranscriptSafe("screenshots");
   }

   public static List getKeystrokesTranscript(DataManager var0) {
      return var0.getTranscriptSafe("keystrokes");
   }

   public static BeaconCommands getBeaconCommands(DataManager var0) {
      return (BeaconCommands)var0.get("beacon_commands", (Object)null);
   }

   public static BeaconCommands getSSHCommands(DataManager var0) {
      return (BeaconCommands)var0.get("ssh_commands", (Object)null);
   }

   public static BeaconExploits getBeaconExploits(DataManager var0) {
      return (BeaconExploits)var0.get("beacon_exploits", (Object)null);
   }

   public static BeaconElevators getBeaconElevators(DataManager var0) {
      return (BeaconElevators)var0.get("beacon_elevators", (Object)null);
   }

   public static BeaconRemoteExploits getBeaconRemoteExploits(DataManager var0) {
      return (BeaconRemoteExploits)var0.get("beacon_remote_exploits", (Object)null);
   }

   public static BeaconRemoteExecMethods getBeaconRemoteExecMethods(DataManager var0) {
      return (BeaconRemoteExecMethods)var0.get("beacon_remote_exec_methods", (Object)null);
   }

   public static List getBeaconPowerShellCommands(DataManager var0, String var1) {
      Map var2 = var0.getMapSafe("cmdlets");
      List var3 = (List)var2.get(var1);
      return (List)(var3 == null ? new LinkedList() : var3);
   }

   public static String getPrimaryStage(DataManager var0) {
      List var1 = getListenerModel(var0);
      Iterator var2 = var1.iterator();

      String var4;
      do {
         if (!var2.hasNext()) {
            return "";
         }

         Map var3 = (Map)var2.next();
         var4 = var3.get("payload") + "";
         if ("windows/beacon_http/reverse_http".equals(var4)) {
            return "HTTP Beacon";
         }

         if ("windows/beacon_https/reverse_https".equals(var4)) {
            return "HTTPS Beacon";
         }

         if ("windows/beacon_dns/reverse_http".equals(var4)) {
            return "DNS Beacon";
         }
      } while(!"windows/beacon_dns/reverse_dns_txt".equals(var4));

      return "DNS Beacon";
   }

   public static String getLocalIP(DataManager var0) {
      return (String)var0.get("localip", "127.0.0.1");
   }

   public static String getTeamServerIP(DataManager var0) {
      return var0.getMapSafe("options").get("host") + "";
   }

   public static List getInterfaceList(DataManager var0) {
      List var1 = var0.getListSafe("interfaces");
      LinkedList var2 = new LinkedList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         var2.add(var4.get("interface"));
      }

      return var2;
   }

   public static Map getInterface(DataManager var0, String var1) {
      List var2 = var0.getListSafe("interfaces");
      Iterator var3 = var2.iterator();

      Map var4;
      do {
         if (!var3.hasNext()) {
            return new HashMap();
         }

         var4 = (Map)var3.next();
      } while(!var1.equals(var4.get("interface")));

      return var4;
   }

   public static String getManualProxySetting(DataManager var0) {
      String var1 = (String)var0.getDataSafe("manproxy");
      return var1 == null ? "" : var1;
   }

   public static Map getGoldenTicket(DataManager var0) {
      return var0.getMapSafe("goldenticket");
   }

   public static String TokenToEmail(String var0) {
      if (var0 != null && !"".equals(var0)) {
         synchronized(tokenCache) {
            if (tokenCache.containsKey(var0)) {
               return (String)tokenCache.get(var0);
            } else {
               GlobalDataManager var2 = GlobalDataManager.getGlobalDataManager();
               List var3 = var2.getListSafe("tokens");
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  Map var5 = (Map)var4.next();
                  String var6 = (String)var5.get("token");
                  String var7 = (String)var5.get("email");
                  tokenCache.put(var6, var7);
               }

               return tokenCache.containsKey(var0) ? (String)tokenCache.get(var0) : "unknown";
            }
         }
      } else {
         return "unknown";
      }
   }
}
