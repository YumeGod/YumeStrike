package org.apache.xerces.dom;

public class DeferredCommentImpl extends CommentImpl implements DeferredNode {
   static final long serialVersionUID = 6498796371083589338L;
   protected transient int fNodeIndex;

   DeferredCommentImpl(DeferredDocumentImpl var1, int var2) {
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
      super.data = var1.getNodeValueString(this.fNodeIndex);
   }
}
