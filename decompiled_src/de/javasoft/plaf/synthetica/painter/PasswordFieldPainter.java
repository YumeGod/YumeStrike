package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import javax.swing.plaf.synth.SynthContext;

public class PasswordFieldPainter extends TextComponentPainter {
   public static final String UI_KEY = "Synthetica.PasswordFieldPainter";

   protected PasswordFieldPainter() {
   }

   public static PasswordFieldPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static PasswordFieldPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, PasswordFieldPainter.class, "Synthetica.PasswordFieldPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, PasswordFieldPainter.class, "Synthetica.PasswordFieldPainter");
      }

      return (PasswordFieldPainter)var1;
   }

   public void paintPasswordFieldBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      super.paintBackground("passwordField", var1, var2, var3, var4, var5, var6);
   }

   public void paintPasswordFieldBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      super.paintBorder("passwordField", var1, var2, var3, var4, var5, var6);
   }
}
