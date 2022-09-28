package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.synth.SynthContext;

public class TreePainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.TreePainter";
   private static boolean cellRendererSelectionBackgroundEnabled;

   protected TreePainter() {
   }

   public static TreePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static TreePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, TreePainter.class, "Synthetica.TreePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, TreePainter.class, "Synthetica.TreePainter");
      }

      return (TreePainter)var1;
   }

   public static void reinitialize() {
      cellRendererSelectionBackgroundEnabled = SyntheticaLookAndFeel.getBoolean("Synthetica.cellRenderer.selectionBackground.enabled", (Component)null, false);
   }

   public void paintTreeBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTreeBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JComponent var7 = var1.getComponent();
      Color var8 = var7.getBackground();
      if (var8 != null && !(var8 instanceof ColorUIResource) && var7.isOpaque()) {
         var2.setColor(var8);
         var2.fillRect(var3, var4, var5, var6);
      }

   }

   public void paintTreeCellBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTreeCellBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JComponent var7 = var1.getComponent();
      if (cellRendererSelectionBackgroundEnabled) {
         boolean var8 = true;
         boolean var9 = SyntheticaLookAndFeel.getBoolean("Synthetica.cellRenderer.respectFocus", var7, false);
         if (var9) {
            if (var7.getParent() instanceof CellRendererPane) {
               var8 = var7.getParent().getParent().hasFocus();
            } else {
               var8 = var7.hasFocus();
            }
         }

         String var10 = SyntheticaLookAndFeel.getString(var8 ? "Synthetica.tree.selectionBackground" : "Synthetica.tree.selectionBackground.inactive", var7);
         if (var10 != null) {
            Insets var11 = SyntheticaLookAndFeel.getInsets("Synthetica.tree.selectionBackground.insets", var7, false);
            ImagePainter var12 = new ImagePainter(var2, var3, var4, var5, var6, var10, var11, var11, 0, 0);
            var12.draw();
         }
      }

   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return Cacheable.ScaleType.NINE_SQUARE;
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return -1;
   }
}
