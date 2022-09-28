package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class ListStyle extends StyleWrapper {
   private static ListStyle instance = new ListStyle();

   private ListStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         ListStyle var3 = new ListStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      JComponent var3 = var1.getComponent();
      return var2 == ColorType.BACKGROUND && var3.getBackground() != null ? var3.getBackground() : this.synthStyle.getColor(var1, var2);
   }

   public Object get(SynthContext var1, Object var2) {
      return "Tree.dropLineColor".equals(var2) ? UIManager.get("Tree.dropLineColor") : this.synthStyle.get(var1, var2);
   }
}
