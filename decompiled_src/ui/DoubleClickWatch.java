package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DoubleClickWatch extends MouseAdapter {
   protected DoubleClickListener l;

   public DoubleClickWatch(DoubleClickListener var1) {
      this.l = var1;
   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.getClickCount() >= 2) {
         this.l.doubleClicked(var1);
         var1.consume();
      }

   }
}
