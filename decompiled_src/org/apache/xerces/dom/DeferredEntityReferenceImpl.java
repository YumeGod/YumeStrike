package org.apache.xerces.dom;

public class DeferredEntityReferenceImpl extends EntityReferenceImpl implements DeferredNode {
   static final long serialVersionUID = 390319091370032223L;
   protected transient int fNodeIndex;

   DeferredEntityReferenceImpl(DeferredDocumentImpl var1, int var2) {
      super(var1, (String)null);
      this.fNodeIndex = var2;
      this.needsSyncData(true);
   }

   public int getNodeIndex() {
      return this.fNodeIndex;
   }

   protected void synchronizeData() {
      this.needsSyncData(false);
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)super.ownerDocument;
      super.name = var1.getNodeName(this.fNodeIndex);
      super.baseURI = var1.getNodeValue(this.fNodeIndex);
   }

   protected void synchronizeChildren() {
      this.needsSyncChildren(false);
      this.isReadOnly(false);
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)this.ownerDocument();
      var1.synchronizeChildren((ParentNode)this, this.fNodeIndex);
      this.setReadOnly(true, true);
   }
}
