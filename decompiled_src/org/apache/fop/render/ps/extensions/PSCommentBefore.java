package org.apache.fop.render.ps.extensions;

public class PSCommentBefore extends PSExtensionAttachment {
   protected static final String ELEMENT = "ps-comment-before";

   public PSCommentBefore(String content) {
      super(content);
   }

   public PSCommentBefore() {
   }

   protected String getElement() {
      return "ps-comment-before";
   }
}
