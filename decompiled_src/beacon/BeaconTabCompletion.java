package beacon;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import common.BeaconEntry;
import common.CommonUtils;
import common.ListenerUtils;
import common.StringStack;
import console.Console;
import console.GenericTabCompletion;
import cortana.Cortana;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BeaconTabCompletion extends GenericTabCompletion {
   protected AggressorClient client;
   protected String bid;

   public BeaconTabCompletion(String var1, AggressorClient var2, Console var3) {
      super(var3);
      this.client = var2;
      this.bid = var1;
   }

   public static void filterList(List var0, String var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         String var3 = var2.next() + "";
         if (!var3.startsWith(var1)) {
            var2.remove();
         }
      }

   }

   public String transformText(String var1) {
      return var1.replace(" ~", " " + System.getProperty("user.home"));
   }

   public Collection getOptionsFromList(String var1, List var2) {
      LinkedList var3 = new LinkedList();
      StringStack var4 = new StringStack(var1, " ");
      var4.pop();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         var3.add(var4.toString() + " " + var5.next());
      }

      Collections.sort(var3);
      filterList(var3, var1);
      return var3;
   }

   public boolean isFoo(String var1) {
      return var1.matches("elevate .*? .*") || var1.matches("spawn x.. .*") || var1.matches("spawnu \\d+ .*") || var1.matches("inject \\d+ .*");
   }

   public boolean isBar(String var1) {
      return var1.matches("spawnas .*? .*? .*") || var1.matches("jump .*? .*? .*");
   }

   public boolean isLink(String var1) {
      return var1.matches("link .*? .*");
   }

   public boolean isConnect(String var1) {
      return var1.matches("connect .*? .*");
   }

   public Collection getOptions(String var1) {
      Object var2 = DataUtils.getBeaconCommands(this.client.getData()).commands();
      ((List)var2).addAll(this.client.getAliases().commands());
      Collections.sort((List)var2);
      Cortana.filterList((List)var2, var1);
      List var8;
      int var19;
      int var24;
      Iterator var25;
      if (var2 != null && ((List)var2).size() == 0 && var1.matches("inject \\d+ x.. .*")) {
         var8 = ListenerUtils.getListenerNames(this.client);
         if (var8.size() == 0) {
            ((List)var2).add(var1);
         } else {
            var19 = var1.indexOf(" ");
            var24 = var1.indexOf(" ", var19 + 1);
            var2 = new LinkedList();
            var25 = var8.iterator();

            while(var25.hasNext()) {
               ((List)var2).add(var1.substring(0, var1.indexOf(" ", var24 + 1)) + " " + var25.next());
            }
         }

         Collections.sort((LinkedList)var2);
         filterList((LinkedList)var2, var1);
      } else {
         Iterator var15;
         if (var2 != null && ((List)var2).size() == 0 && this.isFoo(var1)) {
            var8 = ListenerUtils.getListenerNames(this.client);
            if (var8.size() == 0) {
               ((List)var2).add(var1);
            } else {
               var19 = var1.indexOf(" ");
               var2 = new LinkedList();
               var15 = var8.iterator();

               while(var15.hasNext()) {
                  ((List)var2).add(var1.substring(0, var1.indexOf(" ", var19 + 1)) + " " + var15.next());
               }
            }

            Collections.sort((LinkedList)var2);
            filterList((LinkedList)var2, var1);
         } else if (var2 != null && ((List)var2).size() == 0 && this.isBar(var1)) {
            var8 = ListenerUtils.getListenerNames(this.client);
            if (var8.size() == 0) {
               ((List)var2).add(var1);
            } else {
               var19 = var1.indexOf(" ");
               var24 = var1.indexOf(" ", var19 + 1);
               var2 = new LinkedList();
               var25 = var8.iterator();

               while(var25.hasNext()) {
                  ((List)var2).add(var1.substring(0, var1.indexOf(" ", var24 + 1)) + " " + var25.next());
               }
            }

            Collections.sort((LinkedList)var2);
            filterList((LinkedList)var2, var1);
         } else {
            Iterator var13;
            if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("spawn ")) {
               var8 = ListenerUtils.getListenerNames(this.client);
               if (var8.size() == 0) {
                  ((List)var2).add(var1);
               } else {
                  var2 = new LinkedList();
                  var13 = var8.iterator();

                  while(var13.hasNext()) {
                     ((List)var2).add(var1.substring(0, var1.indexOf(" ")) + " " + var13.next());
                  }
               }

               Collections.sort((LinkedList)var2);
               filterList((LinkedList)var2, var1);
            } else {
               if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("elevate ")) {
                  var8 = DataUtils.getBeaconExploits(this.client.getData()).exploits();
                  return this.getOptionsFromList(var1, var8);
               }

               if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("runasadmin ")) {
                  var8 = DataUtils.getBeaconElevators(this.client.getData()).elevators();
                  return this.getOptionsFromList(var1, var8);
               }

               if (var2 != null && ((List)var2).size() == 0 && (var1.startsWith("kerberos_ticket_use ") || var1.startsWith("kerberos_ccache_use ") || var1.startsWith("upload ") || var1.startsWith("powershell-import "))) {
                  String var20 = var1.substring(var1.indexOf(" ") + 1);
                  File var14 = new File(var20);
                  if (!var14.exists() || !var14.isDirectory()) {
                     var14 = var14.getParentFile();
                  }

                  var2 = new LinkedList();
                  if (var14 == null) {
                     ((List)var2).add(var1);
                     return (Collection)var2;
                  }

                  File[] var23 = var14.listFiles();

                  for(int var21 = 0; var23 != null && var21 < var23.length; ++var21) {
                     if (var23[var21].isDirectory() || !var1.startsWith("powershell-import") || var23[var21].getName().endsWith(".ps1")) {
                        ((List)var2).add(var1.substring(0, var1.indexOf(" ")) + " " + var23[var21].getAbsolutePath());
                     }
                  }

                  Collections.sort((LinkedList)var2);
                  filterList((LinkedList)var2, var1);
               } else if (var2 == null || ((List)var2).size() != 0 || !var1.matches("execute-assembly .*") && !var1.matches("shspawn x.. .*") && !var1.matches("shinject \\d+ x.. .*") && !var1.matches("dllinject \\d+ .*") && !var1.matches("ssh-key .*? .*? .*")) {
                  if (var2 == null || ((List)var2).size() != 0 || !var1.startsWith("help net ") && !var1.startsWith("? net ")) {
                     LinkedList var9;
                     if (var2 != null && ((List)var2).size() == 0 && (var1.startsWith("help ") || var1.startsWith("? "))) {
                        var9 = new LinkedList();
                        var13 = CommonUtils.getNetCommands().iterator();

                        while(var13.hasNext()) {
                           var9.add("net " + var13.next());
                        }

                        List var22 = DataUtils.getBeaconCommands(this.client.getData()).commands();
                        return this.getOptionsFromList(var1, CommonUtils.combine(var9, var22));
                     }

                     if (var2 != null && ((List)var2).size() == 0 && this.isLink(var1)) {
                        var8 = DataUtils.getNamedPipes(this.client.getData());
                        return this.getOptionsFromList(var1, var8);
                     }

                     if (var2 != null && ((List)var2).size() == 0 && this.isConnect(var1)) {
                        var8 = DataUtils.getTCPPorts(this.client.getData());
                        return this.getOptionsFromList(var1, var8);
                     }

                     if (var2 != null && ((List)var2).size() == 0 && (var1.startsWith("ssh ") || var1.startsWith("ssh-key ") || var1.matches("jump .*? .*") || var1.matches("remote-exec .*? .*"))) {
                        var8 = DataUtils.getTargetNames(this.client.getData());
                        return this.getOptionsFromList(var1, var8);
                     }

                     if (var2 == null || ((List)var2).size() != 0 || !var1.startsWith("link ") && !var1.startsWith("connect ")) {
                        if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("jump ")) {
                           var8 = DataUtils.getBeaconRemoteExploits(this.client.getData()).exploits();
                           return this.getOptionsFromList(var1, var8);
                        }

                        if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("remote-exec ")) {
                           var8 = DataUtils.getBeaconRemoteExecMethods(this.client.getData()).methods();
                           return this.getOptionsFromList(var1, var8);
                        }

                        LinkedList var10;
                        String var16;
                        if (var2 == null || ((List)var2).size() != 0 || !var1.startsWith("powershell ") && !var1.startsWith("powerpick ") && !var1.matches("psinject \\d+ x.. .*")) {
                           if (var2 != null && ((List)var2).size() == 0 && var1.matches("reg query.*? x.. .*")) {
                              return this.getOptionsFromList(var1, CommonUtils.toList("HKCC\\, HKCR\\, HKCU\\, HKLM\\, HKU\\"));
                           }

                           if (var2 != null && ((List)var2).size() == 0 && (var1.startsWith("reg query ") || var1.startsWith("reg queryv "))) {
                              return this.getOptionsFromList(var1, CommonUtils.toList("x64, x86"));
                           }

                           if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("reg ")) {
                              return this.getOptionsFromList(var1, CommonUtils.toList("query, queryv"));
                           }

                           if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("net ")) {
                              return this.getOptionsFromList(var1, CommonUtils.getNetCommands());
                           }

                           if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("note ")) {
                              BeaconEntry var11 = DataUtils.getBeacon(this.client.getData(), this.bid);
                              if (var11 != null) {
                                 var10 = new LinkedList();
                                 var10.add(var11.getNote());
                                 return this.getOptionsFromList(var1, var10);
                              }

                              return this.getOptionsFromList(var1, new LinkedList());
                           }

                           if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("covertvpn ")) {
                              return this.getOptionsFromList(var1, DataUtils.getInterfaceList(this.client.getData()));
                           }

                           if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("desktop ")) {
                              return this.getOptionsFromList(var1, CommonUtils.toList("high, low"));
                           }

                           if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("blockdlls ")) {
                              return this.getOptionsFromList(var1, CommonUtils.toList("start, stop"));
                           }

                           if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("unlink ")) {
                              var9 = new LinkedList();
                              BeaconEntry var12 = DataUtils.getBeacon(this.client.getData(), this.bid);
                              if (var12 != null && var12.getParentId() != null && !var12.getPivotHint().isReverse()) {
                                 BeaconEntry var17 = DataUtils.getBeacon(this.client.getData(), var12.getParentId());
                                 if (var17 != null) {
                                    var9.add(var17.getInternal() + " " + var17.getPid());
                                 }
                              }

                              var15 = DataUtils.getBeaconChildren(this.client.getData(), this.bid).iterator();

                              while(var15.hasNext()) {
                                 BeaconEntry var18 = (BeaconEntry)var15.next();
                                 if (!var18.isSSH() && !var18.getPivotHint().isReverse() && var18.isActive()) {
                                    var9.add(var18.getInternal() + " " + var18.getPid());
                                 }
                              }

                              return this.getOptionsFromList(var1, var9);
                           }

                           if (var2 != null && ((List)var2).size() == 0 && var1.startsWith("mimikatz ")) {
                              var9 = new LinkedList(CommonUtils.toList((Object[])CommonUtils.readResourceAsString("resources/mimikatz.txt").trim().split("\n")));
                              var10 = new LinkedList();
                              var15 = var9.iterator();

                              while(var15.hasNext()) {
                                 var16 = (var15.next() + "").trim();
                                 var10.add(var16);
                                 var10.add("!" + var16);
                                 var10.add("@" + var16);
                              }

                              return this.getOptionsFromList(var1, var10);
                           }

                           return (Collection)var2;
                        }

                        var9 = new LinkedList(DataUtils.getBeaconPowerShellCommands(this.client.getData(), this.bid));
                        var10 = new LinkedList();
                        var15 = var9.iterator();

                        while(var15.hasNext()) {
                           var16 = var15.next() + "";
                           if (var16.length() > 0) {
                              var10.add(var16);
                              var10.add("Get-Help " + var16 + " -full");
                           }
                        }

                        return this.getOptionsFromList(var1, var10);
                     }

                     var8 = DataUtils.getTargetNames(this.client.getData());
                     var8.add("127.0.0.1");
                     return this.getOptionsFromList(var1, var8);
                  }

                  var8 = CommonUtils.getNetCommands();
                  return this.getOptionsFromList(var1, var8);
               } else {
                  StringStack var3 = new StringStack(var1, " ");
                  String var4 = var3.pop();
                  File var5 = new File(var4);
                  if (!var5.exists() || !var5.isDirectory()) {
                     var5 = var5.getParentFile();
                  }

                  var2 = new LinkedList();
                  if (var5 == null) {
                     ((List)var2).add(var1);
                     return (Collection)var2;
                  }

                  File[] var6 = var5.listFiles();

                  for(int var7 = 0; var6 != null && var7 < var6.length; ++var7) {
                     ((List)var2).add(var3.toString() + " " + var6[var7].getAbsolutePath());
                  }

                  Collections.sort((LinkedList)var2);
                  filterList((LinkedList)var2, var1);
               }
            }
         }
      }

      return (Collection)var2;
   }
}
