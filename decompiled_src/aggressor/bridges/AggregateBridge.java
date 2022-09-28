package aggressor.bridges;

import aggressor.AggressorClient;
import common.CommonUtils;
import common.Keys;
import common.PivotHint;
import common.RegexParser;
import cortana.Cortana;
import data.DataAggregate;
import dialog.DialogUtils;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScalarHash;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class AggregateBridge implements Function, Loadable {
   protected AggressorClient client;

   public AggregateBridge(AggressorClient var1) {
      this.client = var1;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&aggregate", this);
      Cortana.put(var1, "&agConvert", this);
      Cortana.put(var1, "&agArchives", this);
      Iterator var2 = Keys.getDataModelIterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         var3 = var3.substring(0, 1).toUpperCase() + var3.substring(1);
         final String var4 = var3.toLowerCase();
         Cortana.put(var1, "&ag" + var3, new Function() {
            public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
               Map var4x = (Map)BridgeUtilities.getObject(var3);
               return CommonUtils.convertAll((List)var4x.get(var4));
            }
         });
      }

      Cortana.put(var1, "&agServicesForHost", this);
      Cortana.put(var1, "&agSessionsForHost", this);
      Cortana.put(var1, "&agCredentialsForHost", this);
      Cortana.put(var1, "&agWebHitsForEmail", this);
      Cortana.put(var1, "&agWebHitsForToken", this);
      Cortana.put(var1, "&agCountWebHitsByToken", this);
      Cortana.put(var1, "&agSentEmailsForCampaign", this);
      Cortana.put(var1, "&agSentEmailsForEmailAddress", this);
      Cortana.put(var1, "&agApplicationsForEmailAddress", this);
      Cortana.put(var1, "&agFileIndicatorsForSession", this);
      Cortana.put(var1, "&agFileIndicators", this);
      Cortana.put(var1, "&agOtherIndicatorsForSession", this);
      Cortana.put(var1, "&agTasksAndCheckinsForSession", this);
      Cortana.put(var1, "&agServices", this);
      Cortana.put(var1, "&agArchivesByTactic", this);
      Cortana.put(var1, "&agTacticsUsed", this);
      Cortana.put(var1, "&agTokenToEmail", this);
      Cortana.put(var1, "&agEmailAddresses", this);
      Cortana.put(var1, "&agCampaigns", this);
      Cortana.put(var1, "&agSentEmails", this);
      Cortana.put(var1, "&agIndicators", this);
      Cortana.put(var1, "&agInputs", this);
      Cortana.put(var1, "&agInputsForSession", this);
      Cortana.put(var1, "&agTasks", this);
      Cortana.put(var1, "&agWebHits", this);
      Cortana.put(var1, "&agWebHitsWithTokens", this);
      Cortana.put(var1, "&agSessionsById", this);
      Cortana.put(var1, "&agC2Domains", this);
      Cortana.put(var1, "&agC2ForSample", this);
      Cortana.put(var1, "&agPEForSample", this);
      Cortana.put(var1, "&agPENotesForSample", this);
      Cortana.put(var1, "&agCommunicationPathForSession", this);
      Cortana.put(var1, "&agC2Samples", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar extractValue(Map var1, String var2) {
      List var3 = (List)var1.get(var2);
      return CommonUtils.convertAll(var3);
   }

   public static List filterList(List var0, String var1, String var2) {
      LinkedList var3 = new LinkedList();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Map var5 = (Map)var4.next();
         if (var5.containsKey(var1)) {
            String var6 = var5.get(var1).toString();
            if (var2.equals(var6)) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   public static List filterListBySetMember(List var0, String var1, String var2) {
      LinkedList var3 = new LinkedList();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Map var5 = (Map)var4.next();
         if (var5.containsKey(var1)) {
            Set var6 = CommonUtils.toSet(var5.get(var1).toString());
            if (var6.contains(var2)) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   public static List filterList(List var0, String var1, Set var2) {
      LinkedList var3 = new LinkedList();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Map var5 = (Map)var4.next();
         if (var5.containsKey(var1)) {
            String var6 = var5.get(var1).toString();
            if (var2.contains(var6)) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   public static List filterListNot(List var0, String var1, String var2) {
      LinkedList var3 = new LinkedList();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         Map var5 = (Map)var4.next();
         if (var5.containsKey(var1)) {
            String var6 = var5.get(var1).toString();
            if (!var2.equals(var6)) {
               var3.add(var5);
            }
         }
      }

      return var3;
   }

   public static List getValuesWithout(List var0, String var1) {
      LinkedHashSet var2 = new LinkedHashSet();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         HashMap var4 = new HashMap((Map)var3.next());
         var4.remove(var1);
         var2.add(var4);
      }

      return new LinkedList(var2);
   }

   public static List getValue(List var0, String var1) {
      LinkedList var2 = new LinkedList();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         var2.add(var4.get(var1));
      }

      return var2;
   }

   public static List join(List var0, List var1, String var2) {
      return join(var0, var1, var2, var2);
   }

   public static List join(List var0, List var1, String var2, String var3) {
      LinkedList var4 = new LinkedList();
      HashMap var5 = new HashMap();
      Iterator var6 = var0.iterator();

      while(var6.hasNext()) {
         Map var7 = (Map)var6.next();
         String var8 = var7.get(var2) + "";
         var5.put(var8, var7);
      }

      HashMap var10;
      for(Iterator var11 = var1.iterator(); var11.hasNext(); var4.add(var10)) {
         Map var12 = (Map)var11.next();
         String var9 = var12.get(var3) + "";
         var10 = new HashMap();
         var10.putAll(var12);
         if (var5.containsKey(var9)) {
            var10.putAll((Map)var5.get(var9));
         }
      }

      return var4;
   }

   public static Map toMap(List var0, String var1) {
      HashMap var2 = new HashMap();
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         Map var4 = (Map)var3.next();
         String var5 = (String)var4.get(var1);
         var2.put(var5, var4);
      }

      return var2;
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if ("&aggregate".equals(var1)) {
         return SleepUtils.getScalar((Object)DataAggregate.AllModels(this.client));
      } else {
         Map var4;
         if ("&agArchives".equals(var1)) {
            var4 = (Map)BridgeUtilities.getObject(var3);
            return CommonUtils.convertAll((List)var4.get("archives"));
         } else {
            String var5;
            List var18;
            if ("&agArchivesByTactic".equals(var1)) {
               var4 = (Map)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var18 = filterListBySetMember((List)var4.get("archives"), "tactic", var5);
               return CommonUtils.convertAll(var18);
            } else {
               String var11;
               Iterator var28;
               Set var38;
               String var43;
               if ("&agTacticsUsed".equals(var1)) {
                  var4 = (Map)BridgeUtilities.getObject(var3);
                  HashSet var57 = new HashSet();
                  var18 = getValue((List)var4.get("archives"), "tactic");
                  var28 = var18.iterator();

                  while(true) {
                     do {
                        do {
                           if (!var28.hasNext()) {
                              return CommonUtils.convertAll(var57);
                           }

                           var43 = (String)var28.next();
                        } while(var43 == null);
                     } while("".equals(var43));

                     var38 = CommonUtils.toSet(var43);
                     Iterator var49 = var38.iterator();

                     while(var49.hasNext()) {
                        var11 = (String)var49.next();
                        if (var11.startsWith("T")) {
                           var57.add(var11);
                        }
                     }
                  }
               } else if ("&agConvert".equals(var1)) {
                  Object var53 = BridgeUtilities.getObject(var3);
                  return CommonUtils.convertAll(var53);
               } else {
                  Map var31;
                  if ("&agTokenToEmail".equals(var1)) {
                     var4 = (Map)BridgeUtilities.getObject(var3);
                     var5 = BridgeUtilities.getString(var3, "");
                     var18 = (List)var4.get("tokens");
                     var28 = var18.iterator();

                     do {
                        if (!var28.hasNext()) {
                           return SleepUtils.getEmptyScalar();
                        }

                        var31 = (Map)var28.next();
                     } while(!var5.equals(var31.get("token")));

                     return SleepUtils.getScalar((String)var31.get("email"));
                  } else {
                     Iterator var9;
                     String var10;
                     List var17;
                     Iterator var23;
                     Map var24;
                     if ("&agCampaigns".equals(var1)) {
                        var4 = (Map)BridgeUtilities.getObject(var3);
                        var17 = (List)var4.get("archives");
                        HashMap var44 = new HashMap();
                        HashMap var42 = new HashMap();
                        var23 = var17.iterator();

                        while(var23.hasNext()) {
                           var24 = (Map)var23.next();
                           if ("sendmail_start".equals(var24.get("type"))) {
                              var10 = var24.get("cid").toString();
                              var44.put(var10, var24);
                           } else if ("sendmail_post".equals(var24.get("type"))) {
                              var10 = DialogUtils.string(var24, "cid");
                              var11 = DialogUtils.string(var24, "status");
                              if ("SUCCESS".equals(var11)) {
                                 CommonUtils.increment(var42, var10);
                              }
                           }
                        }

                        var9 = var44.entrySet().iterator();

                        while(var9.hasNext()) {
                           Map.Entry var48 = (Map.Entry)var9.next();
                           var11 = (String)var48.getKey();
                           if (CommonUtils.count(var42, var11) == 0) {
                              var9.remove();
                           }
                        }

                        return CommonUtils.convertAll(var44);
                     } else if ("&agC2Samples".equals(var1)) {
                        var4 = (Map)BridgeUtilities.getObject(var3);
                        var17 = (List)var4.get("c2samples");
                        return CommonUtils.convertAll(var17);
                     } else {
                        LinkedList var21;
                        HashSet var25;
                        if ("&agC2Domains".equals(var1)) {
                           var4 = (Map)BridgeUtilities.getObject(var3);
                           var17 = getValue((List)var4.get("c2info"), "domains");
                           var25 = new HashSet();
                           var28 = var17.iterator();

                           while(var28.hasNext()) {
                              var43 = (String)var28.next();
                              var25.addAll(CommonUtils.toSet(var43));
                           }

                           var21 = new LinkedList(var25);
                           Collections.sort(var21);
                           return CommonUtils.convertAll(var21);
                        } else {
                           Map var51;
                           if ("&agPEForSample".equals(var1)) {
                              var4 = SleepUtils.getMapFromHash((ScalarHash)BridgeUtilities.getObject(var3));
                              var51 = (Map)var4.get("pe");
                              LinkedHashMap var37 = new LinkedHashMap();
                              var37.put("Checksum", var51.get("Checksum") + "");
                              SimpleDateFormat var33 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                              Date var36 = (Date)var51.get("Compilation Timestamp");
                              var37.put("Compilation Timestamp", var33.format(var36));
                              var37.put("Entry Point", var51.get("Entry Point") + "");
                              if (var51.containsKey("Name")) {
                                 var37.put("Name", var51.get("Name"));
                              }

                              long var41 = CommonUtils.toLongNumber(var51.get("Size") + "", 0L);
                              var11 = CommonUtils.formatSize(var41) + " (" + var41 + " bytes)";
                              var37.put("Size", var11);
                              var37.put("Target Machine", var51.get("Target Machine"));
                              Scalar var50 = SleepUtils.getOrderedHashScalar();
                              Iterator var54 = var37.entrySet().iterator();

                              while(var54.hasNext()) {
                                 Map.Entry var55 = (Map.Entry)var54.next();
                                 Scalar var56 = SleepUtils.getScalar((String)var55.getKey());
                                 Scalar var16 = var50.getHash().getAt(var56);
                                 var16.setValue(SleepUtils.getScalar(var55.getValue() + ""));
                              }

                              return var50;
                           } else if ("&agPENotesForSample".equals(var1)) {
                              var4 = SleepUtils.getMapFromHash((ScalarHash)BridgeUtilities.getObject(var3));
                              var51 = (Map)var4.get("pe");
                              new LinkedHashMap();
                              return var51.containsKey("Notes") ? SleepUtils.getScalar((String)var51.get("Notes")) : SleepUtils.getEmptyScalar();
                           } else {
                              LinkedList var6;
                              String var46;
                              HashMap var52;
                              if ("&agC2ForSample".equals(var1)) {
                                 var4 = SleepUtils.getMapFromHash((ScalarHash)BridgeUtilities.getObject(var3));
                                 var17 = getValuesWithout((List)var4.get("callbacks"), "bid");
                                 var6 = new LinkedList();
                                 var28 = var17.iterator();

                                 while(var28.hasNext()) {
                                    var31 = (Map)var28.next();
                                    var38 = CommonUtils.toSet((String)var31.get("domains"));
                                    var10 = DialogUtils.string(var31, "proto");
                                    Iterator var45 = var38.iterator();

                                    while(var45.hasNext()) {
                                       var46 = (String)var45.next();
                                       var52 = new HashMap();
                                       var52.put("Host", var46);
                                       var52.put("Port", var31.get("port"));
                                       var52.put("Protocols", var10);
                                       var6.add(var52);
                                    }
                                 }

                                 return CommonUtils.convertAll(var6);
                              } else {
                                 HashMap var27;
                                 Map var30;
                                 if ("&agCommunicationPathForSession".equals(var1)) {
                                    var4 = (Map)BridgeUtilities.getObject(var3);
                                    var5 = BridgeUtilities.getString(var3, "");
                                    var30 = toMap((List)var4.get("sessions"), "id");
                                    Map var29 = toMap((List)var4.get("c2info"), "bid");
                                    var21 = new LinkedList();
                                    var24 = (Map)var30.get(var5);

                                    while(var24 != null) {
                                       if (!"".equals(var24.get("pbid"))) {
                                          var27 = new HashMap();
                                          if ("beacon".equals(var24.get("session"))) {
                                             PivotHint var40 = new PivotHint(var24.get("phint") + "");
                                             var27.put("protocol", var40.getProtocol());
                                             var27.put("port", var40.getPort());
                                          } else {
                                             var27.put("protocol", "SSH");
                                             var27.put("port", var24.get("port"));
                                          }

                                          var5 = (String)var24.get("pbid");
                                          var24 = (Map)var30.get(var5);
                                          if (var24 != null) {
                                             var24.remove("port");
                                             var24.remove("protocol");
                                             var27.put("hosts", var24.get("computer"));
                                             var27.putAll(var24);
                                             var21.add(var27);
                                          }
                                       } else {
                                          var24 = null;
                                          Map var39 = (Map)var29.get(var5);
                                          if (var39 != null) {
                                             var11 = DialogUtils.string(var39, "domains");
                                             int var47 = DialogUtils.number(var39, "port");
                                             var52 = new HashMap();
                                             var52.put("protocol", DialogUtils.string(var39, "proto"));
                                             var52.put("port", var47);
                                             var52.put("hosts", var11);
                                             var21.add(var52);
                                          }
                                       }
                                    }

                                    CommonUtils.print_info("\t" + var21);
                                    return CommonUtils.convertAll(var21);
                                 } else if ("&agSessionsById".equals(var1)) {
                                    var4 = (Map)BridgeUtilities.getObject(var3);
                                    var17 = (List)var4.get("sessions");
                                    var30 = toMap((List)var4.get("sessions"), "id");
                                    return CommonUtils.convertAll(var30);
                                 } else {
                                    LinkedList var34;
                                    if ("&agEmailAddresses".equals(var1)) {
                                       var4 = (Map)BridgeUtilities.getObject(var3);
                                       var34 = new LinkedList(new HashSet(getValue((List)var4.get("tokens"), "email")));
                                       return CommonUtils.convertAll(var34);
                                    } else if ("&agServicesForHost".equals(var1)) {
                                       var4 = (Map)BridgeUtilities.getObject(var3);
                                       var5 = BridgeUtilities.getString(var3, "");
                                       var18 = filterList((List)var4.get("services"), "address", var5);
                                       return CommonUtils.convertAll(var18);
                                    } else {
                                       String var26;
                                       if ("&agServices".equals(var1)) {
                                          var4 = (Map)BridgeUtilities.getObject(var3);
                                          var34 = new LinkedList((List)var4.get("services"));
                                          var25 = new HashSet(getValue((List)var4.get("targets"), "address"));
                                          var28 = var34.iterator();

                                          while(var28.hasNext()) {
                                             var31 = (Map)var28.next();
                                             var26 = (String)var31.get("address");
                                             if (!var25.contains(var26)) {
                                                var28.remove();
                                             }
                                          }

                                          return CommonUtils.convertAll(var34);
                                       } else if ("&agSessionsForHost".equals(var1)) {
                                          var4 = (Map)BridgeUtilities.getObject(var3);
                                          var5 = BridgeUtilities.getString(var3, "");
                                          var18 = filterList((List)var4.get("sessions"), "internal", var5);
                                          return CommonUtils.convertAll(var18);
                                       } else if ("&agCredentialsForHost".equals(var1)) {
                                          var4 = (Map)BridgeUtilities.getObject(var3);
                                          var5 = BridgeUtilities.getString(var3, "");
                                          var18 = filterList((List)var4.get("credentials"), "host", var5);
                                          return CommonUtils.convertAll(var18);
                                       } else {
                                          List var7;
                                          if ("&agWebHitsForToken".equals(var1)) {
                                             var4 = (Map)BridgeUtilities.getObject(var3);
                                             var5 = BridgeUtilities.getString(var3, "");
                                             var18 = filterList((List)var4.get("archives"), "type", "webhit");
                                             var7 = filterList(var18, "token", var5);
                                             return CommonUtils.convertAll(var7);
                                          } else {
                                             List var8;
                                             if ("&agSentEmailsForCampaign".equals(var1)) {
                                                var4 = (Map)BridgeUtilities.getObject(var3);
                                                var5 = BridgeUtilities.getString(var3, "");
                                                var18 = filterList((List)var4.get("archives"), "type", "sendmail_post");
                                                var7 = filterList(var18, "cid", var5);
                                                var8 = (List)var4.get("tokens");
                                                return CommonUtils.convertAll(join(var8, var7, "token"));
                                             } else if ("&agSentEmailsForEmailAddress".equals(var1)) {
                                                var4 = (Map)BridgeUtilities.getObject(var3);
                                                var5 = BridgeUtilities.getString(var3, "");
                                                var18 = filterList((List)var4.get("archives"), "type", "sendmail_post");
                                                var7 = join((List)var4.get("tokens"), var18, "token");
                                                var8 = filterList(var7, "email", var5);
                                                return CommonUtils.convertAll(var8);
                                             } else if ("&agApplicationsForEmailAddress".equals(var1)) {
                                                var4 = (Map)BridgeUtilities.getObject(var3);
                                                var5 = BridgeUtilities.getString(var3, "");
                                                var18 = (List)var4.get("applications");
                                                var7 = join((List)var4.get("tokens"), var18, "token", "id");
                                                var8 = filterList(var7, "email", var5);
                                                return CommonUtils.convertAll(var8);
                                             } else if ("&agCountWebHitsByToken".equals(var1)) {
                                                var4 = (Map)BridgeUtilities.getObject(var3);
                                                HashMap var22 = new HashMap();
                                                var18 = filterList((List)var4.get("archives"), "type", "webhit");
                                                var7 = getValue((List)var4.get("tokens"), "token");
                                                var23 = var7.iterator();

                                                while(var23.hasNext()) {
                                                   var26 = (String)var23.next();
                                                   int var32 = filterList(var18, "token", var26).size();
                                                   if (var32 > 0) {
                                                      var22.put(var26, var32);
                                                   }
                                                }

                                                return CommonUtils.convertAll(var22);
                                             } else if (!"&agWebHitsForEmail".equals(var1)) {
                                                if ("&agSentEmails".equals(var1)) {
                                                   var4 = (Map)BridgeUtilities.getObject(var3);
                                                   var17 = filterList((List)var4.get("archives"), "type", "sendmail_post");
                                                   return CommonUtils.convertAll(var17);
                                                } else if ("&agIndicators".equals(var1)) {
                                                   var4 = (Map)BridgeUtilities.getObject(var3);
                                                   var17 = filterList((List)var4.get("archives"), "type", "indicator");
                                                   return CommonUtils.convertAll(var17);
                                                } else {
                                                   String var13;
                                                   String var14;
                                                   if ("&agFileIndicators".equals(var1)) {
                                                      var4 = (Map)BridgeUtilities.getObject(var3);
                                                      var5 = BridgeUtilities.getString(var3, "");
                                                      var18 = filterList((List)var4.get("archives"), "type", "indicator");
                                                      HashSet var20 = new HashSet();
                                                      var23 = var18.iterator();

                                                      while(var23.hasNext()) {
                                                         var24 = (Map)var23.next();
                                                         var10 = (String)var24.get("data");
                                                         RegexParser var35 = new RegexParser(var10);
                                                         if (var35.matches("file: (.*?) (.*?) bytes (.*)")) {
                                                            var46 = var35.group(1);
                                                            var13 = var35.group(2);
                                                            var14 = var35.group(3);
                                                            if (var20.contains(var46)) {
                                                               var23.remove();
                                                            } else {
                                                               var24.put("hash", var46);
                                                               var24.put("name", var14);
                                                               var24.put("size", var13);
                                                               var20.add(var46);
                                                            }
                                                         } else {
                                                            var23.remove();
                                                         }
                                                      }

                                                      return CommonUtils.convertAll(var18);
                                                   } else {
                                                      RegexParser var12;
                                                      if ("&agFileIndicatorsForSession".equals(var1)) {
                                                         var4 = (Map)BridgeUtilities.getObject(var3);
                                                         var5 = BridgeUtilities.getString(var3, "");
                                                         var18 = filterList((List)var4.get("archives"), "type", "indicator");
                                                         var7 = filterList(var18, "bid", var5);
                                                         var21 = new LinkedList();
                                                         var9 = var7.iterator();

                                                         while(var9.hasNext()) {
                                                            var27 = new HashMap((Map)var9.next());
                                                            var11 = (String)var27.get("data");
                                                            var12 = new RegexParser(var11);
                                                            if (var12.matches("file: (.*?) (.*?) bytes (.*)")) {
                                                               var13 = var12.group(1);
                                                               var14 = var12.group(2);
                                                               String var15 = var12.group(3);
                                                               var27.put("hash", var13);
                                                               var27.put("name", var15);
                                                               var27.put("size", var14);
                                                               var21.add(var27);
                                                            }
                                                         }

                                                         return CommonUtils.convertAll(var21);
                                                      } else if ("&agOtherIndicatorsForSession".equals(var1)) {
                                                         var4 = (Map)BridgeUtilities.getObject(var3);
                                                         var5 = BridgeUtilities.getString(var3, "");
                                                         var18 = filterList((List)var4.get("archives"), "type", "indicator");
                                                         var7 = filterList(var18, "bid", var5);
                                                         var21 = new LinkedList();
                                                         var9 = var7.iterator();

                                                         while(var9.hasNext()) {
                                                            var27 = new HashMap((Map)var9.next());
                                                            var11 = (String)var27.get("data");
                                                            var12 = new RegexParser(var11);
                                                            if (var12.matches("service: (.*?) (.*)")) {
                                                               var13 = var12.group(1);
                                                               var14 = var12.group(2);
                                                               var27.put("target", var13);
                                                               var27.put("name", var14);
                                                               var27.put("type", "service");
                                                               var21.add(var27);
                                                            }
                                                         }

                                                         return CommonUtils.convertAll(var21);
                                                      } else if ("&agTasksAndCheckinsForSession".equals(var1)) {
                                                         var4 = (Map)BridgeUtilities.getObject(var3);
                                                         var5 = BridgeUtilities.getString(var3, "");
                                                         Set var19 = CommonUtils.toSet("task, checkin, output");
                                                         var7 = filterList((List)var4.get("archives"), "type", var19);
                                                         var8 = filterList(var7, "bid", var5);
                                                         return CommonUtils.convertAll(var8);
                                                      } else if ("&agInputs".equals(var1)) {
                                                         var4 = (Map)BridgeUtilities.getObject(var3);
                                                         var17 = filterList((List)var4.get("archives"), "type", "input");
                                                         return CommonUtils.convertAll(var17);
                                                      } else if ("&agInputsForSession".equals(var1)) {
                                                         var4 = (Map)BridgeUtilities.getObject(var3);
                                                         var5 = BridgeUtilities.getString(var3, "");
                                                         var18 = filterList((List)var4.get("archives"), "type", "input");
                                                         var7 = filterList(var18, "bid", var5);
                                                         return CommonUtils.convertAll(var7);
                                                      } else if ("&agTasks".equals(var1)) {
                                                         var4 = (Map)BridgeUtilities.getObject(var3);
                                                         var17 = filterList((List)var4.get("archives"), "type", "task");
                                                         return CommonUtils.convertAll(var17);
                                                      } else if ("&agWebHits".equals(var1)) {
                                                         var4 = (Map)BridgeUtilities.getObject(var3);
                                                         var17 = filterList((List)var4.get("archives"), "type", "webhit");
                                                         return CommonUtils.convertAll(var17);
                                                      } else if ("&agWebHitsWithTokens".equals(var1)) {
                                                         var4 = (Map)BridgeUtilities.getObject(var3);
                                                         var17 = filterList((List)var4.get("archives"), "type", "webhit");
                                                         var17 = filterListNot(var17, "token", "");
                                                         return CommonUtils.convertAll(var17);
                                                      } else {
                                                         return SleepUtils.getEmptyScalar();
                                                      }
                                                   }
                                                }
                                             } else {
                                                var4 = (Map)BridgeUtilities.getObject(var3);
                                                var5 = BridgeUtilities.getString(var3, "");
                                                var6 = new LinkedList();
                                                var7 = filterList((List)var4.get("archives"), "type", "webhit");
                                                var8 = getValue(filterList((List)var4.get("tokens"), "email", var5), "token");
                                                var9 = var8.iterator();

                                                while(var9.hasNext()) {
                                                   var10 = (String)var9.next();
                                                   var6.addAll(filterList(var7, "token", var10));
                                                }

                                                return CommonUtils.convertAll(var6);
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
