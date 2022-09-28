package de.javasoft.plaf.synthetica.painter;

import java.awt.Graphics;
import javax.swing.plaf.synth.SynthContext;

public class EditorPanePainter extends TextComponentPainter {
   public static final String UI_KEY = "Synthetica.EditorPanePainter";

   protected EditorPanePainter() {
   }

   public static EditorPanePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static EditorPanePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, EditorPanePainter.class, "Synthetica.EditorPanePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, EditorPanePainter.class, "Synthetica.EditorPanePainter");
      }

      return (EditorPanePainter)var1;
   }

   public void paintEditorPaneBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintEditorPaneBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      this.paintBackground("editorPane", var1, var2, var3, var4, var5, var6);
   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }
}
