package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class ProgressBarPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ProgressBarPainter";
   private static HashMap imgCache = new HashMap();

   protected ProgressBarPainter() {
   }

   public static ProgressBarPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ProgressBarPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ProgressBarPainter.class, "Synthetica.ProgressBarPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ProgressBarPainter.class, "Synthetica.ProgressBarPainter");
      }

      return (ProgressBarPainter)var1;
   }

   public void paintProgressBarBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintProgressBarBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JProgressBar var7 = (JProgressBar)var1.getComponent();
      Insets var8 = var1.getStyle().getInsets(var1, (Insets)null);
      Insets var9 = var7.getInsets();
      var5 -= var9.left + var9.right - var8.left - var8.right;
      var6 -= var9.top + var9.bottom - var8.top - var8.bottom;
      var3 += var9.left - var8.left;
      var4 += var9.top - var8.top;
      int var10 = var7.getValue() <= 0 && !var7.isIndeterminate() ? 0 : SyntheticaState.State.ACTIVE.toInt();
      SyntheticaPainterState var11 = new SyntheticaPainterState(var1, var10, false);
      UIKey var12 = new UIKey("progressBar.x.background", var11);
      if (var7.getOrientation() == 1) {
         var12 = new UIKey("progressBar.y.background", var11);
      }

      Insets var13 = (Insets)var12.findProperty(var1, "insets", true, 2);
      String var15 = (String)var12.findProperty(var1, (String)null, true, 1);
      var12 = new UIKey("progressBar.background", var11);
      if (var15 != null) {
         int var16 = SyntheticaLookAndFeel.getInt(var12.get("animation.cycles"), var7, 1);
         int var17 = SyntheticaLookAndFeel.getInt(var12.get("animation.delay"), var7, 50);
         int var18 = SyntheticaLookAndFeel.getInt(var12.get("animation.type"), var7, 6);
         if (var11.isSet(SyntheticaState.State.ACTIVE)) {
            var18 = SyntheticaLookAndFeel.getInt(var12.get("animation.type"), var7, 5);
         }

         ImagePainter var19 = new ImagePainter(var7, var16, var17, var18, var11.getState(), var2, var3, var4, var5, var6, var15, var13, var13, 0, 0);
         var19.draw();
      }

   }

   public void paintProgressBarForeground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      Graphics2D var8 = (Graphics2D)var2;
      JProgressBar var9 = (JProgressBar)var1.getComponent();
      SyntheticaPainterState var10 = new SyntheticaPainterState(var1);
      UIKey var11 = new UIKey(var9.isIndeterminate() ? "progressBar.x.indeterminate" : "progressBar.x", var10);
      if (var7 == 1) {
         var11 = new UIKey(var9.isIndeterminate() ? "progressBar.y.indeterminate" : "progressBar.y", var10);
      }

      Insets var12 = (Insets)var11.findProperty(var1, "insets", true, 2);
      if (var12 == null) {
         var12 = new Insets(0, 0, 0, 0);
      }

      boolean var14 = SyntheticaLookAndFeel.getBoolean("Synthetica.progressBar.respectMinimumBarImageSize", var9, true);
      int var15;
      if (var7 == 0) {
         var15 = var14 ? var12.left + var12.right : 1;
         if (var5 < var15) {
            return;
         }
      } else {
         var15 = var14 ? var12.top + var12.bottom : 1;
         if (var6 < var15) {
            return;
         }
      }

      var15 = var5;
      int var16 = var6;
      int var17 = SyntheticaLookAndFeel.getBoolean("Synthetica.progressBar.tiled", var9) ? 1 : 0;
      String var18 = (String)var11.findProperty(var1, (String)null, true, 2);
      int var20;
      int var21;
      if (!SyntheticaLookAndFeel.getBoolean("Synthetica.progressBar.continuous", var9)) {
         Image var19 = (Image)imgCache.get(var18);
         if (var19 == null) {
            var19 = (new ImageIcon(SyntheticaLookAndFeel.class.getResource(var18))).getImage();
            imgCache.put(var18, var19);
         }

         var20 = var19.getWidth((ImageObserver)null) - var12.left - var12.right;
         var21 = var19.getHeight((ImageObserver)null) - var12.top - var12.bottom;
         var15 = (var5 - var12.left - var12.right) / var20 * var20 + var12.left + var12.right;
         var16 = (var6 - var12.top - var12.bottom) / var21 * var21 + var12.top + var12.bottom;
         var17 = 1;
      }

      int var30 = SyntheticaLookAndFeel.getInt(var11.get("animation.cycles"), var9, -1);
      var20 = SyntheticaLookAndFeel.getInt(var11.get("animation.delay"), var9, 70);
      var21 = SyntheticaLookAndFeel.getInt(var11.get("animation.type"), var9, 0);
      RenderingHints var22 = var8.getRenderingHints();
      var8.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      ImagePainter var23 = null;
      if (var7 == 0) {
         var23 = new ImagePainter(var9, var30, var20, var21, var10.getState(), var2, var3, var4, var15, var6, var18, var12, var12, var17, 0);
         var23.draw();
      } else {
         var23 = new ImagePainter(var9, var30, var20, var21, var10.getState(), var2, var3, var4 - var16 + var6, var5, var16, var18, var12, var12, 0, var17);
         var23.draw();
      }

      var8.setRenderingHints(var22);
      if (var9.isStringPainted() && var9.isIndeterminate() && JAVA5) {
         SynthStyle var24 = var1.getStyle();
         Font var25 = var24.getFont(var1);
         var2.setFont(var25);
         var2.setColor(var24.getColor(var1, ColorType.TEXT_FOREGROUND));
         FontMetrics var26 = var2.getFontMetrics();
         Rectangle var27 = var9.getBounds();
         int var28 = var24.getGraphicsUtils(var1).computeStringWidth(var1, var25, var26, var9.getString());
         Point var29 = new Point(var27.width / 2 - var28 / 2, (var27.height - (var26.getAscent() + var26.getDescent())) / 2);
         var24.getGraphicsUtils(var1).paintText(var1, var2, var9.getString(), var29.x, var29.y, -1);
      }

   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      if (var5.equals("paintProgressBarForeground")) {
         return -1;
      } else {
         JProgressBar var6 = (JProgressBar)var1.getComponent();
         int var7 = var6.getOrientation();
         int var8 = super.getCacheHash(var1, var2, var3, var4, var5);
         var8 = 31 * var8 + var7;
         return var8;
      }
   }
}
