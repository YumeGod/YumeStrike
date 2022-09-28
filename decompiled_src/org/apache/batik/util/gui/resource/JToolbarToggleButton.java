package org.apache.batik.util.gui.resource;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

public class JToolbarToggleButton extends JToggleButton {
   public JToolbarToggleButton() {
      this.initialize();
   }

   public JToolbarToggleButton(String var1) {
      super(var1);
      this.initialize();
   }

   protected void initialize() {
      if (!System.getProperty("java.version").startsWith("1.3")) {
         this.setOpaque(false);
         this.setBackground(new Color(0, 0, 0, 0));
      }

      this.setBorderPainted(false);
      this.setMargin(new Insets(2, 2, 2, 2));
      if (!UIManager.getLookAndFeel().getName().equals("Windows")) {
         this.addMouseListener(new MouseListener());
      }

   }

   protected class MouseListener extends MouseAdapter {
      public void mouseEntered(MouseEvent var1) {
         JToolbarToggleButton.this.setBorderPainted(true);
      }

      public void mouseExited(MouseEvent var1) {
         JToolbarToggleButton.this.setBorderPainted(false);
      }
   }
}
