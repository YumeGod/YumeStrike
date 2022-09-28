package org.apache.xerces.dom;

public class DeferredEntityImpl extends EntityImpl implements DeferredNode {
   static final long serialVersionUID = 4760180431078941638L;
   protected transient int fNodeIndex;

   DeferredEntityImpl(DeferredDocumentImpl var1, int var2) {
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
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)super.ownerDocument;
      super.name = var1.getNodeName(this.fNodeIndex);
      super.publicId = var1.getNodeValue(this.fNodeIndex);
      super.systemId = var1.getNodeURI(this.fNodeIndex);
      int var2 = var1.getNodeExtra(this.fNodeIndex);
      var1.getNodeType(var2);
      super.notationName = var1.getNodeName(var2);
      super.version = var1.getNodeValue(var2);
      super.encoding = var1.getNodeURI(var2);
      int var3 = var1.getNodeExtra(var2);
      super.baseURI = var1.getNodeName(var3);
      super.inputEncoding = var1.getNodeValue(var3);
   }

   protected void synchronizeChildren() {
      this.needsSyncChildren(false);
      this.isReadOnly(false);
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)this.ownerDocument();
      var1.synchronizeChildren((ParentNode)this, this.fNodeIndex);
      this.setReadOnly(true, true);
   }
}
