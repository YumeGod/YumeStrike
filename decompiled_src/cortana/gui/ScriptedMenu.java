package cortana.gui;

import cortana.core.EventManager;
import java.util.Stack;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import sleep.bridges.SleepClosure;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ScriptedMenu extends JMenu implements MenuListener {
   protected MenuBridge bridge;
   protected SleepClosure f;
   protected String label;
   protected Stack args;

   public ScriptedMenu(String var1, SleepClosure var2, MenuBridge var3) {
      if (var1.indexOf(38) > -1) {
         this.setText(var1.substring(0, var1.indexOf(38)) + var1.substring(var1.indexOf(38) + 1, var1.length()));
         this.setMnemonic(var1.charAt(var1.indexOf(38) + 1));
      } else {
         this.setText(var1);
      }

      this.label = var1;
      this.bridge = var3;
      this.f = var2;
      this.args = var3.getArguments();
      this.addMenuListener(this);
   }

   public void menuSelected(MenuEvent var1) {
      this.bridge.push(this, this.args);
      SleepUtils.runCode((SleepClosure)this.f, this.label, (ScriptInstance)null, EventManager.shallowCopy(this.args));
      this.bridge.pop();
   }

   public void menuDeselected(MenuEvent var1) {
      this.removeAll();
   }

   public void menuCanceled(MenuEvent var1) {
      this.removeAll();
   }
}
