package org.apache.xerces.dom;

import org.apache.xerces.util.URI;
import org.w3c.dom.DOMException;
import org.w3c.dom.Notation;

public class NotationImpl extends NodeImpl implements Notation {
   static final long serialVersionUID = -764632195890658402L;
   protected String name;
   protected String publicId;
   protected String systemId;
   protected String baseURI;

   public NotationImpl(CoreDocumentImpl var1, String var2) {
      super(var1);
      this.name = var2;
   }

   public short getNodeType() {
      return 12;
   }

   public String getNodeName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.name;
   }

   public String getPublicId() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.publicId;
   }

   public String getSystemId() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.systemId;
   }

   public void setPublicId(String var1) {
      if (this.isReadOnly()) {
         throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null));
      } else {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         this.publicId = var1;
      }
   }

   public void setSystemId(String var1) {
      if (this.isReadOnly()) {
         throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null));
      } else {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         this.systemId = var1;
      }
   }

   public String getBaseURI() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (this.baseURI != null && this.baseURI.length() != 0) {
         try {
            return (new URI(this.baseURI)).toString();
         } catch (URI.MalformedURIException var2) {
            return null;
         }
      } else {
         return this.baseURI;
      }
   }

   public void setBaseURI(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.baseURI = var1;
   }
}
