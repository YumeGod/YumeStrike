package org.apache.fop.fo.flow.table;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public class TableBody extends TablePart {
   public TableBody(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startBody(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endBody(this);
   }

   public String getLocalName() {
      return "table-body";
   }

   public int getNameId() {
      return 73;
   }
}
