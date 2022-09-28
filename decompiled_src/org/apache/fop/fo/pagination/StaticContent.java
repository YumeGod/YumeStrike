package org.apache.fop.fo.pagination;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.ValidationException;
import org.xml.sax.Locator;

public class StaticContent extends Flow {
   public StaticContent(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      if (this.getFlowName() == null || this.getFlowName().equals("")) {
         this.missingPropertyError("flow-name");
      }

      this.getFOEventHandler().startFlow(this);
   }

   protected void endOfNode() throws FOPException {
      if (this.firstChild == null && this.getUserAgent().validateStrictly()) {
         this.missingChildElementError("(%block;)+");
      }

      this.getFOEventHandler().endFlow(this);
   }

   protected void validateChildNode(Locator loc, String nsURI, String localName) throws ValidationException {
      if ("http://www.w3.org/1999/XSL/Format".equals(nsURI) && !this.isBlockItem(nsURI, localName)) {
         this.invalidChildError(loc, nsURI, localName);
      }

   }

   public String getLocalName() {
      return "static-content";
   }

   public int getNameId() {
      return 70;
   }
}
