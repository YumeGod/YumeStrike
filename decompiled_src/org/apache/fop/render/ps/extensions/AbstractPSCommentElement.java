package org.apache.fop.render.ps.extensions;

import org.apache.fop.apps.FOPException;
import org.apache.fop.fo.FONode;

public abstract class AbstractPSCommentElement extends AbstractPSExtensionElement {
   public AbstractPSCommentElement(FONode parent) {
      super(parent);
   }

   protected void startOfNode() throws FOPException {
      if (this.parent.getNameId() != 13 && this.parent.getNameId() != 68) {
         this.invalidChildError(this.getLocator(), this.parent.getName(), this.getNamespaceURI(), this.getName(), "rule.childOfSPMorDeclarations");
      }

   }
}
