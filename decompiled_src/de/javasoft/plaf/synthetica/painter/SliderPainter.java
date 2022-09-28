package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.synth.SynthContext;

public class SliderPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.SliderPainter";

   protected SliderPainter() {
   }

   public static SliderPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static SliderPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, SliderPainter.class, "Synthetica.SliderPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, SliderPainter.class, "Synthetica.SliderPainter");
      }

      return (SliderPainter)var1;
   }

   public void paintSliderBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JComponent var7 = var1.getComponent();
      if (var7.hasFocus()) {
         FocusPainter.paintFocus("focus.slider", var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSliderBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintSliderTrackBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintSliderTrackBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JSlider var7 = (JSlider)var1.getComponent();
      SyntheticaPainterState var8 = new SyntheticaPainterState(var1);
      this.paintSliderTrack(var7, var8, var7.getOrientation(), var2, var3, var4, var5, var6);
      var8 = new SyntheticaPainterState(var1, 0, true);
      this.paintSliderTrack(var7, var8, var7.getOrientation(), var7.getValue(), var7.getMinimum(), var7.getMaximum(), var7.getInverted(), var2, var3, var4, var5, var6);
   }

   public void paintSliderTrack(JComponent var1, SyntheticaState var2, int var3, Graphics var4, int var5, int var6, int var7, int var8) {
      UIKey var9 = new UIKey("slider.track", var2, -1, -1, var3);
      Insets var10 = (Insets)UIKey.findProperty((JComponent)var1, var9.get(), "image.insets", true, 1);
      String var12 = SyntheticaLookAndFeel.getString(var9.get("image"), var1);
      ImagePainter var13 = new ImagePainter(var1, var4, var5, var6, var7, var8, var12, var10, var10, 0, 0);
      var13.draw();
   }

   public void paintSliderTrack(JComponent var1, SyntheticaState var2, int var3, int var4, int var5, int var6, boolean var7, Graphics var8, int var9, int var10, int var11, int var12) {
      String var13 = "slider.trackMark";
      UIKey var14 = new UIKey(var13, var2, -1, -1, var3);
      String var15 = SyntheticaLookAndFeel.getString(var14.get("image"), var1);
      if (var15 == null) {
         var13 = "slider.track";
         var14 = new UIKey(var13, var2, -1, -1, var3);
         var15 = SyntheticaLookAndFeel.getString(var14.get("image"), var1);
      }

      if (var15 != null) {
         Insets var16 = (Insets)UIKey.findProperty((JComponent)var1, var14.get(), "image.insets", true, 1);
         Insets var17 = (Insets)var16.clone();
         var14 = new UIKey(var13, var2);
         int var18 = SyntheticaLookAndFeel.getInt(var14.get("animation.cycles"), var1, 1);
         int var19 = SyntheticaLookAndFeel.getInt(var14.get("animation.delay"), var1, 50);
         int var20 = SyntheticaLookAndFeel.getInt(var14.get("animation.type"), var1, 2);
         if (var2.isSet(SyntheticaState.State.HOVER) || var2.isSet(SyntheticaState.State.PRESSED)) {
            var20 = SyntheticaLookAndFeel.getInt(var14.get("animation.type"), var1, 1);
         }

         int var21 = var4 - var5;
         int var22 = var6 == var5 ? var21 : var6 - var5;
         boolean var23 = var1.isEnabled() && this.isSliderTrackMarkEnabled(var1);
         if (var22 == 0) {
            var22 = 1;
         }

         int var24;
         int var25;
         int var26;
         if (var3 == 0) {
            var24 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.thumbWidth", var1, SyntheticaLookAndFeel.getInt("Synthetica.slider.thumb.width", var1, 10));
            var25 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.thumbX", var1, 0);
            var11 -= var24;
            if (!var7 ^ !var1.getComponentOrientation().isLeftToRight()) {
               var11 = var23 ? var25 - var9 + var24 / 2 : var11 * var21 / var22 + var24 / 2;
               var17.right = 0;
            } else {
               var26 = var23 ? var11 + var9 - var25 + var24 / 2 : var11 * var21 / var22 + var24 / 2;
               var9 += var11 - var26 + var24;
               var11 = var26;
               var17.left = 0;
            }
         } else {
            var24 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.thumbHeight", var1, SyntheticaLookAndFeel.getInt("Synthetica.slider.thumb.height", var1, 10));
            var25 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.thumbY", var1, 0);
            var12 -= var24;
            if (!var7) {
               var26 = var23 ? var12 + var10 - var25 + var24 / 2 : var12 * var21 / var22 + var24 / 2;
               var10 += var12 - var26 + var24;
               var12 = var26;
               var17.top = 0;
            } else {
               var12 = var23 ? var25 - var10 + var24 / 2 : var12 * var21 / var22 + var24 / 2;
               var17.bottom = 0;
            }
         }

         ImagePainter var27 = new ImagePainter(var1, "track", var18, var19, var20, var2.getState(), var8, var9, var10, var11, var12, var15, var16, var17, 0, 0);
         var27.draw();
      }

   }

   public void paintSliderThumbBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
   }

   public void paintSliderThumbBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      JSlider var8 = (JSlider)var1.getComponent();
      SyntheticaPainterState var9 = new SyntheticaPainterState(var1);
      this.paintSliderThumb(var8, var9, var7, var2, var3, var4, var5, var6);
   }

   public void paintSliderThumb(JComponent var1, SyntheticaState var2, int var3, Graphics var4, int var5, int var6, int var7, int var8) {
      if (this.isSliderTrackMarkEnabled(var1)) {
         int var9 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.thumbX", var1, -1);
         int var10 = (Integer)SyntheticaLookAndFeel.getClientProperty("Synthetica.thumbY", var1, -1);
         if (var9 != var5 || var10 != var6) {
            var1.putClientProperty("Synthetica.thumbX", var5);
            var1.putClientProperty("Synthetica.thumbY", var6);
            var1.putClientProperty("Synthetica.thumbWidth", var7);
            var1.putClientProperty("Synthetica.thumbHeight", var8);
            var1.repaint();
         }
      }

      UIKey var17 = new UIKey("slider.thumb", var2, -1, -1, var3);
      Insets var18 = (Insets)UIKey.findProperty((JComponent)var1, var17.get(), "image.insets", true, 1);
      String var12 = SyntheticaLookAndFeel.getString(var17.get("image"), var1);
      if (var2.isSet(SyntheticaState.State.PRESSED) && var12 == null) {
         var2.setState(SyntheticaState.State.HOVER.toInt());
         var17 = new UIKey("slider.thumb", var2, -1, -1, var3);
         var12 = SyntheticaLookAndFeel.getString(var17.get("image"), var1);
      }

      if (var12 != null) {
         var17 = new UIKey("slider.thumb", var2);
         int var13 = SyntheticaLookAndFeel.getInt(var17.get("animation.cycles"), var1, 1);
         int var14 = SyntheticaLookAndFeel.getInt(var17.get("animation.delay"), var1, 50);
         int var15 = SyntheticaLookAndFeel.getInt(var17.get("animation.type"), var1, 2);
         if (var2.isSet(SyntheticaState.State.HOVER) || var2.isSet(SyntheticaState.State.PRESSED)) {
            var15 = SyntheticaLookAndFeel.getInt(var17.get("animation.type"), var1, 1);
         }

         ImagePainter var16 = new ImagePainter(var1, "thumb", var13, var14, var15, var2.getState(), var4, var5, var6, var7, var8, var12, var18, var18, 0, 0);
         var16.draw();
      }

      if (var1.hasFocus() || var2.isSet(SyntheticaState.State.FOCUSED)) {
         String var19 = null;
         if (SyntheticaLookAndFeel.get("Synthetica.focus.slider.thumb.x", (Component)var1) != null && ((JSlider)var1).getOrientation() == 0) {
            var19 = "focus.slider.thumb.x";
         } else if (SyntheticaLookAndFeel.get("Synthetica.focus.slider.thumb.y", (Component)var1) != null && ((JSlider)var1).getOrientation() == 1) {
            var19 = "focus.slider.thumb.y";
         } else if (SyntheticaLookAndFeel.get("Synthetica.focus.slider.thumb", (Component)var1) != null) {
            var19 = "focus.slider.thumb";
         }

         if (var19 != null) {
            FocusPainter.paintFocus(var19, var1, var2.getState(), "", var4, var5, var6, var7, var8);
         }
      }

   }

   private boolean isSliderTrackMarkEnabled(JComponent var1) {
      return SyntheticaLookAndFeel.getBoolean("Synthetica.slider.hoverAndPressed.enabled", var1, false) || SyntheticaLookAndFeel.getBoolean("Synthetica.slider.trackMark.enabled", var1, false);
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      if (var5.equals("paintSliderBackground")) {
         return -1;
      } else {
         JSlider var6 = (JSlider)var1.getComponent();
         int var7 = var6.getOrientation();
         int var8 = super.getCacheHash(var1, var2, var3, var4, var5);
         var8 = 31 * var8 + var7;
         return var8;
      }
   }
}
