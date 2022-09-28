package org.apache.fop.layoutmgr.table;

import java.util.List;
import org.apache.fop.layoutmgr.LayoutManager;
import org.apache.fop.layoutmgr.Position;

class TableHeaderFooterPosition extends Position {
   protected boolean header;
   protected List nestedElements;

   protected TableHeaderFooterPosition(LayoutManager lm, boolean header, List nestedElements) {
      super(lm);
      this.header = header;
      this.nestedElements = nestedElements;
   }

   public boolean generatesAreas() {
      return true;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("Table");
      sb.append(this.header ? "Header" : "Footer");
      sb.append("Position:");
      sb.append(this.getIndex()).append("(");
      sb.append(this.nestedElements);
      sb.append(")");
      return sb.toString();
   }
}
