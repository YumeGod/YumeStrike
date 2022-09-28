package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthContext;

public class SplitPanePainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.SplitPanePainter";
   private static HashMap imgCache = new HashMap();

   protected SplitPanePainter() {
   }

   public static SplitPanePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static SplitPanePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, SplitPanePainter.class, "Synthetica.SplitPanePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, SplitPanePainter.class, "Synthetica.SplitPanePainter");
      }

      return (SplitPanePainter)var1;
   }

   public void paintSplitPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintSplitPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintSplitPaneDividerBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintSplitPaneDragDivider(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
   }

   public void paintSplitPaneDividerForeground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      JComponent var8 = var1.getComponent();
      Insets var9 = SyntheticaLookAndFeel.getInsets("Synthetica.splitPaneDivider.background.insets", var8, false);
      String var11 = null;
      String var12 = "Synthetica.splitPaneDivider";
      if (var7 == 0) {
         var11 = var12 + ".x.grip";
         var12 = var12 + ".x.background";
      } else {
         var11 = var12 + ".y.grip";
         var12 = var12 + ".y.background";
      }

      if ((var1.getComponentState() & 2) > 0) {
         var11 = var11 + ".hover";
      }

      var12 = SyntheticaLookAndFeel.getString(var12, var8);
      var11 = SyntheticaLookAndFeel.getString(var11, var8);
      int var14;
      if (var12 != null) {
         int var13 = SyntheticaLookAndFeel.getBoolean("Synthetica.splitPaneDivider.background.horizontalTiled", var8, false) ? 1 : 0;
         var14 = SyntheticaLookAndFeel.getBoolean("Synthetica.splitPaneDivider.background.verticalTiled", var8, false) ? 1 : 0;
         ImagePainter var15 = new ImagePainter(var2, var3, var4, var5, var6, var12, var9, var9, var13, var14);
         var15.draw();
      }

      if (var11 != null) {
         Image var18 = (Image)imgCache.get(var11);
         if (var18 == null) {
            var18 = (new ImageIcon(SyntheticaLookAndFeel.class.getResource(var11))).getImage();
            imgCache.put(var11, var18);
         }

         var14 = var18.getWidth((ImageObserver)null);
         int var19 = var18.getHeight((ImageObserver)null);
         int var16 = var3 + (var5 - var14) / 2;
         int var17 = var4 + (var6 - var19) / 2;
         if (var7 == 1 && var5 - 2 < var14) {
            return;
         }

         if (var7 == 0 && var6 - 2 < var19) {
            return;
         }

         var2.drawImage(var18, var16, var17, (ImageObserver)null);
      }

   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return !var1.equals("paintSplitPaneBorder") && !var1.equals("paintSplitPaneBackground") ? super.getCacheScaleType(var1) : Cacheable.ScaleType.NINE_SQUARE;
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return !var5.equals("paintSplitPaneBorder") && !var5.equals("paintSplitPaneBackground") ? super.getCacheHash(var1, var2, var3, var4, var5) : -1;
   }
}
