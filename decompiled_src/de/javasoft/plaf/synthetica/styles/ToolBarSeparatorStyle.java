package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class ToolBarSeparatorStyle extends StyleWrapper {
   private static ToolBarSeparatorStyle instance = new ToolBarSeparatorStyle();

   private ToolBarSeparatorStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         ToolBarSeparatorStyle var3 = new ToolBarSeparatorStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Object get(SynthContext var1, Object var2) {
      if (var2.equals("ToolBar.separatorSize")) {
         Dimension var3 = SyntheticaLookAndFeel.getToolbarSeparatorDimension();
         if (var3 != null) {
            return new DimensionUIResource(var3.width, var3.height);
         }
      }

      return this.synthStyle.get(var1, var2);
   }
}
