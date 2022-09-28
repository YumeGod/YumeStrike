package org.apache.fop.fo.extensions.xmp;

import java.io.Serializable;
import org.apache.fop.fo.extensions.ExtensionAttachment;
import org.apache.xmlgraphics.util.XMLizable;
import org.apache.xmlgraphics.xmp.Metadata;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class XMPMetadata implements ExtensionAttachment, Serializable, XMLizable {
   public static final String CATEGORY = "adobe:ns:meta/";
   private Metadata meta;
   private boolean readOnly = true;

   public XMPMetadata() {
   }

   public XMPMetadata(Metadata metadata) {
      this.meta = metadata;
   }

   public Metadata getMetadata() {
      return this.meta;
   }

   public void setMetadata(Metadata metadata) {
      this.meta = metadata;
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public void setReadOnly(boolean readOnly) {
      this.readOnly = readOnly;
   }

   public String getCategory() {
      return "adobe:ns:meta/";
   }

   public void toSAX(ContentHandler handler) throws SAXException {
      this.getMetadata().toSAX(handler);
   }
}
