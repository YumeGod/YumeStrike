package dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;

public class SolidIcon implements Icon {
   private int width;
   private int height;
   private Color color;

   public SolidIcon(Color var1, int var2, int var3) {
      this.width = var2;
      this.height = var3;
      this.color = var1;
   }

   public Color getColor() {
      return this.color;
   }

   public void setColor(Color var1) {
      this.color = var1;
   }

   public int getIconWidth() {
      return this.width;
   }

   public int getIconHeight() {
      return this.height;
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      var2.setColor(this.color);
      var2.fillRect(var3, var4, this.width - 1, this.height - 1);
   }
}
