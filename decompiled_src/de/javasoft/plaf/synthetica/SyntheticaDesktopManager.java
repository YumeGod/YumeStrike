package de.javasoft.plaf.synthetica;

import java.awt.Insets;
import java.lang.reflect.Field;
import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;

public class SyntheticaDesktopManager extends DefaultDesktopManager {
   private static final long serialVersionUID = -4402279640030928428L;

   public void beginDraggingFrame(JComponent var1) {
      boolean var2 = var1.isOpaque();
      if (!var2) {
         var1.setOpaque(true);
      }

      super.beginDraggingFrame(var1);
      var1.setOpaque(var2);
   }

   public void dragFrame(JComponent var1, int var2, int var3) {
      super.dragFrame(var1, var2, var3);
      if (var1 instanceof JInternalFrame) {
         this.setDragging(var1, false);
         this.repaintBorder(var1);
         this.setDragging(var1, true);
      }

   }

   private void repaintBorder(JComponent var1) {
      Insets var2 = SyntheticaLookAndFeel.getInsets("Synthetica.internalFrame.border.insets", var1);
      int var3 = var1.getWidth();
      int var4 = var1.getHeight();
      var1.paintImmediately(0, 0, var3, var2.top);
      var1.paintImmediately(0, var2.top, var2.left, var4 - var2.top - var2.bottom);
      var1.paintImmediately(0, var4 - var2.bottom, var3, var2.bottom);
      var1.paintImmediately(var3 - var2.right, var2.top, var2.right, var4 - var2.top - var2.bottom);
   }

   private void setDragging(JComponent var1, boolean var2) {
      if (SyntheticaLookAndFeel.getJVMCompatibilityMode() == SyntheticaLookAndFeel.JVMCompatibilityMode.SUN) {
         try {
            Class var3 = JInternalFrame.class;
            Field var4 = var3.getDeclaredField("isDragging");
            var4.setAccessible(true);
            var4.set(var1, var2);
         } catch (Exception var5) {
            throw new RuntimeException(var5);
         }
      }

   }
}
