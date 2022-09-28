package org.apache.fop.fo.flow.table;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public class TableFooter extends TablePart {
   public TableFooter(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startFooter(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endFooter(this);
   }

   public String getLocalName() {
      return "table-footer";
   }

   public int getNameId() {
      return 77;
   }
}
