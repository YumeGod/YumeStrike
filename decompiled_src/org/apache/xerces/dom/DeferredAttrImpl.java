package org.apache.xerces.dom;

public final class DeferredAttrImpl extends AttrImpl implements DeferredNode {
   static final long serialVersionUID = 6903232312469148636L;
   protected transient int fNodeIndex;

   DeferredAttrImpl(DeferredDocumentImpl var1, int var2) {
      super(var1, (String)null);
      this.fNodeIndex = var2;
      this.needsSyncData(true);
      this.needsSyncChildren(true);
   }

   public int getNodeIndex() {
      return this.fNodeIndex;
   }

   protected void synchronizeData() {
      this.needsSyncData(false);
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)this.ownerDocument();
      super.name = var1.getNodeName(this.fNodeIndex);
      int var2 = var1.getNodeExtra(this.fNodeIndex);
      this.isSpecified((var2 & 32) != 0);
      this.isIdAttribute((var2 & 512) != 0);
      int var3 = var1.getLastChild(this.fNodeIndex);
      super.type = var1.getTypeInfo(var3);
   }

   protected void synchronizeChildren() {
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)this.ownerDocument();
      var1.synchronizeChildren((AttrImpl)this, this.fNodeIndex);
   }
}
