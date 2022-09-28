package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.SynthContext;

public class PanelPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.PanelPainter";

   protected PanelPainter() {
   }

   public static PanelPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static PanelPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, PanelPainter.class, "Synthetica.PanelPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, PanelPainter.class, "Synthetica.PanelPainter");
      }

      return (PanelPainter)var1;
   }

   public void paintPanelBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintPanelBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      this.paintPanelBackground(var1.getComponent(), new SyntheticaState(), var2, var3, var4, var5, var6);
   }

   public void paintPanelBackground(JComponent var1, SyntheticaState var2, Graphics var3, int var4, int var5, int var6, int var7) {
      if (var1.isOpaque() || SyntheticaLookAndFeel.getBoolean("Synthetica.panel.background.opaque", var1, false)) {
         Color var8 = var1.getBackground();
         String var9 = SyntheticaLookAndFeel.getString("Synthetica.panel.inactive.background.image", var1);
         boolean var10 = false;
         if (var9 != null) {
            Window var11 = SwingUtilities.getWindowAncestor(var1);
            var10 = var11 != null && !var11.isActive();
         }

         String var28 = var10 ? var9 : SyntheticaLookAndFeel.getString("Synthetica.panel.background.image", var1);
         boolean var12 = SyntheticaLookAndFeel.getBoolean("Synthetica.panel.background.image.enabled", var1, true) && !(var1.getParent() instanceof CellRendererPane);
         if (var8 == null || var8 instanceof ColorUIResource || var12 && var28 != null && !this.isInternalFrameContentPane(var1)) {
            if (var12 && var28 != null && (var8 == null || var8 != null && var8 instanceof ColorUIResource)) {
               int var13 = SyntheticaLookAndFeel.getBoolean("Synthetica.panel.background.horizontalTiled", var1, false) ? 1 : 0;
               int var14 = SyntheticaLookAndFeel.getBoolean("Synthetica.panel.background.verticalTiled", var1, false) ? 1 : 0;
               Insets var15 = SyntheticaLookAndFeel.getInsets("Synthetica.panel.background.image.insets", var1, false);
               String var17 = SyntheticaLookAndFeel.getString("Synthetica.panel.background.image.origin", var1);
               if (var17 != null && var17.equals("PANEL")) {
                  ImagePainter var29 = new ImagePainter(var3, var4, var5, var6, var7, var28, var15, var15, var13, var14);
                  var29.draw();
               } else {
                  Object var18 = var17 != null && var17.equals("CONTENT_PANE") ? var1.getRootPane().getContentPane() : var1.getRootPane();
                  if (var18 == null) {
                     return;
                  }

                  Rectangle var19 = ((Container)var18).getBounds();
                  var19.width = Math.max(var19.width, SyntheticaLookAndFeel.getInt("Synthetica.panel.minimumBackgroundWidth", var1, var19.width));
                  var19.height = Math.max(var19.height, SyntheticaLookAndFeel.getInt("Synthetica.panel.minimumBackgroundHeight", var1, var19.height));
                  boolean var20 = (Boolean)SyntheticaLookAndFeel.getClientProperty("Synthetica.panel.paintViewportAware", var1, true);
                  Object var21 = var20 ? this.findRelevantParent(var1) : var1;
                  Rectangle var22 = SwingUtilities.convertRectangle((Component)var21, new Rectangle(var4, var5, var6, var7), (Component)var18);
                  if (var21 instanceof JViewport) {
                     JViewport var23 = (JViewport)var21;
                     Point var24 = SwingUtilities.convertPoint(var1, new Point(), var23);
                     Point var25 = var23.getViewPosition();
                     var22.x += var24.x + var25.x;
                     var22.y += var24.y + var25.y;
                     Point var26 = SwingUtilities.convertPoint(var23, new Point(), (Component)var18);
                     Dimension var27 = var23.getViewSize();
                     var19.width = var27.width + var26.x;
                     var19.height = var27.height + var26.y;
                  }

                  ImagePainter var30 = new ImagePainter(var3, var4 - var22.x, var5 - var22.y, var19.width, var19.height, var28, var15, var15, var13, var14);
                  var30.draw();
               }
            }
         } else {
            var3.setColor(var8);
            var3.fillRect(var4, var5, var6, var7);
         }
      }

   }

   private boolean isInternalFrameContentPane(JComponent var1) {
      return SwingUtilities.getAncestorOfClass(JInternalFrame.class, var1) != null;
   }

   private Component findRelevantParent(Component var1) {
      Container var2 = null;

      for(Container var3 = var1.getParent(); var3 != null; var3 = var3.getParent()) {
         if (var3 instanceof JViewport) {
            var2 = var3;
         }
      }

      return (Component)(var2 == null ? var1 : var2);
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return -1;
   }
}
