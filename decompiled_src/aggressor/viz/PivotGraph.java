package aggressor.viz;

import aggressor.AggressorClient;
import common.AObject;
import common.BeaconEntry;
import common.Callback;
import common.CommonUtils;
import common.TabScreenshot;
import dialog.DialogUtils;
import graph.GraphPopup;
import graph.NetworkGraph;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import sleep.runtime.SleepUtils;

public class PivotGraph extends AObject implements Callback, GraphPopup {
   protected AggressorClient client = null;
   protected NetworkGraph graph = new NetworkGraph();
   protected long last = 0L;

   public PivotGraph(AggressorClient var1) {
      this.client = var1;
      this.graph.setGraphPopup(this);
      this.graph.addActionForKey("ctrl pressed P", new AbstractAction() {
         public void actionPerformed(ActionEvent var1) {
            Image var2 = PivotGraph.this.graph.getScreenshot();
            byte[] var3 = DialogUtils.toImage((BufferedImage)var2, "png");
            PivotGraph.this.client.getConnection().call("aggressor.screenshot", CommonUtils.args(new TabScreenshot("Pivot Graph", var3)));
            DialogUtils.showInfo("Pushed screenshot to team server");
         }
      });
   }

   public void ready() {
      this.client.getData().subscribe("beacons", this);
   }

   public void showGraphPopup(String[] var1, MouseEvent var2) {
      if (var1.length > 0) {
         DialogUtils.showSessionPopup(this.client, var2, var1);
      } else {
         Stack var3 = new Stack();
         var3.push(SleepUtils.getScalar((Object)this.getContent()));
         this.client.getScriptEngine().getMenuBuilder().installMenu(var2, "pgraph", var3);
      }

   }

   public void showPopup(MouseEvent var1) {
      Stack var2 = new Stack();
      this.client.getScriptEngine().getMenuBuilder().installMenu(var1, "beacon", var2);
   }

   public JComponent getContent() {
      return this.graph;
   }

   public void result(String var1, Object var2) {
      if (this.graph.isShowing()) {
         Map var3 = (Map)var2;
         long var4 = CommonUtils.dataIdentity(var3);
         if (var4 != this.last) {
            this.last = var4;
            this.graph.start();
            if (var3.size() > 0) {
               this.graph.addNode("", "", "", DialogUtils.TargetVisualization("firewall", 0.0, false, false), "", "");
            }

            Iterator var6 = var3.values().iterator();

            BeaconEntry var7;
            while(var6.hasNext()) {
               var7 = (BeaconEntry)var6.next();
               Image var8 = null;
               if (var7.isEmpty()) {
                  var8 = DialogUtils.TargetVisualization("unknown", 0.0, false, false);
               } else {
                  var8 = DialogUtils.TargetVisualization(var7.getOperatingSystem().toLowerCase(), var7.getVersion(), var7.isAdmin(), !var7.isAlive());
               }

               if (var7.isEmpty()) {
                  if ("".equals(var7.getNote())) {
                     this.graph.addNode(var7.getId(), "[unknown]", "", var8, "", var7.getAccent());
                  } else {
                     this.graph.addNode(var7.getId(), "[unknown]\n" + var7.getNote(), "", var8, "", var7.getAccent());
                  }
               } else if (var7.isSSH()) {
                  if ("".equals(var7.getNote())) {
                     this.graph.addNode(var7.getId(), var7.getComputer(), var7.getUser(), var8, var7.getInternal(), var7.getAccent());
                  } else {
                     this.graph.addNode(var7.getId(), var7.getComputer() + "\n" + var7.getNote(), var7.getUser(), var8, var7.getInternal(), var7.getAccent());
                  }
               } else if ("".equals(var7.getNote())) {
                  this.graph.addNode(var7.getId(), var7.getComputer() + " @ " + var7.getPid(), var7.getUser(), var8, var7.getInternal(), var7.getAccent());
               } else {
                  this.graph.addNode(var7.getId(), var7.getComputer() + " @ " + var7.getPid() + "\n" + var7.getNote(), var7.getUser(), var8, var7.getInternal(), var7.getAccent());
               }

               if (var7.getParentId().length() == 0) {
                  if ("".equals(var7.getExternal())) {
                     this.graph.addEdge("", var7.getId(), "#FFFF00", "4", "true", "", 2);
                  } else {
                     this.graph.addEdge("", var7.getId(), "#00FF00", "4", "true", "", 2);
                  }
               }
            }

            var6 = var3.values().iterator();

            while(var6.hasNext()) {
               var7 = (BeaconEntry)var6.next();
               if (var7.getParentId().length() > 0) {
                  String var9 = "";
                  String var10 = "";
                  byte var11;
                  if (var7.getPivotHint().isForward()) {
                     var11 = 1;
                  } else {
                     var11 = 2;
                  }

                  if (var7.getPivotHint().isTCP()) {
                     if (var7.getLinkState() == 1) {
                        var9 = "#00FFA5";
                        var10 = "";
                     } else {
                        var9 = "#A500FF";
                        var10 = "DISCONNECTED";
                     }
                  } else if (var7.getLinkState() == 1) {
                     var9 = "#FFA500";
                     var10 = "";
                  } else {
                     var9 = "#FF0000";
                     var10 = "DISCONNECTED";
                  }

                  this.graph.addEdge(var7.getParentId(), var7.getId(), var9, "4", "false", var10, var11);
               }
            }

            this.graph.deleteNodes();
            this.graph.end();
         }
      }
   }
}
