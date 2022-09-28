package org.apache.fop.fo.flow;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public class PageNumberCitationLast extends AbstractPageNumberCitation {
   public PageNumberCitationLast(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      this.getFOEventHandler().startPageNumberCitationLast(this);
   }

   protected void endOfNode() throws FOPException {
      super.endOfNode();
      this.getFOEventHandler().endPageNumberCitationLast(this);
   }

   public String getLocalName() {
      return "page-number-citation-last";
   }

   public int getNameId() {
      return 52;
   }
}
