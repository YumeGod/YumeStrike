package org.apache.fop.fo.flow.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.fop.fo.ValidationException;

class FixedColRowGroupBuilder extends RowGroupBuilder {
   private int numberOfColumns;
   private TableRow currentTableRow = null;
   private int currentRowIndex;
   private List rows;
   private boolean firstInPart = true;
   private List lastRow;
   private BorderResolver borderResolver;
   // $FF: synthetic field
   static final boolean $assertionsDisabled;

   FixedColRowGroupBuilder(Table t) {
      super(t);
      this.numberOfColumns = t.getNumberOfColumns();
      if (t.isSeparateBorderModel()) {
         this.borderResolver = new SeparateBorderResolver();
      } else {
         this.borderResolver = new CollapsingBorderResolver(t);
      }

      this.initialize();
   }

   private void initialize() {
      this.rows = new ArrayList();
      this.currentRowIndex = 0;
   }

   void addTableCell(TableCell cell) {
      int columnIndex;
      for(columnIndex = this.rows.size(); columnIndex < this.currentRowIndex + cell.getNumberRowsSpanned(); ++columnIndex) {
         List effRow = new ArrayList(this.numberOfColumns);

         for(int j = 0; j < this.numberOfColumns; ++j) {
            effRow.add((Object)null);
         }

         this.rows.add(effRow);
      }

      columnIndex = cell.getColumnNumber() - 1;
      PrimaryGridUnit pgu = new PrimaryGridUnit(cell, columnIndex);
      List row = (List)this.rows.get(this.currentRowIndex);
      row.set(columnIndex, pgu);
      GridUnit[] cellRow = new GridUnit[cell.getNumberColumnsSpanned()];
      cellRow[0] = pgu;

      int i;
      for(i = 1; i < cell.getNumberColumnsSpanned(); ++i) {
         GridUnit gu = new GridUnit(pgu, i, 0);
         row.set(columnIndex + i, gu);
         cellRow[i] = gu;
      }

      pgu.addRow(cellRow);

      for(i = 1; i < cell.getNumberRowsSpanned(); ++i) {
         row = (List)this.rows.get(this.currentRowIndex + i);
         cellRow = new GridUnit[cell.getNumberColumnsSpanned()];

         for(int j = 0; j < cell.getNumberColumnsSpanned(); ++j) {
            GridUnit gu = new GridUnit(pgu, j, i);
            row.set(columnIndex + j, gu);
            cellRow[j] = gu;
         }

         pgu.addRow(cellRow);
      }

   }

   private static void setFlagForCols(int flag, List row) {
      ListIterator iter = row.listIterator();

      while(iter.hasNext()) {
         ((GridUnit)iter.next()).setFlag(flag);
      }

   }

   void startTableRow(TableRow tableRow) {
      this.currentTableRow = tableRow;
   }

   void endTableRow() {
      if (!$assertionsDisabled && this.currentTableRow == null) {
         throw new AssertionError();
      } else {
         TableEventProducer eventProducer;
         if (this.currentRowIndex > 0 && this.currentTableRow.getBreakBefore() != 9) {
            eventProducer = TableEventProducer.Provider.get(this.currentTableRow.getUserAgent().getEventBroadcaster());
            eventProducer.breakIgnoredDueToRowSpanning(this, this.currentTableRow.getName(), true, this.currentTableRow.getLocator());
         }

         if (this.currentRowIndex < this.rows.size() - 1 && this.currentTableRow.getBreakAfter() != 9) {
            eventProducer = TableEventProducer.Provider.get(this.currentTableRow.getUserAgent().getEventBroadcaster());
            eventProducer.breakIgnoredDueToRowSpanning(this, this.currentTableRow.getName(), false, this.currentTableRow.getLocator());
         }

         Iterator iter = ((List)this.rows.get(this.currentRowIndex)).iterator();

         while(iter.hasNext()) {
            GridUnit gu = (GridUnit)iter.next();
            if (gu != null) {
               gu.setRow(this.currentTableRow);
            }
         }

         this.handleRowEnd(this.currentTableRow);
      }
   }

   void endRow(TablePart part) {
      this.handleRowEnd(part);
   }

   private void handleRowEnd(TableCellContainer container) {
      List currentRow = (List)this.rows.get(this.currentRowIndex);
      this.lastRow = currentRow;

      for(int i = 0; i < this.numberOfColumns; ++i) {
         if (currentRow.get(i) == null) {
            currentRow.set(i, new EmptyGridUnit(this.table, this.currentTableRow, i));
         }
      }

      this.borderResolver.endRow(currentRow, container);
      if (this.firstInPart) {
         setFlagForCols(0, currentRow);
         this.firstInPart = false;
      }

      if (this.currentRowIndex == this.rows.size() - 1) {
         container.getTablePart().addRowGroup(this.rows);
         this.initialize();
      } else {
         ++this.currentRowIndex;
      }

      this.currentTableRow = null;
   }

   void startTablePart(TablePart part) {
      this.firstInPart = true;
      this.borderResolver.startPart(part);
   }

   void endTablePart() throws ValidationException {
      if (this.rows.size() > 0) {
         throw new ValidationException("A table-cell is spanning more rows than available in its parent element.");
      } else {
         setFlagForCols(1, this.lastRow);
         this.borderResolver.endPart();
      }
   }

   void endTable() {
      this.borderResolver.endTable();
   }

   static {
      $assertionsDisabled = !FixedColRowGroupBuilder.class.desiredAssertionStatus();
   }
}
