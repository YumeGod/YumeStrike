package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.TablePainter;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.table.DefaultTableCellRenderer;

class SyntheticaHeaderRenderer5 extends DefaultTableCellRenderer implements UIResource {
   private static final long serialVersionUID = -5043195354176641815L;
   private JTable table;
   private int column;

   SyntheticaHeaderRenderer5() {
      this.setHorizontalAlignment(SyntheticaLookAndFeel.getInt("Synthetica.tableHeader.horizontalAlignment", this, 10));
      this.setName("TableHeader.renderer");
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      this.table = var1;
      this.column = var6;
      if (var1 != null && !var4) {
         SynthStyle var7 = SynthLookAndFeel.getStyle(var1.getTableHeader(), Region.TABLE_HEADER);
         SynthContext var8 = new SynthContext(var1.getTableHeader(), Region.TABLE_HEADER, var7, 0);
         Insets var9 = var7.getInsets(var8, (Insets)null);
         Border var10 = this.getBorder();
         Border var11 = UIManager.getBorder("TableHeader.cellBorder");
         if (var11 != null) {
            if (var10 != var11) {
               this.setBorder(var11);
            }
         } else if (var9.equals(new Insets(0, 0, 0, 0))) {
            this.setBorder(noFocusBorder);
         } else {
            this.setBorder(new EmptyBorder(var9));
         }
      }

      this.setValue(var2);
      return this;
   }

   public void paintComponent(Graphics var1) {
      if (this.table != null) {
         boolean var2 = false;
         int[] var3 = (int[])this.table.getClientProperty("SORTABLE_TABLE_SORTED_COLUMNS");
         Boolean var4 = (Boolean)this.table.getClientProperty("SORTABLE_TABLE_PAINT_SORT_BACKGROUND");
         if (var3 != null && var4 != null && var4) {
            int[] var8 = var3;
            int var7 = var3.length;

            for(int var6 = 0; var6 < var7; ++var6) {
               int var5 = var8[var6];
               if (var5 == this.column) {
                  TablePainter.getInstance().paintTableHeaderCellBackground(this.table, new SyntheticaState(0), var1, 0, 0, this.getWidth(), this.getHeight(), 1);
                  var2 = true;
                  break;
               }
            }
         }

         if (!var2) {
            TablePainter.getInstance().paintTableHeaderCellBackground(this.table, new SyntheticaState(0), var1, 0, 0, this.getWidth(), this.getHeight(), 0);
         }
      }

      super.paintComponent(var1);
   }

   public boolean isOpaque() {
      Boolean var1 = (Boolean)SyntheticaLookAndFeel.get("Synthetica.tableHeader.opaque", (Component)this.table);
      return var1 == null ? super.isOpaque() : var1;
   }
}
