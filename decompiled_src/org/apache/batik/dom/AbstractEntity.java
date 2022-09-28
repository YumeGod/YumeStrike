package org.apache.batik.dom;

import org.w3c.dom.Entity;
import org.w3c.dom.Node;

public abstract class AbstractEntity extends AbstractParentNode implements Entity {
   protected String nodeName;
   protected String publicId;
   protected String systemId;

   public short getNodeType() {
      return 6;
   }

   public void setNodeName(String var1) {
      this.nodeName = var1;
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public String getPublicId() {
      return this.publicId;
   }

   public void setPublicId(String var1) {
      this.publicId = var1;
   }

   public String getSystemId() {
      return this.systemId;
   }

   public void setSystemId(String var1) {
      this.systemId = var1;
   }

   public String getNotationName() {
      return this.getNodeName();
   }

   public void setNotationName(String var1) {
      this.setNodeName(var1);
   }

   public String getInputEncoding() {
      return null;
   }

   public String getXmlEncoding() {
      return null;
   }

   public String getXmlVersion() {
      return null;
   }

   protected Node export(Node var1, AbstractDocument var2) {
      super.export(var1, var2);
      AbstractEntity var3 = (AbstractEntity)var1;
      var3.nodeName = this.nodeName;
      var3.publicId = this.publicId;
      var3.systemId = this.systemId;
      return var1;
   }

   protected Node deepExport(Node var1, AbstractDocument var2) {
      super.deepExport(var1, var2);
      AbstractEntity var3 = (AbstractEntity)var1;
      var3.nodeName = this.nodeName;
      var3.publicId = this.publicId;
      var3.systemId = this.systemId;
      return var1;
   }

   protected Node copyInto(Node var1) {
      super.copyInto(var1);
      AbstractEntity var2 = (AbstractEntity)var1;
      var2.nodeName = this.nodeName;
      var2.publicId = this.publicId;
      var2.systemId = this.systemId;
      return var1;
   }

   protected Node deepCopyInto(Node var1) {
      super.deepCopyInto(var1);
      AbstractEntity var2 = (AbstractEntity)var1;
      var2.nodeName = this.nodeName;
      var2.publicId = this.publicId;
      var2.systemId = this.systemId;
      return var1;
   }

   protected void checkChildType(Node var1, boolean var2) {
      switch (var1.getNodeType()) {
         case 1:
         case 3:
         case 4:
         case 5:
         case 7:
         case 8:
         case 11:
            return;
         case 2:
         case 6:
         case 9:
         case 10:
         default:
            throw this.createDOMException((short)3, "child.type", new Object[]{new Integer(this.getNodeType()), this.getNodeName(), new Integer(var1.getNodeType()), var1.getNodeName()});
      }
   }
}
