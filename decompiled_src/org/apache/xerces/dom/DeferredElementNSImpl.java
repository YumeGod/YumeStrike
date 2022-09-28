package org.apache.xerces.dom;

import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.NamedNodeMap;

public class DeferredElementNSImpl extends ElementNSImpl implements DeferredNode {
   static final long serialVersionUID = -5001885145370927385L;
   protected transient int fNodeIndex;

   DeferredElementNSImpl(DeferredDocumentImpl var1, int var2) {
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
      int var3 = super.name.indexOf(58);
      if (var3 < 0) {
         super.localName = super.name;
      } else {
         super.localName = super.name.substring(var3 + 1);
      }

      super.namespaceURI = var1.getNodeURI(this.fNodeIndex);
      super.type = (XSTypeDefinition)var1.getTypeInfo(this.fNodeIndex);
      this.setupDefaultAttributes();
      int var4 = var1.getNodeExtra(this.fNodeIndex);
      if (var4 != -1) {
         NamedNodeMap var5 = this.getAttributes();

         do {
            NodeImpl var6 = (NodeImpl)var1.getNodeObject(var4);
            var5.setNamedItem(var6);
            var4 = var1.getPrevSibling(var4);
         } while(var4 != -1);
      }

      var1.mutationEvents = var2;
   }

   protected final void synchronizeChildren() {
      DeferredDocumentImpl var1 = (DeferredDocumentImpl)this.ownerDocument();
      var1.synchronizeChildren((ParentNode)this, this.fNodeIndex);
   }
}
