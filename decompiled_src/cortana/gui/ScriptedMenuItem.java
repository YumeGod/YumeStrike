package cortana.gui;

import cortana.core.EventManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.JMenuItem;
import sleep.bridges.SleepClosure;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class ScriptedMenuItem extends JMenuItem implements ActionListener {
   protected String label;
   protected SleepClosure code;
   protected MenuBridge bridge;
   protected Stack args;

   public ScriptedMenuItem(String var1, SleepClosure var2, MenuBridge var3) {
      if (var1.indexOf(38) > -1) {
         this.setText(var1.substring(0, var1.indexOf(38)) + var1.substring(var1.indexOf(38) + 1, var1.length()));
         this.setMnemonic(var1.charAt(var1.indexOf(38) + 1));
      } else {
         this.setText(var1);
      }

      this.code = var2;
      this.bridge = var3;
      this.label = var1;
      this.args = var3.getArguments();
      this.addActionListener(this);
   }

   public void actionPerformed(ActionEvent var1) {
      SleepUtils.runCode((SleepClosure)this.code, this.label, (ScriptInstance)null, EventManager.shallowCopy(this.args));
   }
}
