package org.apache.fop.fo.flow.table;

import org.apache.fop.fo.ValidationException;

abstract class RowGroupBuilder {
   protected Table table;

   protected RowGroupBuilder(Table t) {
      this.table = t;
   }

   abstract void addTableCell(TableCell var1);

   abstract void startTableRow(TableRow var1);

   abstract void endTableRow();

   abstract void endRow(TablePart var1);

   abstract void startTablePart(TablePart var1);

   abstract void endTablePart() throws ValidationException;

   abstract void endTable() throws ValidationException;
}
