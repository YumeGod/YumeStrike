package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class SpinnerStyle extends StyleWrapper {
   private static SpinnerStyle instance = new SpinnerStyle();

   private SpinnerStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         SpinnerStyle var3 = new SpinnerStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Insets getInsets(SynthContext var1, Insets var2) {
      var2 = this.synthStyle.getInsets(var1, var2);
      if (!var1.getComponent().getComponentOrientation().isLeftToRight()) {
         int var3 = var2.left;
         var2.left = var2.right;
         var2.right = var3;
      }

      return var2;
   }
}
