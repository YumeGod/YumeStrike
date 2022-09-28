package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import javax.swing.plaf.synth.SynthContext;

public class TextPanePainter extends TextComponentPainter {
   public static final String UI_KEY = "Synthetica.TextPanePainter";

   protected TextPanePainter() {
   }

   public static TextPanePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static TextPanePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, TextPanePainter.class, "Synthetica.TextPanePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, TextPanePainter.class, "Synthetica.TextPanePainter");
      }

      return (TextPanePainter)var1;
   }

   public void paintTextPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTextPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      this.paintBackground("textPane", var1, var2, var3, var4, var5, var6);
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }
}
