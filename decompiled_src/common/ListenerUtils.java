package common;

import aggressor.AggressorClient;
import aggressor.DataManager;
import aggressor.DataUtils;
import aggressor.GlobalDataManager;
import dialog.DialogUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListenerUtils {
   public static boolean checkPort(Map var0, String var1, String var2) {
      String var3 = (String)var0.get(var1);
      int var4 = CommonUtils.toNumber(var3, 0);
      if (!"".equals(var3) && var3 != null) {
         if (!CommonUtils.isNumber(var3)) {
            DialogUtils.showError(var2 + " is not a valid number.");
            return false;
         } else if (var4 >= 0 && var4 <= 65535) {
            return true;
         } else {
            DialogUtils.showError(var2 + " is out of range for a port.");
            return false;
         }
      } else {
         DialogUtils.showError(var2 + " is a required value.");
         return false;
      }
   }

   public static boolean validate(Map var0) {
      String var1 = (String)var0.get("name");
      String var2 = (String)var0.get("host");
      String var3 = (String)var0.get("beacons");
      String var4 = (String)var0.get("payload");
      CommonUtils.print_info("Payload is '" + var4 + "' and " + var0);
      if (var1 != null && !"".equals(var1)) {
         if (var4 != null && !"".equals(var4)) {
            if (var4.equals("windows/beacon_bind_tcp")) {
               if (!checkPort(var0, "port", "Port (C2)")) {
                  return false;
               }
            } else if (var4.equals("windows/beacon_extc2")) {
               if (!checkPort(var0, "port", "Port (Bind)")) {
                  return false;
               }
            } else if (var4.equals("windows/beacon_bind_pipe")) {
               String var6 = (String)var0.get("port");
               if ("".equals(var6) || var6 == null) {
                  DialogUtils.showError("Pipename (C2) need a value");
                  return false;
               }

               if (var6.length() > 118) {
                  DialogUtils.showError("Pipename (C2) is too long. Max is 118 characters.");
                  return false;
               }
            } else if (!var4.equals("windows/foreign/reverse_http") && !var4.equals("windows/foreign/reverse_https")) {
               if (!checkPort(var0, "port", "Port")) {
                  return false;
               }

               int var5 = DialogUtils.number(var0, "port");
               if (var5 == 19 || var5 == 21 || var5 == 25 || var5 == 110 || var5 == 119 || var5 == 143 || var5 == 220 || var5 == 993 || var5 == 220 || var5 == 993) {
                  DialogUtils.showError("Port " + var5 + " is blocked by WinINet to prevent Cross Service/Request Forgery in Internet Explorer.");
                  return false;
               }

               if (!"".equals(var0.get("bindto")) && var0.get("bindto") != null && !checkPort(var0, "bindto", "Port (Bind)")) {
                  return false;
               }

               if (var2 == null || "".equals(var2)) {
                  DialogUtils.showError("Host (Stager) value is required for a listener");
                  return false;
               }

               if (var2.indexOf(",") > -1 || var2.indexOf(" ") > -1) {
                  DialogUtils.showError("Please specify one value in the Host (Stager) field");
                  return false;
               }

               if (var3 == null || "".equals(var3)) {
                  DialogUtils.showError("Please specify one or more Callback Hosts");
                  return false;
               }
            } else {
               if (!checkPort(var0, "port", "Port")) {
                  return false;
               }

               if (var2 != null && !"".equals(var2)) {
                  if (var2.indexOf(",") <= -1 && var2.indexOf(" ") <= -1) {
                     return true;
                  }

                  DialogUtils.showError("Please specify one value in the Host (Stager) field");
                  return false;
               }

               DialogUtils.showError("Host (Stager) value is required for a listener");
               return false;
            }

            return true;
         } else {
            DialogUtils.showError("Please select a payload");
            return false;
         }
      } else {
         DialogUtils.showError("Your listener needs a name");
         return false;
      }
   }

   public static Map dialogToMap(Map var0) {
      HashMap var1 = new HashMap();
      String var2 = CommonUtils.trim(DialogUtils.string(var0, "name"));
      String var3 = DialogUtils.string(var0, "payload");
      var1.put("name", var2);
      if ("Beacon HTTP".equals(var3)) {
         var1.put("payload", "windows/beacon_http/reverse_http");
         var1.put("port", DialogUtils.string(var0, "http_port"));
         var1.put("host", DialogUtils.string(var0, "http_host"));
         var1.put("beacons", DialogUtils.string(var0, "http_hosts"));
         var1.put("proxy", DialogUtils.string(var0, "http_proxy"));
         var1.put("althost", DialogUtils.string(var0, "http_hosth"));
         var1.put("bindto", DialogUtils.string(var0, "http_bind"));
         var1.put("profile", DialogUtils.string(var0, "http_profile"));
      } else if ("Beacon HTTPS".equals(var3)) {
         var1.put("payload", "windows/beacon_https/reverse_https");
         var1.put("port", DialogUtils.string(var0, "https_port"));
         var1.put("host", DialogUtils.string(var0, "https_host"));
         var1.put("beacons", DialogUtils.string(var0, "https_hosts"));
         var1.put("proxy", DialogUtils.string(var0, "https_proxy"));
         var1.put("althost", DialogUtils.string(var0, "https_hosth"));
         var1.put("bindto", DialogUtils.string(var0, "https_bind"));
         var1.put("profile", DialogUtils.string(var0, "https_profile"));
      } else if ("External C2".equals(var3)) {
         var1.put("payload", "windows/beacon_extc2");
         var1.put("localonly", DialogUtils.string(var0, "extc2_local"));
         var1.put("port", DialogUtils.string(var0, "extc2_port"));
         if (DialogUtils.bool(var0, "extc2_local")) {
            var1.put("beacons", "127.0.0.1");
         } else {
            var1.put("beacons", "0.0.0.0");
         }
      } else if ("Foreign HTTP".equals(var3)) {
         var1.put("payload", "windows/foreign/reverse_http");
         var1.put("port", DialogUtils.string(var0, "http_f_port"));
         var1.put("host", DialogUtils.string(var0, "http_f_host"));
      } else if ("Foreign HTTPS".equals(var3)) {
         var1.put("payload", "windows/foreign/reverse_https");
         var1.put("port", DialogUtils.string(var0, "https_f_port"));
         var1.put("host", DialogUtils.string(var0, "https_f_host"));
      } else if ("Beacon DNS".equals(var3)) {
         var1.put("payload", "windows/beacon_dns/reverse_dns_txt");
         var1.put("beacons", DialogUtils.string(var0, "dns_hosts"));
         var1.put("host", DialogUtils.string(var0, "dns_host"));
         var1.put("bindto", DialogUtils.string(var0, "dns_bind"));
         var1.put("port", "53");
      } else if ("Beacon SMB".equals(var3)) {
         var1.put("payload", "windows/beacon_bind_pipe");
         var1.put("port", DialogUtils.string(var0, "smb_pipe"));
      } else if ("Beacon TCP".equals(var3)) {
         var1.put("payload", "windows/beacon_bind_tcp");
         var1.put("localonly", DialogUtils.string(var0, "tcp_local"));
         var1.put("port", DialogUtils.string(var0, "tcp_port"));
         if (DialogUtils.bool(var0, "tcp_local")) {
            var1.put("beacons", "127.0.0.1");
         } else {
            var1.put("beacons", "0.0.0.0");
         }
      }

      return var1;
   }

   public static Map ExternalC2Map(String var0) {
      HashMap var1 = new HashMap();
      var1.put("payload", "windows/beacon_bind_pipe");
      var1.put("port", var0);
      var1.put("localhost", "true");
      var1.put("name", "<ExternalC2.Anonymous>");
      return var1;
   }

   public static Map mapToDialog(Map var0) {
      HashMap var1 = new HashMap();
      String var2 = CommonUtils.trim(DialogUtils.string(var0, "name"));
      String var3 = DialogUtils.string(var0, "payload");
      var1.put("name", var2);
      if ("windows/beacon_http/reverse_http".equals(var3)) {
         var1.put("payload", "Beacon HTTP");
         var1.put("http_port", var0.get("port"));
         var1.put("http_host", var0.get("host"));
         var1.put("http_hosts", var0.get("beacons"));
         var1.put("http_proxy", var0.get("proxy"));
         var1.put("http_hosth", var0.get("althost"));
         var1.put("http_bind", var0.get("bindto"));
         var1.put("http_profile", var0.get("profile"));
      } else if ("windows/beacon_https/reverse_https".equals(var3)) {
         var1.put("payload", "Beacon HTTPS");
         var1.put("https_port", var0.get("port"));
         var1.put("https_host", var0.get("host"));
         var1.put("https_hosts", var0.get("beacons"));
         var1.put("https_proxy", var0.get("proxy"));
         var1.put("https_hosth", var0.get("althost"));
         var1.put("https_bind", var0.get("bindto"));
         var1.put("https_profile", var0.get("profile"));
      } else if ("windows/beacon_dns/reverse_dns_txt".equals(var3)) {
         var1.put("payload", "Beacon DNS");
         var1.put("dns_port", var0.get("port"));
         var1.put("dns_host", var0.get("host"));
         var1.put("dns_hosts", var0.get("beacons"));
         var1.put("dns_bind", var0.get("bindto"));
      } else if ("windows/beacon_bind_pipe".equals(var3)) {
         var1.put("payload", "Beacon SMB");
         var1.put("smb_pipe", var0.get("port"));
      } else if ("windows/beacon_bind_tcp".equals(var3)) {
         var1.put("payload", "Beacon TCP");
         var1.put("tcp_port", var0.get("port"));
         var1.put("tcp_local", var0.get("localonly"));
      } else if ("windows/beacon_extc2".equals(var3)) {
         var1.put("payload", "External C2");
         var1.put("extc2_local", var0.get("localonly"));
         var1.put("extc2_port", var0.get("port"));
      } else if ("windows/foreign/reverse_http".equals(var3)) {
         var1.put("payload", "Foreign HTTP");
         var1.put("http_f_port", var0.get("port"));
         var1.put("http_f_host", var0.get("host"));
      } else if ("windows/foreign/reverse_https".equals(var3)) {
         var1.put("payload", "Foreign HTTPS");
         var1.put("https_f_port", var0.get("port"));
         var1.put("https_f_host", var0.get("host"));
      }

      return var1;
   }

   public static boolean isVisible(Map var0) {
      String var1 = DialogUtils.string(var0, "payload");
      return !var1.equals("windows/beacon_extc2");
   }

   public static List getListenerNames(AggressorClient var0) {
      LinkedList var1 = new LinkedList();
      Iterator var2 = getAllListeners(var0).iterator();

      while(var2.hasNext()) {
         Map var3 = (Map)var2.next();
         if (isVisible(var3)) {
            var1.add(DialogUtils.string(var3, "name"));
         }
      }

      return var1;
   }

   public static boolean isListener(String var0) {
      Iterator var1 = GlobalDataManager.getGlobalDataManager().getStore("listeners").entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         Map var3 = (Map)var2.getValue();
         Iterator var4 = var3.values().iterator();

         while(var4.hasNext()) {
            Map var5 = (Map)var4.next();
            String var6 = DialogUtils.string(var5, "name");
            if (var0.equals(var6) && isVisible(var5)) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean isLocalListener(AggressorClient var0, String var1) {
      return DataUtils.getListenerByName(var0.getData(), var1) != null;
   }

   public static ScListener getListener(AggressorClient var0, String var1) {
      Map var2 = DataUtils.getListenerByName(var0.getData(), var1);
      if (var2 != null) {
         return new ScListener(var0.getData(), var2);
      } else {
         GlobalDataManager var3 = GlobalDataManager.getGlobalDataManager();
         Map var4 = var3.getStore("listeners");
         Iterator var5 = var4.entrySet().iterator();

         DataManager var7;
         Map var8;
         do {
            if (!var5.hasNext()) {
               return null;
            }

            Map.Entry var6 = (Map.Entry)var5.next();
            var7 = (DataManager)var6.getKey();
            var8 = (Map)var6.getValue();
         } while(!var8.containsKey(var1));

         return new ScListener(var7, (Map)var8.get(var1));
      }
   }

   public static List getListenersLocal(AggressorClient var0) {
      LinkedList var1 = new LinkedList();
      Iterator var2 = DataUtils.getListeners(var0.getData()).values().iterator();

      while(var2.hasNext()) {
         Map var3 = (Map)var2.next();
         String var4 = DialogUtils.string(var3, "name");
         if (isVisible(var3)) {
            var1.add(var3);
         }
      }

      return var1;
   }

   public static List getListenersWithStagers(AggressorClient var0) {
      HashSet var1 = new HashSet();
      LinkedList var2 = new LinkedList();
      Iterator var3 = DataUtils.getListeners(var0.getData()).values().iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         String var5 = DialogUtils.string(var4, "name");
         ScListener var6 = new ScListener(var0.getData(), var4);
         if (!var1.contains(var5) && var6.hasStager() && isVisible(var4)) {
            var2.add(var4);
            var1.add(var5);
         }
      }

      Iterator var12 = GlobalDataManager.getGlobalDataManager().getStore("listeners").entrySet().iterator();

      while(true) {
         Map var7;
         DataManager var14;
         do {
            if (!var12.hasNext()) {
               return var2;
            }

            Map.Entry var13 = (Map.Entry)var12.next();
            var14 = (DataManager)var13.getKey();
            var7 = (Map)var13.getValue();
         } while(var14 == var0.getData());

         Iterator var8 = var7.values().iterator();

         while(var8.hasNext()) {
            Map var9 = (Map)var8.next();
            String var10 = DialogUtils.string(var9, "name");
            ScListener var11 = new ScListener(var14, var9);
            if (!var1.contains(var10) && var11.hasStager() && isVisible(var9)) {
               var2.add(var9);
               var1.add(var10);
            }
         }
      }
   }

   public static List getAllListeners(AggressorClient var0) {
      HashSet var1 = new HashSet();
      LinkedList var2 = new LinkedList();
      Iterator var3 = DataUtils.getListeners(var0.getData()).values().iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         String var5 = DialogUtils.string(var4, "name");
         if (!var1.contains(var5) && isVisible(var4)) {
            var2.add(var4);
            var1.add(var5);
         }
      }

      Iterator var11 = GlobalDataManager.getGlobalDataManager().getStore("listeners").entrySet().iterator();

      while(true) {
         DataManager var6;
         Map var7;
         do {
            if (!var11.hasNext()) {
               return var2;
            }

            Map.Entry var12 = (Map.Entry)var11.next();
            var6 = (DataManager)var12.getKey();
            var7 = filterLocal((Map)var12.getValue());
         } while(var6 == var0.getData());

         Iterator var8 = var7.values().iterator();

         while(var8.hasNext()) {
            Map var9 = (Map)var8.next();
            String var10 = DialogUtils.string(var9, "name");
            if (!var1.contains(var10) && isVisible(var9)) {
               var2.add(var9);
               var1.add(var10);
            }
         }
      }
   }

   public static Map filterLocal(Map var0) {
      Iterator var1 = var0.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         Map var3 = (Map)var2.getValue();
         if ("windows/beacon_bind_pipe".equals(var3.get("payload"))) {
            var1.remove();
         } else if ("windows/beacon_bind_tcp".equals(var3.get("payload"))) {
            var1.remove();
         }
      }

      return var0;
   }
}
