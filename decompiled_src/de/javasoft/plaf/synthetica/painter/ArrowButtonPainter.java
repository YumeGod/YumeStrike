package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.plaf.synth.SynthContext;

public class ArrowButtonPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ArrowButtonPainter";

   protected ArrowButtonPainter() {
   }

   public static ArrowButtonPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ArrowButtonPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ArrowButtonPainter.class, "Synthetica.ArrowButtonPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ArrowButtonPainter.class, "Synthetica.ArrowButtonPainter");
      }

      return (ArrowButtonPainter)var1;
   }

   public void paintArrowButtonBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintArrowButtonBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JButton var7 = (JButton)var1.getComponent();
      Container var8 = var7.getParent();
      String var9 = var7.getName();
      int var10 = 0;
      if (var8 instanceof JComboBox) {
         var10 = ((JComboBox)var8).isEditable() ? 0 : SyntheticaState.State.LOCKED.toInt();
      }

      SyntheticaPainterState var11 = new SyntheticaPainterState(var1, var10, false);
      if (var9 == null || !var9.startsWith("SplitPaneDivider.")) {
         int var12 = (Integer)var7.getClientProperty("__arrow_direction__");
         boolean var13 = true;
         boolean var14 = (var12 == 5 || var12 == 1) && !var7.getComponentOrientation().isLeftToRight();
         if (var8 instanceof JComboBox || var9 != null && var9.startsWith("ComboBox.arrowButton")) {
            if (SyntheticaLookAndFeel.getBoolean("Synthetica.comboBox.hoverAndPressed.enabled", var8)) {
               var11 = new SyntheticaPainterState(var1, var10, false, true);
            }

            var12 = 7;
         } else if (var8 instanceof JSpinner || "Spinner.nextButton".equals(var9) || "Spinner.previousButton".equals(var9)) {
            if (SyntheticaLookAndFeel.getBoolean("Synthetica.spinner.hoverAndPressed.enabled", var8)) {
               var11 = new SyntheticaPainterState(var1, var10, false, false);
            }

            var12 = 7;
         }

         Insets var15 = null;
         UIKey var16 = new UIKey("arrowButton.y.background", var11);
         if (var13) {
            if (var5 > 12 && var6 > 12) {
               if (var12 == 3 || var12 == 7) {
                  var16 = new UIKey("arrowButton.x.background", var11);
               }
            } else {
               var16 = new UIKey("arrowButton.8x8.background", var11);
            }

            var15 = (Insets)var16.findProperty(var1, "insets", true, 2);
         }

         String var17 = (String)var16.findProperty(var1, (String)null, true, 1);
         int var19 = SyntheticaLookAndFeel.getInt(var16.get("animation.cycles"), var7, 1);
         int var20 = SyntheticaLookAndFeel.getInt(var16.get("animation.delay"), var7, 50);
         int var21 = SyntheticaLookAndFeel.getInt(var16.get("animation.type"), var7, 2);
         if (var11.isSet(SyntheticaState.State.HOVER)) {
            var21 = SyntheticaLookAndFeel.getInt(var16.get("animation.type"), var7, 1);
         }

         ImagePainter var22 = new ImagePainter(var7, (String)null, var19, var20, var21, var11.getState(), var2, var3, var4, var5, var6, var17, var15, var15, 0, 0, var14, false);
         var22.draw();
      }
   }

   public void paintArrowButtonForeground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6, int var7) {
      JComponent var8 = var1.getComponent();
      SyntheticaPainterState var9 = new SyntheticaPainterState(var1);
      UIKey var10 = new UIKey("arrow", var9, var7, -1, -1);
      boolean var11 = (var7 == 5 || var7 == 1) && !var8.getComponentOrientation().isLeftToRight();
      if (var5 >= 16 && var6 >= 16) {
         if (var5 != 16 || var6 != 16) {
            var3 += var11 ? (var5 - 16) / 2 : (var5 - 16 + 1) / 2;
            var4 += (var6 - 16 + 1) / 2;
            var5 = 16;
            var6 = 16;
         }
      } else {
         var10 = new UIKey("arrow.8x8", var9, var7, -1, -1);
         var3 += (var5 - 8 + 1) / 2;
         var4 += (var6 - 8 + 1) / 2;
         var5 = 8;
         var6 = 8;
      }

      var3 += var8.getClientProperty("Synthetica.arrow.xOffset") == null ? 0 : (Integer)var8.getClientProperty("Synthetica.arrow.xOffset");
      var4 += var8.getClientProperty("Synthetica.arrow.yOffset") == null ? 0 : (Integer)var8.getClientProperty("Synthetica.arrow.yOffset");
      Insets var12 = new Insets(0, 0, 0, 0);
      Insets var13 = new Insets(0, 0, 0, 0);
      String var14 = SyntheticaLookAndFeel.getString(var10.get(), var8);
      if (var14 != null) {
         ImagePainter var15 = new ImagePainter(var8, (String)null, -1, -1, -1, -1, var2, var3, var4, var5, var6, var14, var12, var13, 0, 0, var11, false);
         var15.draw();
      }

   }
}
