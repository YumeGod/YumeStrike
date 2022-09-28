package org.apache.fop.area;

import org.apache.fop.fo.extensions.ExtensionAttachment;

public class OffDocumentExtensionAttachment implements OffDocumentItem {
   private ExtensionAttachment attachment;

   public OffDocumentExtensionAttachment(ExtensionAttachment attachment) {
      this.attachment = attachment;
   }

   public ExtensionAttachment getAttachment() {
      return this.attachment;
   }

   public int getWhenToProcess() {
      return 0;
   }

   public String getName() {
      return this.attachment.getCategory();
   }
}
