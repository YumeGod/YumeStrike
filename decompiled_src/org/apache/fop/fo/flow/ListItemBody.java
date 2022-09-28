package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public class ListItemBody extends AbstractListItemPart {
   public ListItemBody(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startListBody();
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endListBody();
   }

   public String getLocalName() {
      return "list-item-body";
   }

   public int getNameId() {
      return 42;
   }
}
