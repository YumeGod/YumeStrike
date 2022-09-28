package org.apache.fop.render.rtf.rtflib.rtfdoc;

public interface ITableColumnsInfo {
   float INVALID_COLUMN_WIDTH = 200.0F;

   void selectFirstColumn();

   void selectNextColumn();

   float getColumnWidth();

   int getColumnIndex();

   int getNumberOfColumns();

   boolean getFirstSpanningCol();
}
