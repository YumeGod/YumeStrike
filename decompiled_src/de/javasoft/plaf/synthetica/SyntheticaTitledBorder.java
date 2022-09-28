package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.TitledBorderPainter;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

public class SyntheticaTitledBorder implements Border {
   public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
      TitledBorderPainter.getInstance().paintBorder(var1, var2, var3, var4, var5, var6);
   }

   public Insets getBorderInsets(Component var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.titledBorder.insets", var1);
      return (Insets)var2.clone();
   }

   public boolean isBorderOpaque() {
      return false;
   }
}
