package org.apache.xerces.dom;

public class DeferredProcessingInstructionImpl extends ProcessingInstructionImpl implements DeferredNode {
   static final long serialVersionUID = -4643577954293565388L;
   protected transient int fNodeIndex;

   DeferredProcessingInstructionImpl(DeferredDocumentImpl var1, int var2) {
      super(var1, (String)null, (String)null);
      this.fNodeIndex = var2;
      this.needsSyncData(true);
   }

   public int getNodeIndex() {
      return this.fNodeIndex;
   }

   protected void synchronizeData() {
      this.needsSyncData(false);
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)this.ownerDocument();
      super.target = var1.getNodeName(this.fNodeIndex);
      super.data = var1.getNodeValueString(this.fNodeIndex);
   }
}
