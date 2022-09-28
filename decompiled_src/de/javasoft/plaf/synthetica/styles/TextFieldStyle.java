package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.text.JTextComponent;

public class TextFieldStyle extends StyleWrapper {
   private static TextFieldStyle instance = new TextFieldStyle();

   private TextFieldStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null && ((JTextComponent)var1).isEditable()) {
         instance.setStyle(var0);
         return instance;
      } else {
         TextFieldStyle var3 = new TextFieldStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Insets getInsets(SynthContext var1, Insets var2) {
      String var3 = var1.getComponent().getName();
      return "ComboBox.textField".equals(var3) ? new Insets(0, 1, 0, 1) : this.synthStyle.getInsets(var1, var2);
   }

   public Color getColorForState(SynthContext var1, ColorType var2) {
      JComponent var3 = var1.getComponent();
      if (var2 == ColorType.TEXT_FOREGROUND && var3.getForeground() instanceof ColorUIResource && !((JTextComponent)var3).isEditable()) {
         Color var4 = SyntheticaLookAndFeel.getColor("Synthetica.textField.locked.textColor", var3);
         return var4 == null ? super.getColorForState(var1, var2) : var4;
      } else {
         return super.getColorForState(var1, var2);
      }
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      JComponent var3 = var1.getComponent();
      Color var4 = var3.getBackground();
      return var3.isOpaque() && var4 != null && !(var4 instanceof UIResource) && var2 == ColorType.BACKGROUND ? var4 : super.getColor(var1, var2);
   }
}
