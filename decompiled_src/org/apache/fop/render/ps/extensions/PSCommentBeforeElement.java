package org.apache.fop.render.ps.extensions;

import org.apache.fop.fo.FONode;
import org.apache.fop.fo.extensions.ExtensionAttachment;

public class PSCommentBeforeElement extends AbstractPSCommentElement {
   protected static final String ELEMENT = "ps-comment-before";

   public PSCommentBeforeElement(FONode parent) {
      super(parent);
   }

   public String getLocalName() {
      return "ps-comment-before";
   }

   protected ExtensionAttachment instantiateExtensionAttachment() {
      return new PSCommentBefore();
   }
}
