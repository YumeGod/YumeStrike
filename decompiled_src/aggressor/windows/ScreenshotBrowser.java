package aggressor.windows;

import aggressor.DataManager;
import aggressor.DataUtils;
import common.AObject;
import common.AdjustData;
import common.BeaconEntry;
import common.Screenshot;
import common.TeamQueue;
import cortana.Cortana;
import dialog.DialogUtils;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import ui.DataBrowser;
import ui.DataSelectionListener;
import ui.ZoomableImage;

public class ScreenshotBrowser extends AObject implements AdjustData, DataSelectionListener {
   protected TeamQueue conn = null;
   protected Cortana engine = null;
   protected DataManager data = null;
   protected DataBrowser browser = null;
   protected ZoomableImage viewer = null;

   public ScreenshotBrowser(DataManager var1, Cortana var2, TeamQueue var3) {
      this.engine = var2;
      this.conn = var3;
      this.data = var1;
   }

   public ActionListener cleanup() {
      return this.data.unsubOnClose("screenshots", this);
   }

   public JComponent getContent() {
      LinkedList var1 = this.data.populateAndSubscribe("screenshots", this);
      this.viewer = new ZoomableImage();
      this.browser = DataBrowser.getBeaconDataBrowser(this.engine, "data", new JScrollPane(this.viewer), var1);
      this.browser.addDataSelectionListener(this);
      DialogUtils.setupDateRenderer(this.browser.getTable(), "when");
      return this.browser;
   }

   public void selected(Object var1) {
      if (var1 != null) {
         this.viewer.setIcon(((Screenshot)var1).getImage());
      } else {
         this.viewer.setIcon((Icon)null);
      }

   }

   public Map format(String var1, Object var2) {
      Screenshot var3 = (Screenshot)var2;
      BeaconEntry var4 = DataUtils.getBeacon(this.data, var3.id());
      if (var4 == null) {
         return null;
      } else {
         Map var5 = var4.toMap();
         var5.put("when", var3.time());
         var5.put("data", var3);
         var5.put("_accent", "");
         return var5;
      }
   }

   public void result(String var1, Object var2) {
      if (this.browser != null) {
         Map var3 = this.format(var1, var2);
         if (var3 != null) {
            this.browser.addEntry(var3);
         }

      }
   }
}
