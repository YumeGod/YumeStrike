package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class ViewportStyle extends StyleWrapper {
   private static ViewportStyle instance = new ViewportStyle();

   private ViewportStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         ViewportStyle var3 = new ViewportStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      if (var2 == ColorType.BACKGROUND) {
         JViewport var3 = (JViewport)var1.getComponent();
         Component var4 = var3.getView();
         if (var4 != null && var4.getClass().getName().endsWith("JHEditorPane") && SyntheticaLookAndFeel.get("Synthetica.javaHelpEditorViewport.background", (Component)var3) != null) {
            return SyntheticaLookAndFeel.getColor("Synthetica.javaHelpEditorViewport.background", var3);
         }

         boolean var5 = SyntheticaLookAndFeel.getBoolean("Synthetica.viewport.dynamicBackgroundColorEnabled", var3);
         if (var4 != null && var4 instanceof JComponent && ((JComponent)var4).isOpaque() && var5) {
            return var4.getBackground();
         }
      }

      return super.getColor(var1, var2);
   }
}
