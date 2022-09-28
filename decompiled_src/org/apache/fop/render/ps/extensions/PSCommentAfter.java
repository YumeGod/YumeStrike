package org.apache.fop.render.ps.extensions;

public class PSCommentAfter extends PSExtensionAttachment {
   protected static final String ELEMENT = "ps-comment-after";

   public PSCommentAfter() {
   }

   public PSCommentAfter(String content) {
      super(content);
   }

   protected String getElement() {
      return "ps-comment-after";
   }
}
