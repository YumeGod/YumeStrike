package org.apache.fop.render.ps.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public class PSSetupCodeElement extends AbstractPSExtensionObject {
   protected static final String ELEMENT = "ps-setup-code";

   public PSSetupCodeElement(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      if (this.parent.getNameId() != 13) {
         this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfDeclarations");
      }

   }

   public String getLocalName() {
      return "ps-setup-code";
   }
}
