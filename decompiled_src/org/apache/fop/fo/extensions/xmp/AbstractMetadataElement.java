package org.apache.fop.fo.extensions.xmp;

import org.apache.fop.fo.FONode;
import org.apache.fop.fo.FObj;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.fop.util.ContentHandlerFactory;
import org.apache.xmlgraphics.xmp.Metadata;

public abstract class AbstractMetadataElement extends FONode implements ContentHandlerFactory.ObjectBuiltListener {
   private XMPMetadata attachment;

   public AbstractMetadataElement(FONode parent) {
      super(parent);
   }

   public ContentHandlerFactory getContentHandlerFactory() {
      return new XMPContentHandlerFactory();
   }

   public ExtensionAttachment getExtensionAttachment() {
      if (this.parent instanceof FObj) {
         if (this.attachment == null) {
            this.attachment = new XMPMetadata();
         }

         return this.attachment;
      } else {
         return super.getExtensionAttachment();
      }
   }

   public void notifyObjectBuilt(Object obj) {
      this.attachment.setMetadata((Metadata)obj);
   }
}
