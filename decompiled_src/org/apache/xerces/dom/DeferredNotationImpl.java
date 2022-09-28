package org.apache.xerces.dom;

public class DeferredNotationImpl extends NotationImpl implements DeferredNode {
   static final long serialVersionUID = 5705337172887990848L;
   protected transient int fNodeIndex;

   DeferredNotationImpl(DeferredDocumentImpl var1, int var2) {
      super(var1, (String)null);
      this.fNodeIndex = var2;
      this.needsSyncData(true);
   }

   public int getNodeIndex() {
      return this.fNodeIndex;
   }

   protected void synchronizeData() {
      this.needsSyncData(false);
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)this.ownerDocument();
      super.name = var1.getNodeName(this.fNodeIndex);
      var1.getNodeType(this.fNodeIndex);
      super.publicId = var1.getNodeValue(this.fNodeIndex);
      super.systemId = var1.getNodeURI(this.fNodeIndex);
      int var2 = var1.getNodeExtra(this.fNodeIndex);
      var1.getNodeType(var2);
      super.baseURI = var1.getNodeName(var2);
   }
}
