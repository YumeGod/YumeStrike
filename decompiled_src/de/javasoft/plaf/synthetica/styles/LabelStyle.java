package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import java.awt.Container;
import javax.swing.CellRendererPane;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class LabelStyle extends StyleWrapper {
   private static LabelStyle instance = new LabelStyle();

   private LabelStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         LabelStyle var3 = new LabelStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      JComponent var3 = var1.getComponent();
      Container var4 = var3.getParent();
      if (var4 != null && var4 instanceof JComponent) {
         Color var7 = null;
         if (((JComponent)var4).getUIClassID().equals("StatusBarUI")) {
            var7 = SyntheticaLookAndFeel.getColor("Synthetica.statusBar.label.foreground", var3);
         } else if (var4 instanceof JToolBar) {
            var7 = SyntheticaLookAndFeel.getColor("Synthetica.toolBar.label.foreground", var3);
         }

         if (var7 != null) {
            return var7;
         }
      } else if (var2 == ColorType.TEXT_FOREGROUND && var4 instanceof CellRendererPane && var4.getParent() instanceof JComboBox) {
         JComboBox var5 = (JComboBox)var4.getParent();
         Color var6 = null;
         if (!var5.isEnabled()) {
            var6 = SyntheticaLookAndFeel.getColor("Synthetica.comboBox.disabled.textColor", var5);
         } else if (var5.getForeground() != null && !(var5.getForeground() instanceof ColorUIResource)) {
            var6 = var5.getForeground();
         } else if (var5.hasFocus()) {
            var6 = SyntheticaLookAndFeel.getColor("Synthetica.comboBox.focused.textColor", var5);
         } else if (!var5.isEditable()) {
            var6 = SyntheticaLookAndFeel.getColor("Synthetica.comboBox.locked.textColor", var5);
         }

         return var6;
      }

      return this.synthStyle.getColor(var1, var2);
   }
}
