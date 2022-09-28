package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class ComboBoxStyle extends StyleWrapper {
   private static ComboBoxStyle instance = new ComboBoxStyle();

   private ComboBoxStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         ComboBoxStyle var3 = new ComboBoxStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Insets getInsets(SynthContext var1, Insets var2) {
      Insets var3 = super.getInsets(var1, var2);
      if (!var1.getComponent().getComponentOrientation().isLeftToRight()) {
         var3 = new Insets(var3.top, var3.right, var3.bottom, var3.left);
      }

      return var3;
   }
}
