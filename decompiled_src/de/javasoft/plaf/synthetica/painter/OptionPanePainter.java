package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.SynthContext;

public class OptionPanePainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.OptionPanePainter";

   protected OptionPanePainter() {
   }

   public static OptionPanePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static OptionPanePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, OptionPanePainter.class, "Synthetica.OptionPanePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, OptionPanePainter.class, "Synthetica.OptionPanePainter");
      }

      return (OptionPanePainter)var1;
   }

   public void paintOptionPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintOptionPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      this.paintPanelBackground(var1.getComponent(), new SyntheticaState(), var2, var3, var4, var5, var6);
   }

   public void paintPanelBackground(JComponent var1, SyntheticaState var2, Graphics var3, int var4, int var5, int var6, int var7) {
      Color var8 = var1.getBackground();
      if (var1.isOpaque() || SyntheticaLookAndFeel.getBoolean("Synthetica.optionPane.background.opaque", var1, false)) {
         String var9 = SyntheticaLookAndFeel.getString("Synthetica.optionPane.inactive.background.image", var1);
         boolean var10 = false;
         if (var9 != null) {
            Window var11 = SwingUtilities.getWindowAncestor(var1);
            var10 = var11 != null && !var11.isActive();
         }

         String var22 = var10 ? var9 : SyntheticaLookAndFeel.getString("Synthetica.optionPane.background.image", var1);
         boolean var12 = SyntheticaLookAndFeel.getBoolean("Synthetica.optionPane.background.image.enabled", var1, true) && !(var1.getParent() instanceof CellRendererPane);
         if ((!var12 || var22 == null) && var8 != null && !(var8 instanceof ColorUIResource)) {
            var3.setColor(var8);
            var3.fillRect(var4, var5, var6, var7);
         } else if (var12 && var22 != null && (var8 == null || var8 != null && var8 instanceof ColorUIResource)) {
            int var13 = SyntheticaLookAndFeel.getBoolean("Synthetica.optionPane.background.horizontalTiled", var1, false) ? 1 : 0;
            int var14 = SyntheticaLookAndFeel.getBoolean("Synthetica.optionPane.background.verticalTiled", var1, false) ? 1 : 0;
            Insets var15 = SyntheticaLookAndFeel.getInsets("Synthetica.optionPane.background.image.insets", var1, false);
            String var17 = SyntheticaLookAndFeel.getString("Synthetica.optionPane.background.image.origin", var1);
            if (var17 != null && var17.equals("PANEL")) {
               ImagePainter var23 = new ImagePainter(var3, var4, var5, var6, var7, var22, var15, var15, var13, var14);
               var23.draw();
            } else {
               Object var18 = var17 != null && var17.equals("CONTENT_PANE") ? var1.getRootPane().getContentPane() : var1.getRootPane();
               if (var18 == null) {
                  return;
               }

               Rectangle var19 = ((Container)var18).getBounds();
               var19.width = Math.max(var19.width, SyntheticaLookAndFeel.getInt("Synthetica.optionPane.minimumBackgroundWidth", var1, var19.width));
               var19.height = Math.max(var19.height, SyntheticaLookAndFeel.getInt("Synthetica.optionPane.minimumBackgroundHeight", var1, var19.height));
               Rectangle var20 = SwingUtilities.convertRectangle(var1, new Rectangle(var4, var5, var6, var7), (Component)var18);
               ImagePainter var21 = new ImagePainter(var3, var4 - var20.x, var5 - var20.y, var19.width, var19.height, var22, var15, var15, var13, var14);
               var21.draw();
            }
         }
      }

   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return -1;
   }
}
