package org.apache.fop.fo.flow.table;

import java.util.List;

interface BorderResolver {
   void endRow(List var1, TableCellContainer var2);

   void startPart(TablePart var1);

   void endPart();

   void endTable();
}
