package report;

import common.CommonUtils;
import common.RegexParser;
import cortana.Cortana;
import dialog.DialogUtils;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.engine.Block;
import sleep.interfaces.Environment;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ReportBridge implements Function, Loadable, Environment {
   protected Map descriptions = new HashMap();
   protected LinkedHashMap reports = new LinkedHashMap();

   public List reportTitles() {
      return new LinkedList(this.reports.keySet());
   }

   public String describe(String var1) {
      return (String)this.descriptions.get(var1);
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.putenv(var1, "li", this);
      Cortana.putenv(var1, "nobreak", this);
      Cortana.putenv(var1, "output", this);
      Cortana.putenv(var1, "page", this);
      Cortana.putenv(var1, "report", this);
      Cortana.putenv(var1, "ul", this);
      Cortana.put(var1, "&block", this);
      Cortana.put(var1, "&bookmark", this);
      Cortana.put(var1, "&describe", this);
      Cortana.put(var1, "&formatTime", this);
      Cortana.put(var1, "&b", this);
      Cortana.put(var1, "&br", this);
      Cortana.put(var1, "&color", this);
      Cortana.put(var1, "&color2", this);
      Cortana.put(var1, "&h1", this);
      Cortana.put(var1, "&h2", this);
      Cortana.put(var1, "&h2_img", this);
      Cortana.put(var1, "&h3", this);
      Cortana.put(var1, "&h4", this);
      Cortana.put(var1, "&host_image", this);
      Cortana.put(var1, "&img", this);
      Cortana.put(var1, "&landscape", this);
      Cortana.put(var1, "&layout", this);
      Cortana.put(var1, "&li", this);
      Cortana.put(var1, "&link", this);
      Cortana.put(var1, "&kvtable", this);
      Cortana.put(var1, "&nobreak", this);
      Cortana.put(var1, "&output", this);
      Cortana.put(var1, "&p", this);
      Cortana.put(var1, "&p_formatted", this);
      Cortana.put(var1, "&text", this);
      Cortana.put(var1, "&table", this);
      Cortana.put(var1, "&ts", this);
      Cortana.put(var1, "&ul", this);
      Cortana.put(var1, "&list_unordered", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Document buildReport(String var1, String var2, Stack var3) {
      Document var4 = new Document(var2, 0);
      SleepClosure var5 = (SleepClosure)this.reports.get(var1);
      var5.getOwner().getMetadata().put("document", var4);
      SleepUtils.runCode((SleepClosure)var5, var1, (ScriptInstance)null, var3);
      var5.getOwner().getMetadata().put("document", (Object)null);
      var5.getOwner().getMetadata().put("document_stack", (Object)null);
      return var4;
   }

   public Document getCurrentDocument(ScriptInstance var1) {
      Document var2 = (Document)var1.getMetadata().get("document");
      if (var2 == null) {
         throw new RuntimeException("this function must be run within the context of a report!");
      } else {
         return var2;
      }
   }

   public Stack getContentStack(ScriptInstance var1) {
      Stack var2 = (Stack)var1.getMetadata().get("document_stack");
      if (var2 == null) {
         var2 = new Stack();
         var1.getMetadata().put("document_stack", var2);
      }

      return var2;
   }

   public Content getContent(ScriptInstance var1) {
      return (Content)this.getContentStack(var1).peek();
   }

   protected void eval(Content var1, ScriptInstance var2, Block var3) {
      this.getContentStack(var2).push(var1);
      SleepUtils.runCode(var2, var3);
      this.getContentStack(var2).pop();
   }

   protected void eval(Content var1, SleepClosure var2) {
      this.getContentStack(var2.getOwner()).push(var1);
      SleepUtils.runCode((SleepClosure)var2, "", (ScriptInstance)null, new Stack());
      this.getContentStack(var2.getOwner()).pop();
   }

   public void bindFunction(ScriptInstance var1, String var2, String var3, Block var4) {
      if ("report".equals(var2)) {
         this.reports.put(var3, new SleepClosure(var1, var4));
      } else if ("page".equals(var2)) {
         boolean var5 = false;
         byte var6;
         if (var3.equals("rest")) {
            var6 = 1;
         } else if (var3.equals("first")) {
            var6 = 0;
         } else if (var3.equals("first-center")) {
            var6 = 2;
         } else {
            if (!var3.equals("single")) {
               throw new RuntimeException("invalid page type '" + var3 + "'");
            }

            var6 = 3;
         }

         this.eval(this.getCurrentDocument(var1).addPage(var6), var1, var4);
      }

   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (!"sajkld".equals(var1)) {
         String var4;
         String var5;
         if ("&bookmark".equals(var1)) {
            if (var3.size() == 2) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               this.getCurrentDocument(var2).getBookmarks().bookmark(var4, var5);
            } else {
               var4 = BridgeUtilities.getString(var3, "");
               this.getCurrentDocument(var2).getBookmarks().bookmark(var4);
            }
         } else if ("&br".equals(var1)) {
            this.getContent(var2).br();
         } else if ("&describe".equals(var1)) {
            var4 = BridgeUtilities.getString(var3, "");
            var5 = BridgeUtilities.getString(var3, "");
            this.descriptions.put(var4, var5);
         } else {
            if ("&formatTime".equals(var1)) {
               long var28 = BridgeUtilities.getLong(var3);
               return SleepUtils.getScalar(CommonUtils.formatTime(var28));
            }

            String var6;
            if ("&h1".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, var4);
               var6 = BridgeUtilities.getString(var3, "left");
               this.getContent(var2).h1(var4, var5, var6);
            } else if ("&h2".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, var4);
               this.getContent(var2).h2(var4, var5);
            } else if ("&h2_img".equals(var1)) {
               BufferedImage var13 = (BufferedImage)BridgeUtilities.getObject(var3);
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, var5);
               this.getContent(var2).h2_img(var13, var5, var6);
            } else if ("&h3".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               this.getContent(var2).h3(var4);
            } else if ("&h4".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               this.getContent(var2).h4(var4, "left");
            } else if ("&b".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               this.getContent(var2).b(var4);
            } else if ("&color".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               this.getContent(var2).color(var4, var5);
            } else if ("&color2".equals(var1)) {
               var4 = BridgeUtilities.getString(var3, "");
               var5 = BridgeUtilities.getString(var3, "");
               var6 = BridgeUtilities.getString(var3, "");
               this.getContent(var2).color2(var4, var5, var6);
            } else {
               if ("&host_image".equals(var1)) {
                  var4 = BridgeUtilities.getString(var3, "");
                  double var25 = BridgeUtilities.getDouble(var3, 0.0);
                  boolean var18 = !SleepUtils.isEmptyScalar(BridgeUtilities.getScalar(var3));
                  return SleepUtils.getScalar((Object)DialogUtils.TargetVisualizationMedium(var4, var25, var18, false));
               }

               if ("&img".equals(var1)) {
                  var4 = BridgeUtilities.getString(var3, "");
                  var5 = BridgeUtilities.getString(var3, "");
                  this.getContent(var2).img(var4, var5);
               } else if ("&kvtable".equals(var1)) {
                  Scalar var19 = (Scalar)var3.pop();
                  LinkedHashMap var17 = new LinkedHashMap();
                  Iterator var14 = var19.getHash().getData().entrySet().iterator();

                  while(var14.hasNext()) {
                     Map.Entry var7 = (Map.Entry)var14.next();
                     var17.put(var7.getKey().toString(), var7.getValue() != null ? var7.getValue().toString() : "");
                  }

                  this.getContent(var2).kvtable(var17);
               } else if ("&landscape".equals(var1)) {
                  this.getCurrentDocument(var2).setOrientation(1);
               } else {
                  SleepClosure var21;
                  if ("&li".equals(var1)) {
                     var21 = BridgeUtilities.getFunction(var3, var2);
                     this.eval(this.getContent(var2).li(), var21);
                  } else if ("&nobreak".equals(var1)) {
                     var21 = BridgeUtilities.getFunction(var3, var2);
                     this.eval(this.getContent(var2).nobreak(), var21);
                  } else if ("&output".equals(var1)) {
                     var21 = BridgeUtilities.getFunction(var3, var2);
                     this.eval(this.getContent(var2).output("800"), var21);
                  } else if ("&block".equals(var1)) {
                     var21 = BridgeUtilities.getFunction(var3, var2);
                     var5 = BridgeUtilities.getString(var3, "left");
                     this.eval(this.getContent(var2).block(var5), var21);
                  } else if ("&p".equals(var1)) {
                     var4 = BridgeUtilities.getString(var3, "");
                     var5 = BridgeUtilities.getString(var3, "left");
                     this.getContent(var2).p(var4, var5);
                  } else {
                     Content var11;
                     Iterator var16;
                     List var22;
                     if ("&p_formatted".equals(var1)) {
                        var4 = BridgeUtilities.getString(var3, "");
                        var4 = CommonUtils.strrep(var4, "\n\n*", "\n*");
                        var22 = CommonUtils.toList((Object[])var4.split("\n"));
                        LinkedList var15 = new LinkedList();
                        var16 = var22.iterator();

                        while(true) {
                           while(var16.hasNext()) {
                              String var8 = (String)var16.next();
                              var8 = var8.trim();
                              if (!var8.equals("") && var8.charAt(0) == '*' && var8.length() > 1) {
                                 var15.add(var8.substring(1));
                              } else {
                                 if (var15.size() > 0) {
                                    this.getContent(var2).list_formatted(var15);
                                    var15 = new LinkedList();
                                    if ("".equals(var8)) {
                                       continue;
                                    }
                                 }

                                 RegexParser var9 = new RegexParser(var8);
                                 if (var9.matches("===(.*?)===")) {
                                    this.getContent(var2).h4(var9.group(1), "left");
                                    if (var16.hasNext()) {
                                       var8 = (String)var16.next();
                                       if (!"".equals(var8)) {
                                          this.getContent(var2).p(var8, "left");
                                       }
                                    }
                                 } else if ("".equals(var8)) {
                                    this.getContent(var2).br();
                                 } else {
                                    RegexParser var10 = new RegexParser(var8.trim());
                                    if (var10.matches("'''(.*?)'''(.*?)")) {
                                       var11 = this.getContent(var2).block("left");
                                       var11.b(var10.group(1));
                                       var11.text(var10.group(2));
                                    } else {
                                       this.getContent(var2).p(var8, "left");
                                    }
                                 }
                              }
                           }

                           if (var15.size() > 0) {
                              this.getContent(var2).list(var15);
                           }
                           break;
                        }
                     } else if ("&text".equals(var1)) {
                        var4 = BridgeUtilities.getString(var3, "");
                        this.getContent(var2).text(var4);
                     } else {
                        List var27;
                        if (!"&table".equals(var1) && !"&layout".equals(var1)) {
                           if ("&ts".equals(var1)) {
                              var4 = BridgeUtilities.getString(var3, "");
                              this.getContent(var2).ts();
                           } else if ("&ul".equals(var1)) {
                              var21 = BridgeUtilities.getFunction(var3, var2);
                              this.eval(this.getContent(var2).ul(), var21);
                           } else if ("&list_unordered".equals(var1)) {
                              var27 = SleepUtils.getListFromArray((Scalar)var3.pop());
                              this.getContent(var2).list(var27);
                           } else if ("&link".equals(var1)) {
                              var4 = BridgeUtilities.getString(var3, "");
                              var5 = BridgeUtilities.getString(var3, "");
                              this.getContent(var2).link_bullet(var4, var5);
                           }
                        } else {
                           var27 = SleepUtils.getListFromArray((Scalar)var3.pop());
                           var22 = SleepUtils.getListFromArray((Scalar)var3.pop());
                           List var20 = SleepUtils.getListFromArray((Scalar)var3.pop());
                           var16 = var20.iterator();

                           while(var16.hasNext()) {
                              Iterator var23 = ((Map)var16.next()).entrySet().iterator();

                              while(var23.hasNext()) {
                                 Map.Entry var24 = (Map.Entry)var23.next();
                                 if (var24.getValue() instanceof SleepClosure) {
                                    SleepClosure var26 = (SleepClosure)var24.getValue();
                                    var11 = this.getContent(var2).string();
                                    this.eval(var11, var26);
                                    StringBuffer var12 = new StringBuffer();
                                    var11.publish(var12);
                                    var24.setValue(var12.toString());
                                 } else {
                                    var24.setValue(Content.fixText(var24.getValue() != null ? var24.getValue().toString() : ""));
                                 }
                              }
                           }

                           if ("&table".equals(var1)) {
                              this.getContent(var2).table(var27, var22, var20);
                           } else {
                              this.getContent(var2).layout(var27, var22, var20);
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return SleepUtils.getEmptyScalar();
   }
}
