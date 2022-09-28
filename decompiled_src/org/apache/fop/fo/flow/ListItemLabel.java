package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public class ListItemLabel extends AbstractListItemPart {
   public ListItemLabel(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startListLabel();
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endListLabel();
   }

   public String getLocalName() {
      return "list-item-label";
   }

   public int getNameId() {
      return 43;
   }
}
