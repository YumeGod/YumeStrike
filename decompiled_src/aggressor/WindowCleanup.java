package aggressor;

import common.Callback;
import common.CommonUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowCleanup implements ActionListener, WindowListener {
   protected String[] keys;
   protected Callback listener;
   protected GenericDataManager data;
   protected boolean open = true;

   public WindowCleanup(GenericDataManager var1, String var2, Callback var3) {
      this.keys = CommonUtils.toArray(var2);
      this.listener = var3;
      this.data = var1;
   }

   public void actionPerformed(ActionEvent var1) {
      for(int var2 = 0; var2 < this.keys.length; ++var2) {
         this.data.unsub(this.keys[var2], this.listener);
      }

      this.open = false;
   }

   public boolean isOpen() {
      return this.open;
   }

   public void windowClosed(WindowEvent var1) {
      for(int var2 = 0; var2 < this.keys.length; ++var2) {
         this.data.unsub(this.keys[var2], this.listener);
      }

      this.open = false;
   }

   public void windowActivated(WindowEvent var1) {
   }

   public void windowClosing(WindowEvent var1) {
   }

   public void windowDeactivated(WindowEvent var1) {
   }

   public void windowDeiconified(WindowEvent var1) {
   }

   public void windowIconified(WindowEvent var1) {
   }

   public void windowOpened(WindowEvent var1) {
   }
}
