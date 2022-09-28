package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JRadioButton;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.text.View;

public class RadioButtonPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.RadioButtonPainter";

   protected RadioButtonPainter() {
   }

   public static RadioButtonPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static RadioButtonPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, RadioButtonPainter.class, "Synthetica.RadioButtonPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, RadioButtonPainter.class, "Synthetica.RadioButtonPainter");
      }

      return (RadioButtonPainter)var1;
   }

   public void paintRadioButtonBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      AbstractButton var7 = (AbstractButton)var1.getComponent();
      SyntheticaPainterState var8 = new SyntheticaPainterState(var1);
      if (SyntheticaLookAndFeel.get("Synthetica.radioButton.image", (Component)var7) != null && var7.getIcon() == null) {
         UIKey var9 = new UIKey("radioButton", var8);
         Insets var10 = new Insets(0, 0, 0, 0);
         Insets var12 = var7.getInsets();
         String var13 = SyntheticaLookAndFeel.getString(var9.get("image"), var7);
         if (var7.hasFocus() && SyntheticaLookAndFeel.get(var9.get() + ".focused.image", (Component)var7) != null) {
            var13 = SyntheticaLookAndFeel.getString(var9.get() + ".focused.image", var7);
         }

         int var14 = SyntheticaLookAndFeel.getInt(var9.get("animation.cycles"), var7, 1);
         int var15 = SyntheticaLookAndFeel.getInt(var9.get("animation.delay"), var7, 50);
         int var16 = SyntheticaLookAndFeel.getInt(var9.get("animation.type"), var7, 2);
         if (var8.isSet(SyntheticaState.State.HOVER) || var8.isSet(SyntheticaState.State.PRESSED)) {
            var16 = SyntheticaLookAndFeel.getInt(var9.get("animation.type"), var7, 1);
         }

         Rectangle var17 = new Rectangle(var3 + var12.left, var4 + var12.top, var5 - var12.left - var12.right, var6 - var12.top - var12.bottom);
         Rectangle var18 = new Rectangle();
         Rectangle var19 = new Rectangle();
         Icon var20 = var1.getStyle().getIcon(var1, "RadioButton.icon");
         var1.getStyle().getGraphicsUtils(var1).layoutText(var1, var2.getFontMetrics(), var7.getText(), var20, var7.getHorizontalAlignment(), var7.getVerticalAlignment(), var7.getHorizontalTextPosition(), var7.getVerticalTextPosition(), var17, var19, var18, var7.getIconTextGap());
         ImagePainter var21 = new ImagePainter(var7, var14, var15, var16, var8.getState(), var2, var19.x, var19.y, var19.width, var19.height, var13, var10, var10, 0, 0);
         if (!var8.isSet(SyntheticaState.State.PRESSED) || var7.getPressedIcon() == null) {
            var21.drawCenter();
         }

         if (var8.isSet(SyntheticaState.State.FOCUSED) && var7.isFocusPainted() && SyntheticaLookAndFeel.get("Synthetica.radioButton.focus", (Component)var7) != null) {
            String var22 = "focus.radioButton";
            String var23 = SyntheticaLookAndFeel.getStyleName(var7);
            var14 = SyntheticaLookAndFeel.getInt(var22, "animation.cycles", var23, true, -1);
            var15 = SyntheticaLookAndFeel.getInt(var22, "animation.delay", var23, true, 60);
            var16 = SyntheticaLookAndFeel.getInt(var22, "animation.type", var23, true, 3);
            var13 = SyntheticaLookAndFeel.getString("Synthetica.radioButton.focus", var7);
            var21 = new ImagePainter(var7, "", var14, var15, var16, var8.getState(), var2, var19.x, var19.y, var19.width, var19.height, var13, var10, var10, 0, 0);
            var21.drawCenter();
         }
      }

      if (var8.isSet(SyntheticaState.State.FOCUSED) && var7.isFocusPainted()) {
         Icon var24 = var7.getIcon() == null ? var1.getStyle().getIcon(var1, "RadioButton.icon") : var7.getIcon();
         String var25 = var7.getText();
         int var11 = var25 != null && var25.length() != 0 ? var7.getIconTextGap() : 0;
         int var26 = var7.getInsets().left + var7.getInsets().right + var11 + (var24 == null ? 0 : var24.getIconWidth());
         boolean var27 = var7.getClientProperty("html") != null;
         if (!var27) {
            FontMetrics var28 = var7.getFontMetrics(var7.getFont());
            if (var25 != null) {
               var26 += var28.stringWidth(var25);
            }
         } else {
            var26 = (int)((float)var26 + ((View)var7.getClientProperty("html")).getPreferredSpan(0));
         }

         FocusPainter.paintFocus("focus.radioButton", var1, var2, var3, var4, var26, var6);
      }

   }

   public void paintRadioButtonBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JRadioButton var7 = (JRadioButton)var1.getComponent();
      if (var7.isBorderPainted()) {
         Insets var8 = SyntheticaLookAndFeel.getInsets("Synthetica.radioButton.border.insets", var7);
         String var10 = SyntheticaLookAndFeel.getString("Synthetica.radioButton.border", var7);
         ImagePainter var11 = new ImagePainter(var2, var3, var4, var5, var6, var10, var8, var8, 0, 0);
         var11.drawBorder();
      }

   }
}
