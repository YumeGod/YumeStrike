package org.apache.fop.render.afp.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;
import org.apache.fop.fo.extensions.ExtensionAttachment;

public class AFPInvokeMediumMapElement extends AbstractAFPExtensionObject {
   public AFPInvokeMediumMapElement(FONode parent) {
      super(parent, "invoke-medium-map");
   }

   protected void startOfNode() throws FOPException {
      super.startOfNode();
      if (this.parent.getNameId() != 53 && this.parent.getNameId() != 68) {
         this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfPageSequence");
      }

   }

   protected ExtensionAttachment instantiateExtensionAttachment() {
      return new AFPInvokeMediumMap();
   }
}
