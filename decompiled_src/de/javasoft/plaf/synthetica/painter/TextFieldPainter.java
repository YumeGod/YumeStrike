package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import javax.swing.JComboBox;
import javax.swing.plaf.synth.SynthContext;

public class TextFieldPainter extends TextComponentPainter {
   public static final String UI_KEY = "Synthetica.TextFieldPainter";

   protected TextFieldPainter() {
   }

   public static TextFieldPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static TextFieldPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, TextFieldPainter.class, "Synthetica.TextFieldPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, TextFieldPainter.class, "Synthetica.TextFieldPainter");
      }

      return (TextFieldPainter)var1;
   }

   public void paintTextFieldBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      super.paintBackground("textField", var1, var2, var3, var4, var5, var6);
   }

   public void paintTextFieldBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      if (this.paintCheck(var1)) {
         super.paintBorder("textField", var1, var2, var3, var4, var5, var6);
      }
   }

   protected boolean paintCheck(SynthContext var1) {
      return !(var1.getComponent().getParent() instanceof JComboBox);
   }
}
