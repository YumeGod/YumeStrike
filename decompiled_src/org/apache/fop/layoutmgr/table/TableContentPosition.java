package org.apache.fop.layoutmgr.table;

import java.util.List;
import org.apache.fop.fo.flow.table.EffRow;
import org.apache.fop.fo.flow.table.TablePart;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.Position;

class TableContentPosition extends Position {
   public static final int FIRST_IN_ROWGROUP = 1;
   public static final int LAST_IN_ROWGROUP = 2;
   protected List cellParts;
   private EffRow row;
   protected int flags;
   private EffRow newPageRow;

   protected TableContentPosition(LayoutManager lm, List cellParts, EffRow row) {
      super(lm);
      this.cellParts = cellParts;
      this.row = row;
      this.newPageRow = row;
   }

   void setNewPageRow(EffRow newPageRow) {
      this.newPageRow = newPageRow;
   }

   EffRow getNewPageRow() {
      return this.newPageRow;
   }

   EffRow getRow() {
      return this.row;
   }

   TablePart getTablePart() {
      return ((CellPart)this.cellParts.get(0)).pgu.getTablePart();
   }

   public boolean getFlag(int which) {
      return (this.flags & 1 << which) != 0;
   }

   public void setFlag(int which, boolean value) {
      if (value) {
         this.flags |= 1 << which;
      } else {
         this.flags &= ~(1 << which);
      }

   }

   public boolean generatesAreas() {
      return true;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("TableContentPosition:");
      sb.append(this.getIndex());
      sb.append("[");
      sb.append(this.row.getIndex()).append("/");
      sb.append(this.getFlag(1) ? "F" : "-");
      sb.append(this.getFlag(2) ? "L" : "-").append("]");
      sb.append("(");
      sb.append(this.cellParts);
      sb.append(")");
      return sb.toString();
   }
}
