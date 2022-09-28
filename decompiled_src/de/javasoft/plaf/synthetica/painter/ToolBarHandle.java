package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;

public class ToolBarHandle extends SyntheticaIconPainter {
   private SyntheticaState state = new SyntheticaState();

   public int getIconHeight() {
      return UIManager.getInt("Synthetica.toolBar.handle.size");
   }

   public int getIconWidth() {
      return UIManager.getInt("Synthetica.toolBar.handle.size");
   }

   public void paintIcon(Component var1, Graphics var2, int var3, int var4) {
      JToolBar var5 = (JToolBar)var1;
      int var6 = var5.getOrientation();
      boolean var7 = SyntheticaLookAndFeel.getBoolean("Synthetica.toolBar.handle.stateSupportEnabled", var1);
      SynthContext var8 = null;
      if (var7) {
         SynthStyle var9 = SynthLookAndFeel.getStyle(var5, Region.TOOL_BAR);
         var8 = new SynthContext(var5, Region.TOOL_BAR, var9, 0);
         this.state = new SyntheticaPainterState(var8);
      }

      int var12 = SyntheticaLookAndFeel.getInt("Synthetica.toolBar.handle.size", var1);
      int var10 = var12;
      if (var12 > 0) {
         Insets var11 = null;
         if (var6 == 0) {
            var11 = SyntheticaLookAndFeel.getInsets("Synthetica.toolBar.margin.horizontal", var1);
            var10 = var1.getHeight() - var11.top - var11.bottom;
         } else {
            var3 = var1.getComponentOrientation().isLeftToRight() ? var3 : 0;
            var11 = SyntheticaLookAndFeel.getInsets("Synthetica.toolBar.margin.vertical", var1);
            var12 = var1.getWidth() - var11.left - var11.right;
         }

         var3 += var11.left;
         var4 += var11.top;
         ToolBarHandlePainter.getInstance(var8).paintHandle(var5, this.state, var6, var2, var3, var4, var12, var10);
      }
   }
}
