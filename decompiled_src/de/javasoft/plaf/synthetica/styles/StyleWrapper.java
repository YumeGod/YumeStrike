package de.javasoft.plaf.synthetica.styles;

import de.javasoft.plaf.synthetica.painter.SyntheticaPainter;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthGraphicsUtils;
import javax.swing.plaf.synth.SynthPainter;
import javax.swing.plaf.synth.SynthStyle;

public abstract class StyleWrapper extends SynthStyle {
   protected SynthStyle synthStyle;

   public StyleWrapper() {
   }

   public StyleWrapper(SynthStyle var1) {
      this.synthStyle = var1;
   }

   void setStyle(SynthStyle var1) {
      this.synthStyle = var1;
   }

   public Object get(SynthContext var1, Object var2) {
      return this.synthStyle.get(var1, var2);
   }

   public boolean getBoolean(SynthContext var1, Object var2, boolean var3) {
      return this.synthStyle.getBoolean(var1, var2, var3);
   }

   public Color getColor(SynthContext var1, ColorType var2) {
      return this.synthStyle.getColor(var1, var2);
   }

   public Color getColorForState(SynthContext var1, ColorType var2) {
      return this.synthStyle.getColor(var1, var2);
   }

   public Font getFont(SynthContext var1) {
      return this.synthStyle.getFont(var1);
   }

   public Font getFontForState(SynthContext var1) {
      return this.synthStyle.getFont(var1);
   }

   public SynthGraphicsUtils getGraphicsUtils(SynthContext var1) {
      return this.synthStyle.getGraphicsUtils(var1);
   }

   public Icon getIcon(SynthContext var1, Object var2) {
      return this.synthStyle.getIcon(var1, var2);
   }

   public Insets getInsets(SynthContext var1, Insets var2) {
      return this.synthStyle.getInsets(var1, var2);
   }

   public int getInt(SynthContext var1, Object var2, int var3) {
      return this.synthStyle.getInt(var1, var2, var3);
   }

   public SynthPainter getPainter(SynthContext var1) {
      return SyntheticaPainter.getInstance();
   }

   public String getString(SynthContext var1, Object var2, String var3) {
      return this.synthStyle.getString(var1, var2, var3);
   }

   public void installDefaults(SynthContext var1) {
      this.synthStyle.installDefaults(var1);
   }

   public boolean isOpaque(SynthContext var1) {
      return this.synthStyle.isOpaque(var1);
   }

   public void uninstallDefaults(SynthContext var1) {
      this.synthStyle.uninstallDefaults(var1);
   }
}
