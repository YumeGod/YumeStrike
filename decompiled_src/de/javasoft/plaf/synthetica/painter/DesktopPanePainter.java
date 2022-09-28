package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthContext;

public class DesktopPanePainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.DesktopPanePainter";

   protected DesktopPanePainter() {
   }

   public static DesktopPanePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static DesktopPanePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, DesktopPanePainter.class, "Synthetica.DesktopPanePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, DesktopPanePainter.class, "Synthetica.DesktopPanePainter");
      }

      return (DesktopPanePainter)var1;
   }

   public void paintDesktopPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintDesktopPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JComponent var7 = var1.getComponent();
      String var8 = SyntheticaLookAndFeel.getString("Synthetica.desktopPane.background", var7);
      Insets var9;
      ImagePainter var11;
      if (var8 != null && SyntheticaLookAndFeel.isOpaque(var7)) {
         var9 = SyntheticaLookAndFeel.getInsets("Synthetica.desktopPane.background.insets", var7);
         var11 = new ImagePainter(var2, var3, var4, var5, var6, var8, var9, var9, 0, 0);
         var11.draw();
      }

      var8 = SyntheticaLookAndFeel.getString("Synthetica.desktopPane.patternBackground", var7);
      if (var8 != null && SyntheticaLookAndFeel.isOpaque(var7)) {
         var9 = SyntheticaLookAndFeel.getInsets("Synthetica.desktopPane.patternBackground.insets", var7);
         var11 = new ImagePainter(var2, var3, var4, var5, var6, var8, var9, var9, 1, 1);
         var11.draw();
      }

   }
}
