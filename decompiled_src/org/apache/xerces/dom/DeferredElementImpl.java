package org.apache.xerces.dom;

import org.w3c.dom.NamedNodeMap;

public class DeferredElementImpl extends ElementImpl implements DeferredNode {
   static final long serialVersionUID = -7670981133940934842L;
   protected transient int fNodeIndex;

   DeferredElementImpl(DeferredDocumentImpl var1, int var2) {
      super(var1, (String)null);
      this.fNodeIndex = var2;
      this.needsSyncChildren(true);
   }

   public final int getNodeIndex() {
      return this.fNodeIndex;
   }

   protected final void synchronizeData() {
      this.needsSyncData(false);
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)super.ownerDocument;
      boolean var2 = var1.mutationEvents;
      var1.mutationEvents = false;
      super.name = var1.getNodeName(this.fNodeIndex);
      this.setupDefaultAttributes();
      int var3 = var1.getNodeExtra(this.fNodeIndex);
      if (var3 != -1) {
         NamedNodeMap var4 = this.getAttributes();

         do {
            NodeImpl var5 = (NodeImpl)var1.getNodeObject(var3);
            var4.setNamedItem(var5);
            var3 = var1.getPrevSibling(var3);
         } while(var3 != -1);
      }

      var1.mutationEvents = var2;
   }

   protected final void synchronizeChildren() {
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)this.ownerDocument();
      var1.synchronizeChildren((ParentNode)this, this.fNodeIndex);
   }
}
