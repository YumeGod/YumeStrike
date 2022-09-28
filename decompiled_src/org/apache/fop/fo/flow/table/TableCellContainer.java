package org.apache.fop.fo.flow.table;

import java.util.List;
import org.apache.fop.apps.FOPException;
import org.apache.fop.datatypes.Length;
import org.apache.fop.fo.FONode;

public abstract class TableCellContainer extends TableFObj implements ColumnNumberManagerHolder {
   protected List pendingSpans;
   protected ColumnNumberManager columnNumberManager;

   public TableCellContainer(FONode parent) {
      super(parent);
   }

   protected void addTableCellChild(TableCell cell, boolean firstRow) throws FOPException {
      int colNumber = cell.getColumnNumber();
      int colSpan = cell.getNumberColumnsSpanned();
      int rowSpan = cell.getNumberRowsSpanned();
      Table t = this.getTable();
      if (t.hasExplicitColumns()) {
         if (colNumber + colSpan - 1 > t.getNumberOfColumns()) {
            TableEventProducer eventProducer = TableEventProducer.Provider.get(this.getUserAgent().getEventBroadcaster());
            eventProducer.tooManyCells(this, this.getLocator());
         }
      } else {
         t.ensureColumnNumber(colNumber + colSpan - 1);

         while(this.pendingSpans.size() < colNumber + colSpan - 1) {
            this.pendingSpans.add((Object)null);
         }
      }

      if (firstRow) {
         this.handleCellWidth(cell, colNumber, colSpan);
      }

      if (rowSpan > 1) {
         for(int i = 0; i < colSpan; ++i) {
            this.pendingSpans.set(colNumber - 1 + i, new PendingSpan(rowSpan));
         }
      }

      this.columnNumberManager.signalUsedColumnNumbers(colNumber, colNumber + colSpan - 1);
      t.getRowGroupBuilder().addTableCell(cell);
   }

   private void handleCellWidth(TableCell cell, int colNumber, int colSpan) throws FOPException {
      Table t = this.getTable();
      Length colWidth = null;
      if (cell.getWidth().getEnum() != 9 && colSpan == 1) {
         colWidth = cell.getWidth();
      }

      for(int i = colNumber; i < colNumber + colSpan; ++i) {
         TableColumn col = t.getColumn(i - 1);
         if (colWidth != null) {
            col.setColumnWidth(colWidth);
         }
      }

   }

   abstract TablePart getTablePart();

   public ColumnNumberManager getColumnNumberManager() {
      return this.columnNumberManager;
   }
}
