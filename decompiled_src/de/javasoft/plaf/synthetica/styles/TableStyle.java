package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import java.awt.FontMetrics;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthStyle;

public class TableStyle extends StyleWrapper {
   private static TableStyle instance = new TableStyle();

   private TableStyle() {
   }

   public static SynthStyle getStyle(SynthStyle var0, JComponent var1, Region var2) {
      if (SyntheticaLookAndFeel.getStyleName(var1) == null) {
         instance.setStyle(var0);
         return instance;
      } else {
         TableStyle var3 = new TableStyle();
         var3.setStyle(var0);
         return var3;
      }
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      if (var2 == ColorType.BACKGROUND) {
         Color var3 = var1.getComponent().getBackground();
         if (var3 != null && !(var3 instanceof UIResource)) {
            return var3;
         }
      }

      return super.getColor(var1, var2);
   }

   public Object get(SynthContext var1, Object var2) {
      if ("Table.rowHeight".equals(var2)) {
         JComponent var3 = var1.getComponent();
         FontMetrics var4 = var3.getFontMetrics(var3.getFont());
         int var5 = var4.getHeight();
         Integer var6 = (Integer)this.synthStyle.get(var1, var2);
         return var6 == null ? new Integer(var5 + 2) : new Integer((int)SyntheticaLookAndFeel.scaleFontSize((float)var6));
      } else if ("Table.dropLineColor".equals(var2)) {
         return UIManager.get("Table.dropLineColor");
      } else {
         return "Table.dropLineShortColor".equals(var2) ? UIManager.get("Table.dropLineShortColor") : this.synthStyle.get(var1, var2);
      }
   }
}
