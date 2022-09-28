package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public class PageNumberCitation extends AbstractPageNumberCitation {
   public PageNumberCitation(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startPageNumberCitation(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endPageNumberCitation(this);
   }

   public String getLocalName() {
      return "page-number-citation";
   }

   public int getNameId() {
      return 51;
   }
}
