package aggressor.windows;

import aggressor.AggressorClient;
import aggressor.DataUtils;
import beacon.BeaconCommands;
import beacon.SecureShellTabCompletion;
import common.BeaconOutput;
import common.Callback;
import common.CommandParser;
import common.CommonUtils;
import console.Colors;
import console.GenericTabCompletion;
import dialog.SafeDialogCallback;
import dialog.SafeDialogs;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Stack;
import javax.swing.JTextField;
import sleep.runtime.SleepUtils;

public class SecureShellConsole extends BeaconConsole {
   public SecureShellConsole(String var1, AggressorClient var2) {
      super(var1, var2);
   }

   public String getPrompt() {
      return Colors.underline("ssh") + "> ";
   }

   public String Script(String var1) {
      return "SSH_" + var1;
   }

   public GenericTabCompletion getTabCompletion() {
      return new SecureShellTabCompletion(this.bid, this.client, this.console);
   }

   public void showPopup(String var1, MouseEvent var2) {
      Stack var3 = new Stack();
      LinkedList var4 = new LinkedList();
      var4.add(this.bid);
      var3.push(SleepUtils.getArrayWrapper(var4));
      this.engine.getMenuBuilder().installMenu(var2, "ssh", var3);
   }

   public void actionPerformed(ActionEvent var1) {
      String var2 = var1.getActionCommand().trim();
      ((JTextField)var1.getSource()).setText("");
      CommandParser var3 = new CommandParser(var2);
      if (this.client.getSSHAliases().isAlias(var3.getCommand())) {
         this.master.input(var2);
         this.client.getSSHAliases().fireCommand(this.bid, var3.getCommand(), var3.getArguments());
      } else {
         String var4;
         if (!var3.is("help") && !var3.is("?")) {
            if (var3.is("downloads")) {
               this.console.append(this.formatLocal(BeaconOutput.Input(this.bid, var2)) + "\n");
               this.conn.call("beacons.downloads", CommonUtils.args(this.bid), new Callback() {
                  public void result(String var1, Object var2) {
                     Stack var3 = new Stack();
                     var3.push(CommonUtils.convertAll(var2));
                     var3.push(SleepUtils.getScalar(SecureShellConsole.this.bid));
                     SecureShellConsole.this.console.append(SecureShellConsole.this.engine.format("BEACON_OUTPUT_DOWNLOADS", var3) + "\n");
                  }
               });
            } else {
               this.master.input(var2);
               if (var3.is("cancel")) {
                  if (var3.verify("Z")) {
                     this.master.Cancel(var3.popString());
                  }
               } else if (var3.is("cd")) {
                  if (var3.verify("Z")) {
                     this.master.Cd(var3.popString());
                  }
               } else if (var3.is("clear")) {
                  this.master.Clear();
               } else {
                  int var7;
                  String var8;
                  if (var3.is("connect")) {
                     if (!var3.verify("AI") && !var3.reset()) {
                        if (var3.verify("Z")) {
                           var4 = var3.popString();
                           this.master.Connect(var4);
                        }
                     } else {
                        var7 = var3.popInt();
                        var8 = var3.popString();
                        this.master.Connect(var8, var7);
                     }
                  } else if (var3.is("download")) {
                     if (var3.verify("Z")) {
                        this.master.Download(var3.popString());
                     }
                  } else if (var3.is("exit")) {
                     this.master.Die();
                  } else if (var3.is("getuid")) {
                     this.master.GetUID();
                  } else if (var3.is("note")) {
                     if (var3.verify("Z")) {
                        var4 = var3.popString();
                        this.master.Note(var4);
                     } else if (var3.isMissingArguments()) {
                        this.master.Note("");
                     }
                  } else if (var3.is("pwd")) {
                     this.master.Pwd();
                  } else if (var3.is("rportfwd")) {
                     if (!var3.verify("IAI") && !var3.reset()) {
                        if (var3.verify("AI")) {
                           var7 = var3.popInt();
                           var8 = var3.popString();
                           if (!"stop".equals(var8)) {
                              var3.error("only acceptable argument is stop");
                           } else {
                              this.master.PortForwardStop(var7);
                           }
                        }
                     } else {
                        var7 = var3.popInt();
                        var8 = var3.popString();
                        int var9 = var3.popInt();
                        this.master.PortForward(var9, var8, var7);
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
                        var7 = var3.popInt();
                        int var10 = var3.popInt();
                        this.master.Sleep(var10, var7);
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
                  } else if (var3.is("sudo")) {
                     if (var3.verify("AZ")) {
                        var4 = var3.popString();
                        var8 = var3.popString();
                        this.master.ShellSudo(var8, var4);
                     }
                  } else if (var3.is("unlink")) {
                     if (!var3.verify("AI") && !var3.reset()) {
                        if (var3.verify("Z")) {
                           this.master.Unlink(var3.popString());
                        }
                     } else {
                        var4 = var3.popString();
                        var8 = var3.popString();
                        this.master.Unlink(var8, var4);
                     }
                  } else if (var3.is("upload") && var3.empty()) {
                     SafeDialogs.openFile("Select file to upload", (String)null, (String)null, false, false, new SafeDialogCallback() {
                        public void dialogResult(String var1) {
                           if (CommonUtils.lof(var1) > 786432L) {
                              SecureShellConsole.this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(SecureShellConsole.this.bid, "File " + var1 + " is larger than 768KB")));
                           } else {
                              SecureShellConsole.this.master.Upload(var1);
                           }

                        }
                     });
                  } else if (var3.is("upload")) {
                     if (var3.verify("F")) {
                        var4 = var3.popString();
                        if (CommonUtils.lof(var4) > 786432L) {
                           this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(this.bid, "File " + var4 + " is larger than 768KB")));
                        } else {
                           this.master.Upload(var4);
                        }
                     }
                  } else {
                     this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(this.bid, "Unknown command: " + var2)));
                  }
               }

               if (var3.hasError()) {
                  this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(this.bid, var3.error())));
               }

            }
         } else {
            this.console.append(this.formatLocal(BeaconOutput.Input(this.bid, var2)) + "\n");
            if (!var3.verify("Z") && !var3.reset()) {
               this.console.append(this.engine.format("SSH_OUTPUT_HELP", new Stack()) + "\n");
            } else {
               var4 = var3.popString();
               BeaconCommands var5 = DataUtils.getSSHCommands(this.data);
               if (var5.isHelpAvailable(var4)) {
                  Stack var6 = new Stack();
                  var6.push(SleepUtils.getScalar(var4));
                  this.console.append(this.engine.format("SSH_OUTPUT_HELP_COMMAND", var6) + "\n");
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
