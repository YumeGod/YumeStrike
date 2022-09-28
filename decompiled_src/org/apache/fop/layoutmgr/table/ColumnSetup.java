package org.apache.fop.layoutmgr.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.datatypes.Length;
import org.apache.fop.datatypes.PercentBaseContext;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.expr.RelativeNumericProperty;
import org.apache.fop.fo.flow.table.Table;
import org.apache.fop.fo.flow.table.TableColumn;
import org.apache.fop.fo.properties.TableColLength;

public class ColumnSetup {
   private static Log log;
   private Table table;
   private List columns = new ArrayList();
   private List colWidths = new ArrayList();
   private int maxColIndexReferenced = 0;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   public ColumnSetup(Table table) {
      this.table = table;
      this.prepareColumns();
      this.initializeColumnWidths();
   }

   private void prepareColumns() {
      List rawCols = this.table.getColumns();
      if (rawCols != null) {
         int colnum = true;
         ListIterator iter = rawCols.listIterator();

         label47:
         while(true) {
            TableColumn col;
            do {
               if (!iter.hasNext()) {
                  int pos = 1;

                  for(ListIterator ppIter = this.columns.listIterator(); ppIter.hasNext(); ++pos) {
                     TableColumn col = (TableColumn)ppIter.next();
                     if (col == null && !$assertionsDisabled) {
                        throw new AssertionError();
                     }
                  }
                  break label47;
               }

               col = (TableColumn)iter.next();
            } while(col == null);

            int colnum = col.getColumnNumber();

            for(int i = 0; i < col.getNumberColumnsRepeated(); ++i) {
               while(colnum > this.columns.size()) {
                  this.columns.add((Object)null);
               }

               this.columns.set(colnum - 1, col);
               ++colnum;
            }
         }
      }

   }

   public TableColumn getColumn(int index) {
      int size = this.columns.size();
      if (index <= size) {
         return (TableColumn)this.columns.get(index - 1);
      } else {
         if (index > this.maxColIndexReferenced) {
            this.maxColIndexReferenced = index;
            TableColumn col = this.getColumn(1);
            if (size != 1 || !col.isImplicitColumn()) {
               if (!$assertionsDisabled) {
                  throw new AssertionError();
               }

               log.warn(FONode.decorateWithContextInfo("There are fewer table-columns than are needed. Column " + index + " was accessed, but only " + size + " columns have been defined. " + "The last defined column will be reused.", this.table));
               if (!this.table.isAutoLayout()) {
                  log.warn("Please note that according XSL-FO 1.0 (7.26.9) says that the 'column-width' property must be specified for every column, unless the automatic table layout is used.");
               }
            }
         }

         return (TableColumn)this.columns.get(size - 1);
      }
   }

   public String toString() {
      return this.columns.toString();
   }

   public int getColumnCount() {
      return this.maxColIndexReferenced > this.columns.size() ? this.maxColIndexReferenced : this.columns.size();
   }

   public Iterator iterator() {
      return this.columns.iterator();
   }

   private void initializeColumnWidths() {
      int i = this.columns.size();

      while(true) {
         --i;
         if (i < 0) {
            this.colWidths.add(0, (Object)null);
            return;
         }

         if (this.columns.get(i) != null) {
            TableColumn col = (TableColumn)this.columns.get(i);
            Length colWidth = col.getColumnWidth();
            this.colWidths.add(0, colWidth);
         }
      }
   }

   protected double computeTableUnit(TableLayoutManager tlm) {
      return (double)this.computeTableUnit(tlm, tlm.getContentAreaIPD());
   }

   public float computeTableUnit(PercentBaseContext percentBaseContext, int contentAreaIPD) {
      int sumCols = 0;
      float factors = 0.0F;
      float unit = 0.0F;
      Iterator i = this.colWidths.iterator();

      while(i.hasNext()) {
         Length colWidth = (Length)i.next();
         if (colWidth != null) {
            sumCols += colWidth.getValue(percentBaseContext);
            if (colWidth instanceof RelativeNumericProperty) {
               factors = (float)((double)factors + ((RelativeNumericProperty)colWidth).getTableUnits());
            } else if (colWidth instanceof TableColLength) {
               factors = (float)((double)factors + ((TableColLength)colWidth).getTableUnits());
            }
         }
      }

      if (factors > 0.0F) {
         if (sumCols < contentAreaIPD) {
            unit = (float)(contentAreaIPD - sumCols) / factors;
         } else {
            log.warn("No space remaining to distribute over columns.");
         }
      }

      return unit;
   }

   public int getXOffset(int col, PercentBaseContext context) {
      int xoffset = 0;
      int i = col;

      while(true) {
         --i;
         if (i < 0) {
            return xoffset;
         }

         int effCol;
         if (i < this.colWidths.size()) {
            effCol = i;
         } else {
            effCol = this.colWidths.size() - 1;
         }

         if (this.colWidths.get(effCol) != null) {
            xoffset += ((Length)this.colWidths.get(effCol)).getValue(context);
         }
      }
   }

   public int getSumOfColumnWidths(PercentBaseContext context) {
      int sum = 0;
      int i = 1;

      for(int c = this.getColumnCount(); i <= c; ++i) {
         int effIndex = i;
         if (i >= this.colWidths.size()) {
            effIndex = this.colWidths.size() - 1;
         }

         if (this.colWidths.get(effIndex) != null) {
            sum += ((Length)this.colWidths.get(effIndex)).getValue(context);
         }
      }

      return sum;
   }

   static {
      $assertionsDisabled = !ColumnSetup.class.desiredAssertionStatus();
      log = LogFactory.getLog(ColumnSetup.class);
   }
}
