package de.javasoft.plaf.synthetica.painter;

import java.awt.Insets;
import javax.swing.plaf.synth.SynthContext;

interface Cacheable {
   int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5);

   ScaleType getCacheScaleType(String var1);

   Insets getCacheScaleInsets(SynthContext var1, String var2);

   public static enum ScaleType {
      NONE,
      NINE_SQUARE;
   }
}
