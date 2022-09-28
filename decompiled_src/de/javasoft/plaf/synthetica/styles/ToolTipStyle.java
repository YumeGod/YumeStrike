package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import javax.swing.JComponent;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthStyle;

public class ToolTipStyle extends StyleWrapper {
   private static ToolTipStyle instance = new ToolTipStyle();

   private ToolTipStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      var1.setOpaque(false);
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         ToolTipStyle var3 = new ToolTipStyle();
         var3.setStyle(var0);
         return var3;
      }
   }
}
