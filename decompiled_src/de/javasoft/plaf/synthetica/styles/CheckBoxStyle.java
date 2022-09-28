package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.GraphicsUtils;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.util.java2d.Synthetica2DUtils;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class CheckBoxStyle extends StyleWrapper {
   private static CheckBoxStyle instance = new CheckBoxStyle();

   private CheckBoxStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         CheckBoxStyle var3 = new CheckBoxStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Icon getIcon(SynthContext var1, Object var2) {
      AbstractButton var3 = (AbstractButton)var1.getComponent();
      if (var3.getIcon() != null) {
         return null;
      } else {
         int var4 = var1.getComponentState();
         Boolean var5 = (Boolean)var3.getClientProperty("Synthetica.MOUSE_OVER");
         boolean var6 = var5 == null ? false : var5;
         Object var7 = null;
         BufferedImage var10;
         if ((var4 & 4) > 0) {
            boolean var8 = SyntheticaLookAndFeel.getBoolean("Synthetica.checkBox.emulatedPressedState.enabled", var3);
            boolean var9 = var3.isSelected();
            if (var8) {
               var7 = SyntheticaLookAndFeel.loadIcon(var9 ? "Synthetica.checkBox.selected.image" : "Synthetica.checkBox.image");
            }

            if (var7 == null) {
               var7 = this.synthStyle.getIcon(new SynthContext(var3, var1.getRegion(), var1.getStyle(), var9 ? 516 : 4), var2);
            }

            if (var8) {
               var10 = (BufferedImage)GraphicsUtils.iconToImage(var1, (Icon)var7);
               Color var11 = SyntheticaLookAndFeel.getColor("Synthetica.checkBox.emulatedPressedState.color", var3, UIManager.getColor("Panel.background").darker());
               float var12 = (float)SyntheticaLookAndFeel.getInt("Synthetica.checkBox.emulatedPressedState.alpha", var3, 25) / 100.0F;
               BufferedImage var13 = Synthetica2DUtils.createColorizedImage(var10, var11, var12);
               var7 = new ImageIcon(var13);
            }
         } else if ((var4 & 512) > 0 && var6) {
            var7 = this.synthStyle.getIcon(new SynthContext(var3, var1.getRegion(), var1.getStyle(), var4 | 2), var2);
         } else {
            var7 = this.synthStyle.getIcon(var1, var2);
         }

         if (var3.getClientProperty("Synthetica.background") != null) {
            float var14 = var3.getClientProperty("Synthetica.background.alpha") == null ? 0.1F : (Float)var3.getClientProperty("Synthetica.background.alpha");
            BufferedImage var15 = (BufferedImage)GraphicsUtils.iconToImage(var1, (Icon)var7);
            var10 = Synthetica2DUtils.createColorizedImage(var15, (Color)var3.getClientProperty("Synthetica.background"), var14);
            var7 = new ImageIcon(var10);
         }

         return (Icon)var7;
      }
   }
}
