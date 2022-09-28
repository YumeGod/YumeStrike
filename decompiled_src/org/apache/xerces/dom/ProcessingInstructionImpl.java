package org.apache.xerces.dom;

import org.w3c.dom.ProcessingInstruction;

public class ProcessingInstructionImpl extends CharacterDataImpl implements ProcessingInstruction {
   static final long serialVersionUID = 7554435174099981510L;
   protected String target;

   public ProcessingInstructionImpl(CoreDocumentImpl var1, String var2, String var3) {
      super(var1, var3);
      this.target = var2;
   }

   public short getNodeType() {
      return 7;
   }

   public String getNodeName() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.target;
   }

   public String getTarget() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.target;
   }

   public String getData() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return super.data;
   }

   public void setData(String var1) {
      this.setNodeValue(var1);
   }

   public String getBaseURI() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return super.ownerNode.getBaseURI();
   }
}
