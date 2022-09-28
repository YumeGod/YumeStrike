package org.apache.xerces.dom;

import org.w3c.dom.Entity;
import org.w3c.dom.Node;

public class EntityImpl extends ParentNode implements Entity {
   static final long serialVersionUID = -3575760943444303423L;
   protected String name;
   protected String publicId;
   protected String systemId;
   protected String encoding;
   protected String inputEncoding;
   protected String version;
   protected String notationName;
   protected String baseURI;

   public EntityImpl(CoreDocumentImpl var1, String var2) {
      super(var1);
      this.name = var2;
      this.isReadOnly(true);
   }

   public short getNodeType() {
      return 6;
   }

   public String getNodeName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.name;
   }

   public Node cloneNode(boolean var1) {
      EntityImpl var2 = (EntityImpl)super.cloneNode(var1);
      var2.setReadOnly(true, var1);
      return var2;
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

   public String getXmlVersion() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.version;
   }

   public String getXmlEncoding() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.encoding;
   }

   public String getNotationName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.notationName;
   }

   public void setPublicId(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.publicId = var1;
   }

   public void setXmlEncoding(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.encoding = var1;
   }

   public String getInputEncoding() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.inputEncoding;
   }

   public void setInputEncoding(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.inputEncoding = var1;
   }

   public void setXmlVersion(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.version = var1;
   }

   public void setSystemId(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.systemId = var1;
   }

   public void setNotationName(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.notationName = var1;
   }

   public String getBaseURI() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.baseURI != null ? this.baseURI : ((CoreDocumentImpl)this.getOwnerDocument()).getBaseURI();
   }

   public void setBaseURI(String var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.baseURI = var1;
   }
}
