package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class ButtonStyle extends StyleWrapper {
   private static ButtonStyle instance = new ButtonStyle();

   private ButtonStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         ButtonStyle var3 = new ButtonStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      JComponent var3 = var1.getComponent();
      return var2 == ColorType.TEXT_FOREGROUND && !(var3.getForeground() instanceof ColorUIResource) ? var3.getForeground() : super.getColor(var1, var2);
   }

   public Font getFont(SynthContext var1) {
      Object var2 = this.synthStyle.getFont(var1);
      JComponent var3 = var1.getComponent();
      String var4 = "Synthetica.button.font";
      if (var3 instanceof JButton && ((JButton)var3).isDefaultButton()) {
         var4 = "Synthetica.button.default.font";
      }

      if (SyntheticaLookAndFeel.get(var4 + ".style", (Component)var3) != null) {
         int var5 = 0;
         String var6 = SyntheticaLookAndFeel.getString(var4 + ".style", var3);
         if (var6.contains("BOLD")) {
            var5 |= 1;
         }

         if (var6.contains("ITALIC")) {
            var5 |= 2;
         }

         float var7 = SyntheticaLookAndFeel.scaleFontSize((float)SyntheticaLookAndFeel.getInt(var4 + ".size", var3, 0));
         if (var7 == 0.0F) {
            var7 = ((Font)var2).getSize2D();
         }

         var2 = new FontUIResource(((Font)var2).deriveFont(var5, var7));
      }

      return (Font)var2;
   }
}
