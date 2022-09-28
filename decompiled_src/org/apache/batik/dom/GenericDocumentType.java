package org.apache.batik.dom;

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class GenericDocumentType extends AbstractChildNode implements DocumentType {
   protected String qualifiedName;
   protected String publicId;
   protected String systemId;

   public GenericDocumentType(String var1, String var2, String var3) {
      this.qualifiedName = var1;
      this.publicId = var2;
      this.systemId = var3;
   }

   public String getNodeName() {
      return this.qualifiedName;
   }

   public short getNodeType() {
      return 10;
   }

   public boolean isReadonly() {
      return true;
   }

   public void setReadonly(boolean var1) {
   }

   public String getName() {
      return null;
   }

   public NamedNodeMap getEntities() {
      return null;
   }

   public NamedNodeMap getNotations() {
      return null;
   }

   public String getPublicId() {
      return this.publicId;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public String getInternalSubset() {
      return null;
   }

   protected Node newNode() {
      return new GenericDocumentType(this.qualifiedName, this.publicId, this.systemId);
   }
}
