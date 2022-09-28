package ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TableClickListener extends MouseAdapter {
   protected TablePopup popup = null;

   public void setPopup(TablePopup var1) {
      this.popup = var1;
   }

   public void mousePressed(MouseEvent var1) {
      this.checkPopup(var1);
   }

   public void mouseReleased(MouseEvent var1) {
      this.checkPopup(var1);
   }

   public void checkPopup(MouseEvent var1) {
      if (var1.isPopupTrigger() && this.popup != null) {
         this.popup.showPopup(var1);
      }

   }

   public void mouseClicked(MouseEvent var1) {
      this.checkPopup(var1);
   }
}
