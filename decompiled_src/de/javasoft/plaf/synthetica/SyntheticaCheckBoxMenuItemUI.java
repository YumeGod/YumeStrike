package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.MenuPainter;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.synth.SynthContext;

public class SyntheticaCheckBoxMenuItemUI extends SyntheticaMenuItemUI {
   public static ComponentUI createUI(JComponent var0) {
      return new SyntheticaCheckBoxMenuItemUI();
   }

   protected String getPropertyPrefix() {
      return "CheckBoxMenuItem";
   }

   public void processMouseEvent(JMenuItem var1, MouseEvent var2, MenuElement[] var3, MenuSelectionManager var4) {
      Point var5 = var2.getPoint();
      if (var5.x >= 0 && var5.x < var1.getWidth() && var5.y >= 0 && var5.y < var1.getHeight()) {
         if (var2.getID() == 502) {
            var4.clearSelectedPath();
            var1.doClick(0);
         } else {
            var4.setSelectedPath(var3);
         }
      } else if (var1.getModel().isArmed()) {
         int var6 = var3.length - 1;
         MenuElement[] var7 = new MenuElement[var6];

         for(int var8 = 0; var8 < var6; ++var8) {
            var7[var8] = var3[var8];
         }

         var4.setSelectedPath(var7);
      }

   }

   void paintBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintCheckBoxMenuItemBackground(var1, var2, var3, var4, var5, var6);
   }

   void paintBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      MenuPainter.getInstance(var1).paintCheckBoxMenuItemBorder(var1, var2, var3, var4, var5, var6);
   }
}
