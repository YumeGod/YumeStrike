package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.painter.TablePainter;
import de.javasoft.util.JavaVersion;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import sun.swing.table.DefaultTableCellHeaderRenderer;

class SyntheticaHeaderRenderer extends DefaultTableCellHeaderRenderer implements UIResource {
   private static final long serialVersionUID = -4089492349514455249L;
   private JTable table;
   private int column;
   // $FF: synthetic field
   private static int[] $SWITCH_TABLE$javax$swing$SortOrder;

   SyntheticaHeaderRenderer() {
      this.setHorizontalAlignment(SyntheticaLookAndFeel.getInt("Synthetica.tableHeader.horizontalAlignment", this, 10));
      this.setName("TableHeader.renderer");
   }

   public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
      this.table = var1;
      this.column = var6;
      Component var7 = super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
      SynthContext var10;
      if (!var4 && var7 instanceof JComponent && var1 != null && var1.getTableHeader() != null && var7 instanceof JComponent) {
         JComponent var8 = (JComponent)var7;
         SynthStyle var9 = SynthLookAndFeel.getStyle(var1.getTableHeader(), Region.TABLE_HEADER);
         var10 = new SynthContext(var1.getTableHeader(), Region.TABLE_HEADER, var9, 0);
         Insets var11 = var9.getInsets(var10, (Insets)null);
         Border var12 = var8.getBorder();
         Border var13 = UIManager.getBorder("TableHeader.cellBorder");
         if (var13 != null) {
            if (var12 != var13) {
               var8.setBorder(var13);
            }
         } else if (var11.equals(new Insets(0, 0, 0, 0))) {
            if (var12 == null) {
               var8.setBorder(noFocusBorder);
            }
         } else {
            var8.setBorder(new EmptyBorder(var11));
         }
      }

      if (JavaVersion.JAVA6U10_OR_ABOVE && var1 != null && var1.getRowSorter() != null) {
         Iterator var15 = this.getSortKeys(var1).iterator();

         while(var15.hasNext()) {
            RowSorter.SortKey var14 = (RowSorter.SortKey)var15.next();
            if (var14.getColumn() == var1.convertColumnIndexToModel(var6)) {
               var10 = null;
               Icon var16;
               switch (var14.getSortOrder()) {
                  case ASCENDING:
                     var16 = UIManager.getIcon("Table.ascendingSortIcon");
                     break;
                  case DESCENDING:
                     var16 = UIManager.getIcon("Table.descendingSortIcon");
                     break;
                  default:
                     var16 = UIManager.getIcon("Table.naturalSortIcon");
               }

               this.setIcon(var16);
            }
         }
      }

      return var7;
   }

   public void paintComponent(Graphics var1) {
      if (this.table != null) {
         byte var2 = 0;
         boolean var3 = false;
         int[] var4 = (int[])this.table.getClientProperty("SORTABLE_TABLE_SORTED_COLUMNS");
         Boolean var5 = (Boolean)this.table.getClientProperty("SORTABLE_TABLE_PAINT_SORT_BACKGROUND");
         if (var4 != null && var5 != null && var5) {
            int[] var9 = var4;
            int var8 = var4.length;

            for(int var11 = 0; var11 < var8; ++var11) {
               int var10 = var9[var11];
               if (var10 == this.column) {
                  TablePainter.getInstance().paintTableHeaderCellBackground(this.table, new SyntheticaState(0), var1, 0, 0, this.getWidth(), this.getHeight(), 1);
                  var3 = true;
                  break;
               }
            }
         } else if (JavaVersion.JAVA6U10_OR_ABOVE && this.table.getRowSorter() != null) {
            Iterator var7 = this.getSortKeys(this.table).iterator();

            while(var7.hasNext()) {
               RowSorter.SortKey var6 = (RowSorter.SortKey)var7.next();
               if (var6.getColumn() == this.table.convertColumnIndexToModel(this.column)) {
                  switch (var6.getSortOrder()) {
                     case ASCENDING:
                        var2 = 1;
                        break;
                     case DESCENDING:
                        var2 = 2;
                     case UNSORTED:
                  }

                  TablePainter.getInstance().paintTableHeaderCellBackground(this.table, new SyntheticaState(0), var1, 0, 0, this.getWidth(), this.getHeight(), var2);
                  var3 = true;
               }
            }
         }

         if (!var3) {
            TablePainter.getInstance().paintTableHeaderCellBackground(this.table, new SyntheticaState(0), var1, 0, 0, this.getWidth(), this.getHeight(), var2);
         }
      }

      super.paintComponent(var1);
   }

   private List getSortKeys(JTable var1) {
      RowSorter var2 = var1.getRowSorter();
      Object var3 = var2.getSortKeys();
      if (((List)var3).size() > 0) {
         var3 = new ArrayList();
         ((List)var3).add((RowSorter.SortKey)var2.getSortKeys().get(0));
      }

      return (List)var3;
   }

   public boolean isOpaque() {
      Boolean var1 = (Boolean)SyntheticaLookAndFeel.get("Synthetica.tableHeader.opaque", (Component)this.table);
      return var1 == null ? super.isOpaque() : var1;
   }

   // $FF: synthetic method
   static int[] $SWITCH_TABLE$javax$swing$SortOrder() {
      int[] var10000 = $SWITCH_TABLE$javax$swing$SortOrder;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[SortOrder.values().length];

         try {
            var0[SortOrder.ASCENDING.ordinal()] = 1;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[SortOrder.DESCENDING.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[SortOrder.UNSORTED.ordinal()] = 3;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$javax$swing$SortOrder = var0;
         return var0;
      }
   }
}
