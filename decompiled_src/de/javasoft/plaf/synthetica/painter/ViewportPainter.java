package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import javax.swing.plaf.synth.SynthContext;

public class ViewportPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ViewportPainter";

   protected ViewportPainter() {
   }

   public static ViewportPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ViewportPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ViewportPainter.class, "Synthetica.ViewportPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ViewportPainter.class, "Synthetica.ViewportPainter");
      }

      return (ViewportPainter)var1;
   }

   public void paintViewportBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintViewportBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return -1;
   }
}
