package org.apache.fop.render.ps.extensions;

import org.apache.fop.fo.FONode;
import org.apache.fop.fo.extensions.ExtensionAttachment;

public class PSCommentAfterElement extends AbstractPSCommentElement {
   protected static final String ELEMENT = "ps-comment-after";

   public PSCommentAfterElement(FONode parent) {
      super(parent);
   }

   public String getLocalName() {
      return "ps-comment-after";
   }

   protected ExtensionAttachment instantiateExtensionAttachment() {
      return new PSCommentAfter();
   }
}
