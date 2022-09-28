package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.plaf.synth.SynthContext;

public class FormattedTextFieldPainter extends TextComponentPainter {
   public static final String UI_KEY = "Synthetica.FormattedTextFieldPainter";

   protected FormattedTextFieldPainter() {
   }

   public static FormattedTextFieldPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static FormattedTextFieldPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, FormattedTextFieldPainter.class, "Synthetica.FormattedTextFieldPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, FormattedTextFieldPainter.class, "Synthetica.FormattedTextFieldPainter");
      }

      return (FormattedTextFieldPainter)var1;
   }

   public void paintFormattedTextFieldBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (this.paintCheck(var1)) {
         super.paintBackground("formattedTextField", var1, var2, var3, var4, var5, var6);
      }
   }

   public void paintFormattedTextFieldBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (this.paintCheck(var1)) {
         super.paintBorder("formattedTextField", var1, var2, var3, var4, var5, var6);
      }
   }

   protected boolean paintCheck(SynthContext var1) {
      JComponent var2 = var1.getComponent();
      String var3 = var2.getName();
      if (var3 != null && var3.startsWith("Spinner")) {
         return false;
      } else {
         return !(var2.getParent() instanceof JComboBox);
      }
   }
}
