package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;

public class SpinnerPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.SpinnerPainter";

   protected SpinnerPainter() {
   }

   public static SpinnerPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static SpinnerPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, SpinnerPainter.class, "Synthetica.SpinnerPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, SpinnerPainter.class, "Synthetica.SpinnerPainter");
      }

      return (SpinnerPainter)var1;
   }

   public void paintSpinnerBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JSpinner var7 = (JSpinner)var1.getComponent();
      if (this.hasFocus(var7)) {
         FocusPainter.paintFocus("focus.spinner", var1, var2, var3, var4, var5, var6);
      }

   }

   public void paintSpinnerBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JSpinner var7 = (JSpinner)var1.getComponent();
      var7.putClientProperty("Synthetica.flipHorizontal", !var7.getComponentOrientation().isLeftToRight());
      Color var8 = var7.getBackground();
      boolean var9 = var8 == null || var8 instanceof ColorUIResource;
      boolean var10 = SyntheticaLookAndFeel.getBoolean("Synthetica.spinner.noBorderIfColored", var7);
      boolean var11 = SyntheticaLookAndFeel.isOpaque(var7);
      if (var11) {
         if (var9) {
            var8 = var1.getStyle().getColor(var1, ColorType.BACKGROUND);
         }

         SyntheticaPainterState var12 = new SyntheticaPainterState(var1, 0, true);
         UIKey var13 = new UIKey("spinner", var12);
         Boolean var14 = (Boolean)SyntheticaLookAndFeel.get(var13.get("fillBackground"), (Component)var7);
         Border var15 = var7.getBorder();
         Border var16 = SyntheticaLookAndFeel.findDefaultBorder(var15);
         boolean var17 = var15 instanceof CompoundBorder && ((CompoundBorder)var15).getInsideBorder() == var16;
         if (var17) {
            Insets var18 = var15.getBorderInsets(var7);
            Insets var19 = var16.getBorderInsets(var7);
            var3 += var18.left - var19.left;
            var4 += var18.top - var19.top;
            var5 -= var18.left - var19.left + var18.right - var19.right;
            var6 -= var18.top - var19.top + var18.bottom - var19.bottom;
         }

         if (var14 == null || var14) {
            this.fillBackground(var7, var15, var16, var2, var8, var3, var4, var5, var6);
         }

         if (var9 || !var10) {
            SyntheticaState var25 = new SyntheticaState(var12.getState());
            var25.resetState(SyntheticaState.State.HOVER);
            var25.resetState(SyntheticaState.State.PRESSED);
            var13 = new UIKey("spinner.border", var25);
            String var26 = var13.get();
            boolean var20 = SyntheticaLookAndFeel.get(var26, (Component)var7) != null;
            if (var26.equals("Synthetica.spinner.border.disabled") && !var20) {
               var13 = new UIKey("textField.border", var25);
            }

            String var21 = (String)var13.findProperty(var1, (String)null, true, 1);
            if (this.hasFocus(var7) && SyntheticaLookAndFeel.get(var13.get() + ".focused", (Component)var7) != null) {
               var21 = SyntheticaLookAndFeel.getString(var13.get() + ".focused", var7);
            }

            Insets var22 = SyntheticaLookAndFeel.getInsets("Synthetica.spinner.border.insets", var7);
            Insets var23 = SyntheticaLookAndFeel.get("Synthetica.spinner.border.dInsets", (Component)var7) == null ? var22 : SyntheticaLookAndFeel.getInsets("Synthetica.spinner.border.dInsets", var7);
            if (var16 == null) {
               Insets var24 = var7.getInsets();
               var3 += var24.left;
               var4 += var24.top;
               var5 += -var24.left - var24.right;
               var6 += -var24.top - var24.bottom;
               var23 = new Insets(0, 0, 0, 0);
            }

            ImagePainter var27 = new ImagePainter(var7, var2, var3, var4, var5, var6, var21, var22, var23, 0, 0);
            if (!var9) {
               var27.drawBorder();
            } else if (var16 == null) {
               var27.drawCenter();
            } else {
               var27.draw();
            }

            if (SyntheticaLookAndFeel.getBoolean("Synthetica.spinner.hoverAndPressed.enabled", var7)) {
               this.paintHoverPressed(var7, var12, var15, var16, var17, var2, var3, var4, var5, var6, var22, var23);
            }

         }
      }
   }

   private void paintHoverPressed(JSpinner var1, SyntheticaPainterState var2, Border var3, Border var4, boolean var5, Graphics var6, int var7, int var8, int var9, int var10, Insets var11, Insets var12) {
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.spinner.stateChange4ButtonOnly", var1)) {
         boolean var13 = var1.getComponentOrientation().isLeftToRight();
         if (var11.equals(var12)) {
            var12 = new Insets(var11.top, 0, var11.bottom, var11.right);
         }

         int var14 = SyntheticaLookAndFeel.findComponent((String)"Spinner.nextButton", var1).getWidth();
         Insets var15 = var5 ? var4.getBorderInsets(var1) : var1.getInsets();
         int var16 = SyntheticaLookAndFeel.getInt("Synthetica.spinner.buttons.relativeX", var1, 0);
         if (var4 != null) {
            var7 += var13 ? var9 - var15.right - var14 + var16 : 0;
            var9 = var14 + (var13 ? var15.right : var15.left) - var16;
         } else {
            var7 += var13 ? var9 - var14 + var16 : 0;
            var9 = var14 - var16;
         }
      }

      String var21 = "spinner.border";
      if (SyntheticaLookAndFeel.getBoolean("Synthetica.spinner.stateChange4SingleButton", var1)) {
         JButton var22 = (JButton)SyntheticaLookAndFeel.findComponent((String)"Spinner.nextButton", var1);
         if (var22.getModel().isPressed() || var22.getModel().isRollover()) {
            var21 = var21 + ".nextButton";
            if (var22.getModel().isPressed()) {
               var2.setState(SyntheticaState.State.PRESSED);
            }
         }

         var22 = (JButton)SyntheticaLookAndFeel.findComponent((String)"Spinner.previousButton", var1);
         if (var22.getModel().isPressed() || var22.getModel().isRollover()) {
            var21 = var21 + ".previousButton";
            if (var22.getModel().isPressed()) {
               var2.setState(SyntheticaState.State.PRESSED);
            }
         }
      }

      UIKey var23 = new UIKey(var21, var2);
      String var24 = (String)UIKey.findProperty((JComponent)var1, var23.get(), (String)null, true, 1);
      UIKey var25 = new UIKey("spinner", var2);
      int var17 = SyntheticaLookAndFeel.getInt(var25.get("animation.cycles"), var1, 1);
      int var18 = SyntheticaLookAndFeel.getInt(var25.get("animation.delay"), var1, 50);
      int var19 = SyntheticaLookAndFeel.getInt(var25.get("animation.type"), var1, 2);
      if (var2.isSet(SyntheticaState.State.HOVER) || var2.isSet(SyntheticaState.State.PRESSED)) {
         var19 = SyntheticaLookAndFeel.getInt(var25.get("animation.type"), var1, 1);
      }

      ImagePainter var20 = new ImagePainter(var1, var17, var18, var19, var2.getState(), var6, var7, var8, var9, var10, var24, var11, var12, 0, 0);
      if (var4 == null) {
         var20.drawCenter();
      } else {
         var20.draw();
      }

   }

   protected boolean hasFocus(JComponent var1) {
      if (var1.isEnabled() && var1.isFocusable()) {
         Component var2 = SyntheticaLookAndFeel.findComponent((String)"Spinner.formattedTextField", var1);
         if (var1.hasFocus() || var2 != null && var2.hasFocus()) {
            return true;
         }
      }

      return false;
   }

   private void fillBackground(JSpinner var1, Border var2, Border var3, Graphics var4, Color var5, int var6, int var7, int var8, int var9) {
      Color var10 = var4.getColor();
      var4.setColor(var5);
      Insets var11 = var1.getInsets();
      if (var2 != null && !var11.equals(new Insets(0, 0, 0, 0))) {
         if (var3 == null) {
            var4.fillRect(var6 + var11.left, var7 + var11.top, var8 - var11.left - var11.right, var9 - var11.top - var11.bottom);
         } else {
            Insets var12 = SyntheticaLookAndFeel.getInsets("Synthetica.spinner.border.fillInsets", var1, false);
            int var13 = SyntheticaLookAndFeel.getInt("Synthetica.spinner.border.arcWidth", var1, 8);
            int var14 = SyntheticaLookAndFeel.getInt("Synthetica.spinner.border.arcHeight", var1, 8);
            Graphics2D var15 = (Graphics2D)var4;
            RenderingHints var16 = var15.getRenderingHints();
            var15.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            var15.fillRoundRect(var6 + var12.left, var7 + var12.top, var8 - var12.left - var12.right, var9 - var12.top - var12.bottom, var13, var14);
            var15.setRenderingHints(var16);
         }
      } else {
         var4.fillRect(var6, var7, var8, var9);
      }

      var4.setColor(var10);
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      JComponent var6 = var1.getComponent();
      int var7 = super.getCacheHash(var1, var2, var3, var4, var5);
      var7 = var7 * 31 + (this.hasFocus(var6) ? 0 : 1);
      return var7;
   }
}
