package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class TabbedPaneContentStyle extends StyleWrapper {
   private static TabbedPaneContentStyle instance = new TabbedPaneContentStyle();

   private TabbedPaneContentStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         TabbedPaneContentStyle var3 = new TabbedPaneContentStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Insets getInsets(SynthContext var1, Insets var2) {
      JTabbedPane var3 = (JTabbedPane)var1.getComponent();
      int var4 = var3.getTabPlacement();
      Insets var5 = super.getInsets(var1, var2);
      if (var4 == 2) {
         return SyntheticaLookAndFeel.getInsets("Synthetica.tabbedPane.insets.left", var3, var5);
      } else if (var4 == 4) {
         return SyntheticaLookAndFeel.getInsets("Synthetica.tabbedPane.insets.right", var3, var5);
      } else {
         return var4 == 3 ? SyntheticaLookAndFeel.getInsets("Synthetica.tabbedPane.insets.bottom", var3, var5) : var5;
      }
   }
}
