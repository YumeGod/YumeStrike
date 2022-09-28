package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import javax.swing.plaf.synth.SynthContext;

public class ListPainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.ListPainter";

   protected ListPainter() {
   }

   public static ListPainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static ListPainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, ListPainter.class, "Synthetica.ListPainter"));
      if (var1 == null) {
         var1 = getInstance(var0, ListPainter.class, "Synthetica.ListPainter");
      }

      return (ListPainter)var1;
   }

   public void paintListBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintListBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return -1;
   }
}
