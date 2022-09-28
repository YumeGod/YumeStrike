package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.JTextComponent;

public class EditorPaneStyle extends StyleWrapper {
   private static EditorPaneStyle instance = new EditorPaneStyle();

   private EditorPaneStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null && ((JTextComponent)var1).isEditable()) {
         instance.setStyle(var0);
         return instance;
      } else {
         EditorPaneStyle var3 = new EditorPaneStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Color getColorForState(SynthContext var1, ColorType var2) {
      JComponent var3 = var1.getComponent();
      if (var2 == ColorType.TEXT_FOREGROUND && var3.getForeground() instanceof ColorUIResource && !((JTextComponent)var3).isEditable()) {
         Color var4 = SyntheticaLookAndFeel.getColor("Synthetica.editorPane.locked.textColor", var3);
         return var4 == null ? super.getColorForState(var1, var2) : var4;
      } else {
         return super.getColorForState(var1, var2);
      }
   }
}
