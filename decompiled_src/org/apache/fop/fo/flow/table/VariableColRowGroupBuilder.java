package org.apache.fop.fo.flow.table;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.fop.fo.ValidationException;

class VariableColRowGroupBuilder extends RowGroupBuilder {
   private List events = new LinkedList();

   VariableColRowGroupBuilder(Table t) {
      super(t);
   }

   void addTableCell(final TableCell cell) {
      this.events.add(new Event() {
         public void play(RowGroupBuilder rowGroupBuilder) {
            rowGroupBuilder.addTableCell(cell);
         }
      });
   }

   void startTableRow(final TableRow tableRow) {
      this.events.add(new Event() {
         public void play(RowGroupBuilder rowGroupBuilder) {
            rowGroupBuilder.startTableRow(tableRow);
         }
      });
   }

   void endTableRow() {
      this.events.add(new Event() {
         public void play(RowGroupBuilder rowGroupBuilder) {
            rowGroupBuilder.endTableRow();
         }
      });
   }

   void endRow(final TablePart part) {
      this.events.add(new Event() {
         public void play(RowGroupBuilder rowGroupBuilder) {
            rowGroupBuilder.endRow(part);
         }
      });
   }

   void startTablePart(final TablePart part) {
      this.events.add(new Event() {
         public void play(RowGroupBuilder rowGroupBuilder) {
            rowGroupBuilder.startTablePart(part);
         }
      });
   }

   void endTablePart() throws ValidationException {
      this.events.add(new Event() {
         public void play(RowGroupBuilder rowGroupBuilder) throws ValidationException {
            rowGroupBuilder.endTablePart();
         }
      });
   }

   void endTable() throws ValidationException {
      RowGroupBuilder delegate = new FixedColRowGroupBuilder(this.table);
      Iterator eventIter = this.events.iterator();

      while(eventIter.hasNext()) {
         ((Event)eventIter.next()).play(delegate);
      }

      delegate.endTable();
   }

   private interface Event {
      void play(RowGroupBuilder var1) throws ValidationException;
   }
}
