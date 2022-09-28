package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthContext;

class FocusPainter {
   static void paintFocus(String var0, SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      paintFocus(var0, var1, (String)null, var2, var3, var4, var5, var6);
   }

   static void paintFocus(String var0, SynthContext var1, String var2, Graphics var3, int var4, int var5, int var6, int var7) {
      JComponent var8 = var1.getComponent();
      int var9 = var1.getComponentState();
      paintFocus(var0, var8, var9, var2, var3, var4, var5, var6, var7);
   }

   static void paintFocus(String var0, JComponent var1, int var2, String var3, Graphics var4, int var5, int var6, int var7, int var8) {
      paintFocus(var0, var1, var2, var3, 0, var4, var5, var6, var7, var8);
   }

   static void paintFocus(String var0, JComponent var1, int var2, String var3, int var4, Graphics var5, int var6, int var7, int var8, int var9) {
      String var10 = SyntheticaLookAndFeel.getStyleName(var1);
      String var11 = SyntheticaLookAndFeel.getString(var0, (String)null, var10, true);
      Insets var12 = SyntheticaLookAndFeel.getInsets(var0, "insets", var10, true);
      boolean var13 = (Boolean)SyntheticaLookAndFeel.getClientProperty("Synthetica.paintFocus", var1, true);
      if (var11 != null && var11.length() > 0 && var13) {
         int var14 = SyntheticaLookAndFeel.getInt(var0, "animation.cycles", var10, true, -1);
         int var15 = SyntheticaLookAndFeel.getInt(var0, "animation.delay", var10, true, 60);
         int var16 = SyntheticaLookAndFeel.getInt(var0, "animation.type", var10, true, 3);
         ImagePainter var18 = new ImagePainter(var1, var3, var14, var15, var16, var2, var5, var6, var7, var8, var9, var11, var12, var12, 1, 1);
         var18.setAngle(var4);
         var18.draw();
      }

   }
}
