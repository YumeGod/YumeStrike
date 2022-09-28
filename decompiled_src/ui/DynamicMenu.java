package ui;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class DynamicMenu extends JMenu implements MenuListener {
   protected DynamicMenuHandler handler = null;

   public DynamicMenu(String var1) {
      super(var1);
      this.addMenuListener(this);
   }

   public void setHandler(DynamicMenuHandler var1) {
      this.handler = var1;
   }

   public void menuSelected(MenuEvent var1) {
      if (this.handler != null) {
         this.handler.setupMenu(this);
      }

   }

   public void menuCanceled(MenuEvent var1) {
      this.removeAll();
   }

   public void menuDeselected(MenuEvent var1) {
      this.removeAll();
   }

   public interface DynamicMenuHandler {
      void setupMenu(JMenu var1);
   }
}
