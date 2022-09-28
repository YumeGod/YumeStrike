package parser;

import common.CommonUtils;
import common.RegexParser;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import server.Resources;

public class ScanResults extends Parser {
   public ScanResults(Resources var1) {
      super(var1);
   }

   public boolean check(String var1, int var2) {
      return var2 == 25;
   }

   public void addHost(Map var1, String var2) {
      var2 = CommonUtils.trim(var2);
      if (!var1.containsKey(var2)) {
         HashMap var3 = new HashMap();
         var3.put("address", var2);
         var1.put(var2, var3);
      }
   }

   public Map service(String var1, String var2) {
      var1 = CommonUtils.trim(var1);
      HashMap var3 = new HashMap();
      var3.put("address", var1);
      var3.put("port", var2);
      return var3;
   }

   public Map service(String var1, String var2, String var3) {
      var1 = CommonUtils.trim(var1);
      HashMap var4 = new HashMap();
      var4.put("address", var1);
      var4.put("port", var2);
      var4.put("banner", var3);
      return var4;
   }

   public void parse(String var1, String var2) throws Exception {
      String[] var3 = var1.split("\n");
      HashMap var4 = new HashMap();
      LinkedList var5 = new LinkedList();

      String var8;
      String var9;
      for(int var6 = 0; var6 < var3.length; ++var6) {
         RegexParser var7 = new RegexParser(var3[var6]);
         String var10;
         if (var7.matches("(.*?):(\\d+) \\((.*?)\\)")) {
            var8 = var7.group(1);
            var9 = var7.group(2);
            var10 = var7.group(3);
            this.addHost(var4, var8);
            var5.add(this.service(var8, var9, var10));
         } else if (var7.matches("(.*?):(\\d+)")) {
            var8 = var7.group(1);
            var9 = var7.group(2);
            this.addHost(var4, var8);
            var5.add(this.service(var8, var9));
         }

         if (var7.matches("(.*?):445 \\(platform: (\\d+) version: (.*?) name: (.*?) domain: (.*?)\\)")) {
            var8 = var7.group(1);
            var9 = var7.group(2);
            var10 = var7.group(3);
            String var11 = var7.group(4);
            String var12 = var7.group(5);
            Map var13 = (Map)var4.get(var8);
            if (!"4.9".equals(var10)) {
               var13.put("os", "Windows");
               var13.put("version", var10);
               var13.put("name", var11);
            }
         } else if (var7.matches("(.*?):22 \\((.*?)\\)")) {
            var8 = var7.group(1);
            var9 = var7.group(2).toLowerCase();
            Map var18;
            if (var9.indexOf("debian") > -1) {
               var18 = (Map)var4.get(var8);
               var18.put("os", "Linux");
            } else if (var9.indexOf("ubuntu") > -1) {
               var18 = (Map)var4.get(var8);
               var18.put("os", "Linux");
            } else if (var9.indexOf("cisco") > -1) {
               var18 = (Map)var4.get(var8);
               var18.put("os", "Cisco IOS");
            } else if (var9.indexOf("freebsd") > -1) {
               var18 = (Map)var4.get(var8);
               var18.put("os", "FreeBSD");
            } else if (var9.indexOf("openbsd") > -1) {
               var18 = (Map)var4.get(var8);
               var18.put("os", "OpenBSD");
            }
         }
      }

      Iterator var14 = var4.values().iterator();

      while(var14.hasNext()) {
         Map var15 = (Map)var14.next();
         var8 = CommonUtils.TargetKey(var15);
         this.resources.call("targets.update", CommonUtils.args(var8, var15));
      }

      Iterator var16 = var5.iterator();

      while(var16.hasNext()) {
         Map var17 = (Map)var16.next();
         var9 = CommonUtils.ServiceKey(var17);
         this.resources.call("services.update", CommonUtils.args(var9, var17));
      }

      this.resources.call("services.push");
      this.resources.call("targets.push");
   }
}
