package org.apache.fop.render.awt.viewer;

import java.awt.event.ActionEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class Command extends AbstractAction {
   private static final String IMAGE_DIR = "images/";

   public Command(String name, int mnemonic) {
      super(name);
      this.putValue("ShortDescription", name);
      if (mnemonic > 0) {
         this.putValue("MnemonicKey", new Integer(mnemonic));
      }

   }

   public Command(String name, String iconName) {
      super(name);
      this.putValue("ShortDescription", name);
      URL url = this.getClass().getResource("images/" + iconName + ".gif");
      if (url != null) {
         this.putValue("SmallIcon", new ImageIcon(url));
      }

   }

   public void actionPerformed(ActionEvent e) {
      this.doit();
   }

   public void doit() {
   }
}
