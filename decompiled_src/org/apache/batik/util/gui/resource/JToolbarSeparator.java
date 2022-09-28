package org.apache.batik.util.gui.resource;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

public class JToolbarSeparator extends JComponent {
   public JToolbarSeparator() {
      this.setMaximumSize(new Dimension(15, Integer.MAX_VALUE));
   }

   protected void paintComponent(Graphics var1) {
      super.paintComponent(var1);
      Dimension var2 = this.getSize();
      int var3 = var2.width / 2;
      var1.setColor(Color.gray);
      var1.drawLine(var3, 3, var3, var2.height - 5);
      var1.drawLine(var3, 2, var3 + 1, 2);
      var1.setColor(Color.white);
      var1.drawLine(var3 + 1, 3, var3 + 1, var2.height - 5);
      var1.drawLine(var3, var2.height - 4, var3 + 1, var2.height - 4);
   }
}
