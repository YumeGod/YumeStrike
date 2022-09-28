package org.apache.xerces.dom;

public class DeferredTextImpl extends TextImpl implements DeferredNode {
   static final long serialVersionUID = 2310613872100393425L;
   protected transient int fNodeIndex;

   DeferredTextImpl(DeferredDocumentImpl var1, int var2) {
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
      this.isIgnorableWhitespace(var1.getNodeExtra(this.fNodeIndex) == 1);
   }
}
