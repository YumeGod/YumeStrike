package common;

import beacon.Settings;
import dialog.DialogUtils;
import java.util.HashMap;
import java.util.Map;

public class ProxyServer {
   public static final int PROXY_MANUAL = 0;
   public static final int PROXY_DIRECT = 1;
   public static final int PROXY_PRECONFIG = 2;
   public static final int PROXY_MANUAL_CREDS = 4;
   public String username = null;
   public String password = null;
   public String phost = "";
   public int pport = 8080;
   public String ptype = "";
   public int means = 2;

   public boolean hasCredentials() {
      return this.username != null && this.password != null && this.username.length() > 0 && this.password.length() > 0;
   }

   public boolean hasHostAndPort() {
      return this.phost != null && this.pport > 0 && this.phost.length() > 0;
   }

   public String toString() {
      if (this.means == 1) {
         return "*direct*";
      } else if (this.means == 2) {
         return "";
      } else if (this.hasHostAndPort()) {
         StringBuffer var1 = new StringBuffer();
         var1.append(this.ptype);
         var1.append("://");
         if (this.hasCredentials()) {
            var1.append(CommonUtils.URLEncode(this.username));
            var1.append(":");
            var1.append(CommonUtils.URLEncode(this.password));
            var1.append("@");
         }

         var1.append(this.phost);
         var1.append(":");
         var1.append(this.pport);
         return var1.toString();
      } else {
         return "";
      }
   }

   public static ProxyServer resolve(Map var0) {
      ProxyServer var1 = new ProxyServer();
      if (var0.size() == 0) {
         var1.means = 2;
      } else if (DialogUtils.bool(var0, "pdirect")) {
         var1.means = 1;
      } else {
         var1.means = 0;
         var1.ptype = DialogUtils.string(var0, "ptype");
         var1.phost = DialogUtils.string(var0, "phost");
         var1.pport = CommonUtils.toNumber(DialogUtils.string(var0, "pport"), 8080);
         if (var0.containsKey("puser") && var0.containsKey("ppass")) {
            var1.username = DialogUtils.string(var0, "puser");
            var1.password = DialogUtils.string(var0, "ppass");
         }
      }

      return var1;
   }

   public Map toMap() {
      HashMap var1 = new HashMap();
      if (this.means == 1) {
         var1.put("pdirect", "true");
         return var1;
      } else if (this.means == 2) {
         return new HashMap();
      } else {
         if (this.username != null && this.password != null) {
            var1.put("puser", this.username);
            var1.put("ppass", this.password);
         }

         var1.put("phost", this.phost);
         var1.put("pport", this.pport + "");
         var1.put("ptype", this.ptype);
         return var1;
      }
   }

   public static ProxyServer parse(String var0) {
      ProxyServer var1 = new ProxyServer();
      RegexParser var2 = new RegexParser(var0);
      if ("".equals(var0)) {
         var1.means = 2;
         return var1;
      } else if ("*direct*".equals(var0)) {
         var1.means = 1;
         return var1;
      } else if (var2.matches("(.*?)://(.*?):(.*?)@(.*?):(.*?)")) {
         var1.ptype = var2.group(1);
         var1.username = CommonUtils.URLDecode(var2.group(2));
         var1.password = CommonUtils.URLDecode(var2.group(3));
         var1.phost = var2.group(4);
         var1.pport = CommonUtils.toNumber(var2.group(5), 5555);
         var1.means = 0;
         return var1;
      } else if (var2.matches("(.*?)://(.*?):(.*?)")) {
         var1.ptype = var2.group(1);
         var1.phost = var2.group(2);
         var1.pport = CommonUtils.toNumber(var2.group(3), 5555);
         var1.means = 0;
         return var1;
      } else {
         var1.means = 2;
         return var1;
      }
   }

   public void setup(Settings var1) {
      if (this.means == 1) {
         var1.addShort(35, this.means);
      } else if (this.means == 2) {
         var1.addShort(35, this.means);
      } else if (this.hasHostAndPort()) {
         StringBuffer var2 = new StringBuffer();
         if ("socks".equals(this.ptype)) {
            var2.append("socks=");
         }

         if ("http".equals(this.ptype)) {
            var2.append("http://");
         }

         if ("https".equals(this.ptype)) {
            var2.append("https://");
         }

         var2.append(this.phost);
         var2.append(":");
         var2.append(this.pport);
         var1.addString(32, var2.toString(), 128);
         if (this.hasCredentials()) {
            var1.addShort(35, 4);
            var1.addString(33, this.username, 64);
            var1.addString(34, this.password, 64);
         } else {
            var1.addShort(35, 0);
         }
      } else {
         AssertUtils.TestFail("means not known: " + this.means);
      }

   }
}
