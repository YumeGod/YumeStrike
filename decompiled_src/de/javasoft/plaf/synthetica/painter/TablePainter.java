package de.javasoft.plaf.synthetica.painter;

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaState;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.JTableHeader;

public class TablePainter extends SyntheticaComponentPainter {
   public static final String UI_KEY = "Synthetica.TablePainter";

   protected TablePainter() {
   }

   public static TablePainter getInstance() {
      return getInstance((SynthContext)null);
   }

   public static TablePainter getInstance(SynthContext var0) {
      SyntheticaComponentPainter var1 = (SyntheticaComponentPainter)instances.get(getPainterClassName(var0, TablePainter.class, "Synthetica.TablePainter"));
      if (var1 == null) {
         var1 = getInstance(var0, TablePainter.class, "Synthetica.TablePainter");
      }

      return (TablePainter)var1;
   }

   public void paintTableBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTableBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTableHeaderBackground(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
      JComponent var7 = var1.getComponent();
      String var8 = SyntheticaLookAndFeel.getString("Synthetica.tableHeader.background.image", var7);
      if (var8 != null && SyntheticaLookAndFeel.isOpaque(var7)) {
         Insets var9 = SyntheticaLookAndFeel.getInsets("Synthetica.tableHeader.background.image.insets", var7, true);
         ImagePainter var11 = new ImagePainter(var2, var3, var4, var5, var6, var8, var9, var9, 0, 0);
         var11.draw();
      }

   }

   public void paintTableHeaderBorder(SynthContext var1, Graphics var2, int var3, int var4, int var5, int var6) {
   }

   public void paintTableHeaderBackground(JTable var1, SyntheticaState var2, Graphics var3, int var4, int var5, int var6, int var7) {
      JTableHeader var8 = var1.getTableHeader();
      SynthStyle var9 = SynthLookAndFeel.getStyleFactory().getStyle(var8, Region.TABLE_HEADER);
      SynthContext var10 = new SynthContext(var8, Region.TABLE_HEADER, var9, var2.getState());
      var9.getPainter(var10).paintTableHeaderBackground(var10, var3, var4, var5, var6, var7);
   }

   public void paintTableHeaderCellBackground(JTable var1, SyntheticaState var2, Graphics var3, int var4, int var5, int var6, int var7, int var8) {
      JTableHeader var9 = var1.getTableHeader();
      String var10 = null;
      Insets var11 = null;
      if (var8 == 1) {
         var10 = SyntheticaLookAndFeel.getString("Synthetica.tableHeader.ascendingSort.background.image", var9);
         var11 = SyntheticaLookAndFeel.getInsets("Synthetica.tableHeader.ascendingSort.background.image.insets", var9, true);
      } else if (var8 == 2) {
         var10 = SyntheticaLookAndFeel.getString("Synthetica.tableHeader.descendingSort.background.image", var9);
         var11 = SyntheticaLookAndFeel.getInsets("Synthetica.tableHeader.descendingSort.background.image.insets", var9, true);
      } else {
         var10 = SyntheticaLookAndFeel.getString("Synthetica.tableHeader.cell.background.image", var9);
         var11 = SyntheticaLookAndFeel.getInsets("Synthetica.tableHeader.cell.background.image.insets", var9, true);
      }

      if (var10 != null && SyntheticaLookAndFeel.isOpaque(var9)) {
         ImagePainter var12 = new ImagePainter(var9, var3, var4, var5, var6, var7, var10, var11, var11, 0, 0);
         var12.draw();
      }

   }

   public Cacheable.ScaleType getCacheScaleType(String var1) {
      return !var1.equals("paintTableHeaderBackground") ? Cacheable.ScaleType.NINE_SQUARE : super.getCacheScaleType(var1);
   }

   public int getCacheHash(SynthContext var1, int var2, int var3, int var4, String var5) {
      return !var5.equals("paintTableHeaderBackground") ? -1 : super.getCacheHash(var1, var2, var3, var4, var5);
   }
}
