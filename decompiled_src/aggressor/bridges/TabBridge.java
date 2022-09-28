package aggressor.bridges;

import aggressor.TabManager;
import cortana.Cortana;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.JComponent;
import sleep.bridges.BridgeUtilities;
import sleep.interfaces.Function;
import sleep.interfaces.Loadable;
import sleep.runtime.Scalar;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class TabBridge implements Function, Loadable {
   protected TabManager manager;
   protected Cortana engine;

   public TabBridge(Cortana var1, TabManager var2) {
      this.engine = var1;
      this.manager = var2;
   }

   public void scriptLoaded(ScriptInstance var1) {
      Cortana.put(var1, "&nextTab", this);
      Cortana.put(var1, "&previousTab", this);
      Cortana.put(var1, "&addTab", this);
      Cortana.put(var1, "&removeTab", this);
   }

   public void scriptUnloaded(ScriptInstance var1) {
   }

   public Scalar evaluate(String var1, ScriptInstance var2, Stack var3) {
      if (var1.equals("&nextTab")) {
         this.manager.nextTab();
      } else if (var1.equals("&previousTab")) {
         this.manager.previousTab();
      } else if (var1.equals("&addTab")) {
         String var4 = BridgeUtilities.getString(var3, "");
         Object var5 = BridgeUtilities.getObject(var3);
         String var6 = BridgeUtilities.getString(var3, (String)null);
         this.manager.addTab(var4, (JComponent)var5, new TabRemoveListener(var2, var4, (JComponent)var5), var6);
      } else if (var1.equals("&removeTab")) {
         Object var7 = BridgeUtilities.getObject(var3);
         this.manager.removeTab((JComponent)var7);
      }

      return SleepUtils.getEmptyScalar();
   }

   private class TabRemoveListener implements ActionListener {
      protected String title;
      protected JComponent comp;
      protected ScriptInstance ctx;

      public TabRemoveListener(ScriptInstance var2, String var3, JComponent var4) {
         this.title = var3;
         this.comp = var4;
         this.ctx = var2;
      }

      public void actionPerformed(ActionEvent var1) {
         Stack var2 = new Stack();
         var2.push(SleepUtils.getScalar((Object)this.comp));
         var2.push(SleepUtils.getScalar(this.title));
      }
   }
}
