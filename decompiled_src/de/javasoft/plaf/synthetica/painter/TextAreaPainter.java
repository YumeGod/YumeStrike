package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import javax.swing.plaf.synth.SynthContext;

public class TextAreaPainter extends TextComponentPainter {
   public static final String UI_KEY = "Synthetica.TextAreaPainter";

   protected TextAreaPainter() {
   }

   public static TextAreaPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static TextAreaPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, TextAreaPainter.class, "Synthetica.TextAreaPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, TextAreaPainter.class, "Synthetica.TextAreaPainter");
      }

      return (TextAreaPainter)var1;
   }

   public void paintTextAreaBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTextAreaBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      this.paintBackground("textArea", var1, var2, var3, var4, var5, var6);
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }
}
