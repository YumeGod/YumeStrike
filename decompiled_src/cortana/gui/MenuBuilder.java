package cortana.gui;

import cortana.core.EventManager;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Stack;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import sleep.bridges.SleepClosure;
import sleep.interfaces.Loadable;
import sleep.runtime.ScriptInstance;
import sleep.runtime.SleepUtils;

public class MenuBuilder {
   protected MenuBridge bridge;

   public MenuBuilder(ScriptableApplication var1) {
      this.bridge = new MenuBridge(var1, this);
   }

   public Loadable getBridge() {
      return this.bridge;
   }

   public void installMenu(MouseEvent var1, String var2, Stack var3) {
      if (var1.isPopupTrigger() && this.bridge.isPopulated(var2)) {
         JPopupMenu var4 = new JPopupMenu();
         this.setupMenu(var4, var2, var3);
         if (this.bridge.isPopulated(var2)) {
            var4.show((JComponent)var1.getSource(), var1.getX(), var1.getY());
            var1.consume();
         }
      }

   }

   public void setupMenu(JComponent var1, String var2, Stack var3) {
      if (this.bridge.isPopulated(var2)) {
         this.bridge.push(var1, var3);
         Iterator var4 = this.bridge.getMenus(var2).iterator();

         while(var4.hasNext()) {
            SleepClosure var5 = (SleepClosure)var4.next();
            if (var5.getOwner().isLoaded()) {
               SleepUtils.runCode((SleepClosure)var5, var2, (ScriptInstance)null, EventManager.shallowCopy(var3));
            } else {
               var4.remove();
            }
         }

         this.bridge.pop();
      }
   }
}
