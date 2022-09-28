package org.apache.fop.render.ps.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public class PSPageSetupCodeElement extends AbstractPSExtensionObject {
   protected static final String ELEMENT = "ps-page-setup-code";

   public PSPageSetupCodeElement(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      if (this.parent.getNameId() != 68) {
         this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfSPM");
      }

   }

   public String getLocalName() {
      return "ps-page-setup-code";
   }
}
