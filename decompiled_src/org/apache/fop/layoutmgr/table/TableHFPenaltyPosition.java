package org.apache.fop.layoutmgr.table;

import java.util.List;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.Position;

class TableHFPenaltyPosition extends Position {
   protected List headerElements;
   protected List footerElements;

   protected TableHFPenaltyPosition(LayoutManager lm) {
      super(lm);
   }

   public boolean generatesAreas() {
      return true;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("TableHFPenaltyPosition:");
      sb.append(this.getIndex()).append("(");
      sb.append("header:");
      sb.append(this.headerElements);
      sb.append(", footer:");
      sb.append(this.footerElements);
      sb.append(")");
      return sb.toString();
   }
}
