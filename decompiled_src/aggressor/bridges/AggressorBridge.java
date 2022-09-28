package aggressor.bridges;

import aggressor.AggressorClient;
import aggressor.ColorManagerScripted;
import aggressor.DataUtils;
import aggressor.MultiFrame;
import aggressor.TabManager;
import aggressor.browsers.Beacons;
import aggressor.browsers.Sessions;
import aggressor.browsers.Targets;
import aggressor.dialogs.AboutDialog;
import aggressor.dialogs.AutoRunDialog;
import aggressor.dialogs.BrowserPivotSetup;
import aggressor.dialogs.CloneSiteDialog;
import aggressor.dialogs.ConnectDialog;
import aggressor.dialogs.CovertVPNSetup;
import aggressor.dialogs.GoldenTicketDialog;
import aggressor.dialogs.HTMLApplicationDialog;
import aggressor.dialogs.HostFileDialog;
import aggressor.dialogs.JavaSignedAppletDialog;
import aggressor.dialogs.JavaSmartAppletDialog;
import aggressor.dialogs.JumpDialogAlt;
import aggressor.dialogs.MakeTokenDialog;
import aggressor.dialogs.OfficeMacroDialog;
import aggressor.dialogs.PayloadGeneratorDialog;
import aggressor.dialogs.PayloadGeneratorStageDialog;
import aggressor.dialogs.PivotListenerSetup;
import aggressor.dialogs.PortScanDialog;
import aggressor.dialogs.PortScanLocalDialog;
import aggressor.dialogs.PreferencesDialog;
import aggressor.dialogs.SOCKSSetup;
import aggressor.dialogs.ScListenerChooser;
import aggressor.dialogs.ScriptedWebDialog;
import aggressor.dialogs.ScriptedWebStageDialog;
import aggressor.dialogs.SecureShellDialog;
import aggressor.dialogs.SecureShellPubKeyDialog;
import aggressor.dialogs.SpawnAsDialog;
import aggressor.dialogs.SpearPhishDialog;
import aggressor.dialogs.SystemInformationDialog;
import aggressor.dialogs.SystemProfilerDialog;
import aggressor.dialogs.WindowsDropperDialog;
import aggressor.dialogs.WindowsExecutableDialog;
import aggressor.dialogs.WindowsExecutableStageDialog;
import aggressor.viz.PivotGraph;
import aggressor.windows.ApplicationManager;
import aggressor.windows.BeaconBrowser;
import aggressor.windows.BeaconConsole;
import aggressor.windows.CortanaConsole;
import aggressor.windows.CredentialManager;
import aggressor.windows.DownloadBrowser;
import aggressor.windows.EventLog;
import aggressor.windows.FileBrowser;
import aggressor.windows.InterfaceManager;
import aggressor.windows.KeystrokeBrowser;
import aggressor.windows.ListenerManager;
import aggressor.windows.ProcessBrowser;
import aggressor.windows.ProcessBrowserMulti;
import aggressor.windows.SOCKSBrowser;
import aggressor.windows.ScreenshotBrowser;
import aggressor.windows.ScriptManager;
import aggressor.windows.SecureShellConsole;
import aggressor.windows.ServiceBrowser;
import aggressor.windows.SiteManager;
import aggressor.windows.TargetBrowser;
import aggressor.windows.WebLog;
import common.BeaconEntry;
import common.CommonUtils;
import common.Keys;
import common.ScriptUtils;
import common.TeamQueue;
import console.Console;
import cortana.Cortana;
import dialog.DialogUtils;
import dialog.SafeDialogCallback;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.swing.JComponent;
import sleep.bridges.BridgeUtilities;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class AggressorBridge implements Function, Loadable {
   protected TabManager manager;
   protected Cortana engine;
   protected MultiFrame window;
   protected AggressorClient client;
   protected TeamQueue conn;

   public AggressorBridge(AggressorClient var1, Cortana var2, TabManager var3, MultiFrame var4, TeamQueue var5) {
      this.client = var1;
      this.engine = var2;
      this.manager = var3;
      this.window = var4;
      this.conn = var5;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&openScriptConsole", this);
      Cortana.put(var1, "&openEventLog", this);
      Cortana.put(var1, "&openConnectDialog", this);
      Cortana.put(var1, "&closeClient", this);
      Cortana.put(var1, "&openHostFileDialog", this);
      Cortana.put(var1, "&openWebLog", this);
      Cortana.put(var1, "&openSiteManager", this);
      Cortana.put(var1, "&openListenerManager", this);
      Cortana.put(var1, "&openBeaconBrowser", this);
      Cortana.put(var1, "&openWindowsExecutableStageDialog", this);
      Cortana.put(var1, "&openAutoRunDialog", this);
      Cortana.put(var1, "&openPayloadHelper", this);
      Cortana.put(var1, "&openWindowsExecutableDialog", this);
      Cortana.put(var1, "&openPayloadGeneratorDialog", this);
      Cortana.put(var1, "&openPayloadGeneratorStageDialog", this);
      Cortana.put(var1, "&openOfficeMacroDialog", this);
      Cortana.put(var1, "&openJavaSignedAppletDialog", this);
      Cortana.put(var1, "&openJavaSmartAppletDialog", this);
      Cortana.put(var1, "&openHTMLApplicationDialog", this);
      Cortana.put(var1, "&openWindowsDropperDialog", this);
      Cortana.put(var1, "&openPowerShellWebDialog", this);
      Cortana.put(var1, "&openScriptedWebDialog", this);
      Cortana.put(var1, "&openBeaconConsole", this);
      Cortana.put(var1, "&openProcessBrowser", this);
      Cortana.put(var1, "&openFileBrowser", this);
      Cortana.put(var1, "&openCloneSiteDialog", this);
      Cortana.put(var1, "&openSystemProfilerDialog", this);
      Cortana.put(var1, "&openSpearPhishDialog", this);
      Cortana.put(var1, "&openPreferencesDialog", this);
      Cortana.put(var1, "&openScriptManager", this);
      Cortana.put(var1, "&openAboutDialog", this);
      Cortana.put(var1, "&openInterfaceManager", this);
      Cortana.put(var1, "&openScreenshotBrowser", this);
      Cortana.put(var1, "&openKeystrokeBrowser", this);
      Cortana.put(var1, "&openDownloadBrowser", this);
      Cortana.put(var1, "&openBrowserPivotSetup", this);
      Cortana.put(var1, "&openCovertVPNSetup", this);
      Cortana.put(var1, "&openSOCKSSetup", this);
      Cortana.put(var1, "&openPivotListenerSetup", this);
      Cortana.put(var1, "&openSOCKSBrowser", this);
      Cortana.put(var1, "&openGoldenTicketDialog", this);
      Cortana.put(var1, "&openMakeTokenDialog", this);
      Cortana.put(var1, "&openSpawnAsDialog", this);
      Cortana.put(var1, "&openCredentialManager", this);
      Cortana.put(var1, "&openApplicationManager", this);
      Cortana.put(var1, "&openJumpDialog", this);
      Cortana.put(var1, "&openTargetBrowser", this);
      Cortana.put(var1, "&openServiceBrowser", this);
      Cortana.put(var1, "&openPortScanner", this);
      Cortana.put(var1, "&openPortScannerLocal", this);
      Cortana.put(var1, "&openSystemInformationDialog", this);
      Cortana.put(var1, "&getAggressorClient", this);
      Cortana.put(var1, "&highlight", this);
      Cortana.put(var1, "&addVisualization", this);
      Cortana.put(var1, "&showVisualization", this);
      Cortana.put(var1, "&pgraph", this);
      Cortana.put(var1, "&tbrowser", this);
      Cortana.put(var1, "&bbrowser", this);
      Cortana.put(var1, "&sbrowser", this);
      Cortana.put(var1, "&colorPanel", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&openScriptConsole")) {
         Console var41 = (new CortanaConsole(this.engine)).getConsole();
         this.manager.addTab("Script Console", var41, (ActionListener)null, "Cortana script console");
         return SleepUtils.getScalar((Object)var41);
      } else {
         Console var25;
         if (var1.equals("&openEventLog")) {
            EventLog var40 = new EventLog(this.client.getData(), this.engine, this.client.getConnection());
            var25 = var40.getConsole();
            this.manager.addTab("Event Log", var25, var40.cleanup(), "Log of events/chat messages");
            return SleepUtils.getScalar((Object)var25);
         } else if (var1.equals("&openWebLog")) {
            WebLog var39 = new WebLog(this.client.getData(), this.engine, this.client.getConnection());
            var25 = var39.getConsole();
            this.manager.addTab("Web Log", var25, var39.cleanup(), "Log of web server activity");
            return SleepUtils.getScalar((Object)var25);
         } else if (var1.equals("&openSiteManager")) {
            SiteManager var38 = new SiteManager(this.client.getData(), this.engine, this.client.getConnection());
            this.manager.addTab("Sites", var38.getContent(), var38.cleanup(), "Manage Cobalt Strike's web server");
            return SleepUtils.getEmptyScalar();
         } else if (var1.equals("&openListenerManager")) {
            ListenerManager var37 = new ListenerManager(this.client);
            this.manager.addTab("Listeners", var37.getContent(), var37.cleanup(), "Manage Cobalt Strike's listeners");
            return SleepUtils.getEmptyScalar();
         } else if (var1.equals("&openCredentialManager")) {
            CredentialManager var36 = new CredentialManager(this.client);
            this.manager.addTab("Credentials", var36.getContent(), var36.cleanup(), "Manage credentials");
            return SleepUtils.getEmptyScalar();
         } else if (var1.equals("&openApplicationManager")) {
            ApplicationManager var35 = new ApplicationManager(this.client);
            this.manager.addTab("Applications", var35.getContent(), var35.cleanup(), "View system profiler results");
            return SleepUtils.getEmptyScalar();
         } else if (var1.equals("&openBeaconBrowser")) {
            BeaconBrowser var34 = new BeaconBrowser(this.client);
            this.manager.addTab("Beacons", var34.getContent(), var34.cleanup(), "Haters gonna hate, beacons gonna beacon");
            return SleepUtils.getEmptyScalar();
         } else if (var1.equals("&openTargetBrowser")) {
            TargetBrowser var33 = new TargetBrowser(this.client);
            this.manager.addTab("Targets", var33.getContent(), var33.cleanup(), "Hosts that Cobalt Strike knows about");
            return SleepUtils.getEmptyScalar();
         } else {
            String[] var10;
            if (var1.equals("&openServiceBrowser")) {
               var10 = CommonUtils.toStringArray(BridgeUtilities.getArray(var3));
               ServiceBrowser var23 = new ServiceBrowser(this.client, var10);
               this.manager.addTab("Services", var23.getContent(), var23.cleanup(), "Services known by Cobalt Strike");
               return SleepUtils.getEmptyScalar();
            } else if (var1.equals("&openPortScanner")) {
               var10 = CommonUtils.toStringArray(BridgeUtilities.getArray(var3));
               (new PortScanDialog(this.client, var10)).show();
               return SleepUtils.getEmptyScalar();
            } else {
               String var4;
               if (var1.equals("&openPortScannerLocal")) {
                  var4 = BridgeUtilities.getString(var3, "");
                  (new PortScanLocalDialog(this.client, var4)).show();
                  return SleepUtils.getEmptyScalar();
               } else {
                  BeaconEntry var5;
                  if (var1.equals("&openBeaconConsole")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     var5 = DataUtils.getBeacon(this.client.getData(), var4);
                     if (var5 == null) {
                        throw new RuntimeException("No beacon entry for: '" + var4 + "'");
                     }

                     if (var5.isBeacon()) {
                        BeaconConsole var6 = new BeaconConsole(var4, this.client);
                        this.manager.addTab(var5.title(), var6.getConsole(), var6.cleanup(), "Beacon console");
                     } else if (var5.isSSH()) {
                        SecureShellConsole var12 = new SecureShellConsole(var4, this.client);
                        this.manager.addTab(var5.title(), var12.getConsole(), var12.cleanup(), "SSH console");
                     }
                  } else if (var1.equals("&openProcessBrowser")) {
                     var10 = BeaconBridge.bids(var3);
                     if (var10.length == 1) {
                        var5 = DataUtils.getBeacon(this.client.getData(), var10[0]);
                        ProcessBrowser var14 = new ProcessBrowser(this.client, var10[0]);
                        this.manager.addTab(var5.title("Processes"), var14.getContent(), (ActionListener)null, "Process Browser");
                     } else {
                        ProcessBrowserMulti var11 = new ProcessBrowserMulti(this.client, var10);
                        this.manager.addTab("Processes", var11.getContent(), (ActionListener)null, "Process Browser");
                     }
                  } else if (var1.equals("&openFileBrowser")) {
                     var10 = BeaconBridge.bids(var3);
                     if (var10.length == 1) {
                        var5 = DataUtils.getBeacon(this.client.getData(), var10[0]);
                        FileBrowser var16 = new FileBrowser(this.client, var10[0]);
                        this.manager.addTab(var5.title("Files"), var16.getContent(), (ActionListener)null, "File Browser");
                     }
                  } else if (var1.equals("&openBrowserPivotSetup")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     (new BrowserPivotSetup(this.client, var4)).show();
                  } else if (var1.equals("&openGoldenTicketDialog")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     (new GoldenTicketDialog(this.client, var4)).show();
                  } else if (var1.equals("&openMakeTokenDialog")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     (new MakeTokenDialog(this.client, var4)).show();
                  } else if (var1.equals("&openSpawnAsDialog")) {
                     var4 = BridgeUtilities.getString(var3, "");
                     (new SpawnAsDialog(this.client, var4)).show();
                  } else {
                     String[] var13;
                     if (var1.equals("&openJumpDialog")) {
                        var4 = BridgeUtilities.getString(var3, "");
                        var13 = CommonUtils.toStringArray(BridgeUtilities.getArray(var3));
                        if (var4.equals("ssh")) {
                           (new SecureShellDialog(this.client, var13)).show();
                        } else if (var4.equals("ssh-key")) {
                           (new SecureShellPubKeyDialog(this.client, var13)).show();
                        } else {
                           (new JumpDialogAlt(this.client, var13, var4)).show();
                        }
                     } else if (var1.equals("&openSOCKSSetup")) {
                        var4 = BridgeUtilities.getString(var3, "");
                        (new SOCKSSetup(this.client, var4)).show();
                     } else if (var1.equals("&openPivotListenerSetup")) {
                        var4 = BridgeUtilities.getString(var3, "");
                        (new PivotListenerSetup(this.client, var4)).show();
                     } else if (var1.equals("&openCovertVPNSetup")) {
                        var4 = BridgeUtilities.getString(var3, "");
                        (new CovertVPNSetup(this.client, var4)).show();
                     } else if (var1.equals("&openScreenshotBrowser")) {
                        ScreenshotBrowser var19 = new ScreenshotBrowser(this.client.getData(), this.engine, this.client.getConnection());
                        this.manager.addTab("Screenshots", var19.getContent(), var19.cleanup(), "Screenshot browser");
                     } else if (var1.equals("&openSOCKSBrowser")) {
                        SOCKSBrowser var21 = new SOCKSBrowser(this.client);
                        this.manager.addTab("Proxy Pivots", var21.getContent(), var21.cleanup(), "Beacon SOCKS Servers, port forwards, and reverse port forwards.");
                     } else if (var1.equals("&openKeystrokeBrowser")) {
                        KeystrokeBrowser var22 = new KeystrokeBrowser(this.client.getData(), this.engine, this.client.getConnection());
                        this.manager.addTab("Keystrokes", var22.getContent(), var22.cleanup(), "Keystroke browser");
                     } else if (var1.equals("&openDownloadBrowser")) {
                        DownloadBrowser var24 = new DownloadBrowser(this.client.getData(), this.engine, this.client.getConnection());
                        this.manager.addTab("Downloads", var24.getContent(), var24.cleanup(), "Downloads browser");
                     } else if (var1.equals("&openConnectDialog")) {
                        (new ConnectDialog(this.window)).show();
                     } else if (var1.equals("&openHostFileDialog")) {
                        (new HostFileDialog(this.window, this.conn, this.client.getData())).show();
                     } else if (var1.equals("&openCloneSiteDialog")) {
                        (new CloneSiteDialog(this.window, this.conn, this.client.getData())).show();
                     } else if (var1.equals("&openSystemProfilerDialog")) {
                        (new SystemProfilerDialog(this.window, this.conn, this.client.getData())).show();
                     } else if (var1.equals("&openSpearPhishDialog")) {
                        (new SpearPhishDialog(this.client, this.window, this.conn, this.client.getData())).show();
                     } else if (var1.equals("&closeClient")) {
                        this.client.kill();
                     } else if (var1.equals("&openWindowsExecutableStageDialog")) {
                        (new WindowsExecutableStageDialog(this.client)).show();
                     } else if (var1.equals("&openAutoRunDialog")) {
                        (new AutoRunDialog(this.window, this.conn)).show();
                     } else if (var1.equals("&openPayloadHelper")) {
                        final SleepClosure var26 = BridgeUtilities.getFunction(var3, var2);
                        ScListenerChooser.ListenersAll(this.client, new SafeDialogCallback() {
                           public void dialogResult(String var1) {
                              Stack var2 = new Stack();
                              var2.push(SleepUtils.getScalar(var1));
                              SleepUtils.runCode((SleepClosure)var26, "dialogResult", (ScriptInstance)null, var2);
                           }
                        }).show();
                     } else if (var1.equals("&openWindowsExecutableDialog")) {
                        (new WindowsExecutableDialog(this.client)).show();
                     } else if (var1.equals("&openPayloadGeneratorDialog")) {
                        (new PayloadGeneratorDialog(this.client)).show();
                     } else if (var1.equals("&openPayloadGeneratorStageDialog")) {
                        (new PayloadGeneratorStageDialog(this.client)).show();
                     } else if (var1.equals("&openOfficeMacroDialog")) {
                        (new OfficeMacroDialog(this.client)).show();
                     } else if (var1.equals("&openJavaSignedAppletDialog")) {
                        (new JavaSignedAppletDialog(this.client)).show();
                     } else if (var1.equals("&openJavaSmartAppletDialog")) {
                        (new JavaSmartAppletDialog(this.client)).show();
                     } else if (var1.equals("&openHTMLApplicationDialog")) {
                        (new HTMLApplicationDialog(this.client)).show();
                     } else if (var1.equals("&openWindowsDropperDialog")) {
                        (new WindowsDropperDialog(this.client)).show();
                     } else if (var1.equals("&openPowerShellWebDialog")) {
                        (new ScriptedWebDialog(this.client)).show();
                     } else if (var1.equals("&openScriptedWebDialog")) {
                        (new ScriptedWebStageDialog(this.client)).show();
                     } else if (var1.equals("&openPreferencesDialog")) {
                        (new PreferencesDialog()).show();
                     } else if (var1.equals("&openAboutDialog")) {
                        (new AboutDialog()).show();
                     } else if (var1.equals("&openScriptManager")) {
                        ScriptManager var27 = new ScriptManager(this.client);
                        this.manager.addTab("Scripts", var27.getContent(), (ActionListener)null, "Manage your Aggressor scripts.");
                     } else if (var1.equals("&openInterfaceManager")) {
                        InterfaceManager var28 = new InterfaceManager(this.client.getData(), this.engine, this.client.getConnection());
                        this.manager.addTab("Interfaces", var28.getContent(), var28.cleanup(), "Manage Covert VPN Interfaces");
                     } else if (var1.equals("&openSystemInformationDialog")) {
                        (new SystemInformationDialog(this.client)).show();
                     } else {
                        JComponent var15;
                        if (var1.equals("&addVisualization")) {
                           var4 = BridgeUtilities.getString(var3, "");
                           var15 = (JComponent)BridgeUtilities.getObject(var3);
                           this.client.addViz(var4, var15);
                        } else if (var1.equals("&showVisualization")) {
                           var4 = BridgeUtilities.getString(var3, "");
                           this.client.showViz(var4);
                        } else {
                           if (var1.equals("&pgraph")) {
                              PivotGraph var32 = new PivotGraph(this.client);
                              var32.ready();
                              return SleepUtils.getScalar((Object)var32.getContent());
                           }

                           if (var1.equals("&tbrowser")) {
                              Targets var31 = new Targets(this.client);
                              var15 = var31.getContent();
                              DialogUtils.setupScreenshotShortcut(this.client, var31.getTable(), "Targets");
                              return SleepUtils.getScalar((Object)var15);
                           }

                           if (var1.equals("&bbrowser")) {
                              Beacons var30 = new Beacons(this.client, true);
                              var15 = var30.getContent();
                              DialogUtils.setupScreenshotShortcut(this.client, var30.getTable(), "Beacons");
                              return SleepUtils.getScalar((Object)var15);
                           }

                           if (var1.equals("&sbrowser")) {
                              Sessions var29 = new Sessions(this.client, true);
                              var15 = var29.getContent();
                              DialogUtils.setupScreenshotShortcut(this.client, var29.getTable(), "Sessions");
                              return SleepUtils.getScalar((Object)var15);
                           }

                           if (var1.equals("&colorPanel")) {
                              var4 = BridgeUtilities.getString(var3, "");
                              var13 = ScriptUtils.ArrayOrString(var3);
                              ColorManagerScripted var20 = new ColorManagerScripted(this.client, var4, var13);
                              return SleepUtils.getScalar((Object)var20.getColorPanel());
                           }

                           if (var1.equals("&getAggressorClient")) {
                              return SleepUtils.getScalar((Object)this.client);
                           }

                           if (var1.equals("&highlight")) {
                              var4 = BridgeUtilities.getString(var3, "");
                              List var17 = SleepUtils.getListFromArray(BridgeUtilities.getArray(var3));
                              String var18 = BridgeUtilities.getString(var3, "");
                              HashMap var7 = new HashMap();
                              var7.put("_accent", var18);
                              Iterator var8 = var17.iterator();

                              while(var8.hasNext()) {
                                 Map var9 = (Map)var8.next();
                                 this.client.getConnection().call(var4 + ".update", CommonUtils.args(Keys.ToKey(var4, var9), var7));
                              }

                              this.client.getConnection().call(var4 + ".push");
                           }
                        }
                     }
                  }

                  return SleepUtils.getEmptyScalar();
               }
            }
         }
      }
   }
}
