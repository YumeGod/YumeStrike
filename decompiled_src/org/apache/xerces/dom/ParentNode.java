package org.apache.xerces.dom;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;

public abstract class ParentNode extends ChildNode {
   static final long serialVersionUID = 2815829867152120872L;
   protected CoreDocumentImpl ownerDocument;
   protected ChildNode firstChild = null;
   protected transient NodeListCache fNodeListCache = null;

   protected ParentNode(CoreDocumentImpl var1) {
      super(var1);
      this.ownerDocument = var1;
   }

   public ParentNode() {
   }

   public Node cloneNode(boolean var1) {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      ParentNode var2 = (ParentNode)super.cloneNode(var1);
      var2.ownerDocument = this.ownerDocument;
      var2.firstChild = null;
      var2.fNodeListCache = null;
      if (var1) {
         for(ChildNode var3 = this.firstChild; var3 != null; var3 = var3.nextSibling) {
            var2.appendChild(var3.cloneNode(true));
         }
      }

      return var2;
   }

   public Document getOwnerDocument() {
      return this.ownerDocument;
   }

   CoreDocumentImpl ownerDocument() {
      return this.ownerDocument;
   }

   void setOwnerDocument(CoreDocumentImpl var1) {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      super.setOwnerDocument(var1);
      this.ownerDocument = var1;

      for(ChildNode var2 = this.firstChild; var2 != null; var2 = var2.nextSibling) {
         var2.setOwnerDocument(var1);
      }

   }

   public boolean hasChildNodes() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.firstChild != null;
   }

   public NodeList getChildNodes() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this;
   }

   public Node getFirstChild() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.firstChild;
   }

   public Node getLastChild() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return this.lastChild();
   }

   final ChildNode lastChild() {
      return this.firstChild != null ? this.firstChild.previousSibling : null;
   }

   final void lastChild(ChildNode var1) {
      if (this.firstChild != null) {
         this.firstChild.previousSibling = var1;
      }

   }

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      return this.internalInsertBefore(var1, var2, false);
   }

   Node internalInsertBefore(Node var1, Node var2, boolean var3) throws DOMException {
      boolean var4 = this.ownerDocument.errorChecking;
      if (var1.getNodeType() == 11) {
         if (var4) {
            for(Node var10 = var1.getFirstChild(); var10 != null; var10 = var10.getNextSibling()) {
               if (!this.ownerDocument.isKidOK(this, var10)) {
                  throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null));
               }
            }
         }

         while(var1.hasChildNodes()) {
            this.insertBefore(var1.getFirstChild(), var2);
         }

         return var1;
      } else if (var1 == var2) {
         var2 = var2.getNextSibling();
         this.removeChild(var1);
         this.insertBefore(var1, var2);
         return var1;
      } else {
         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         if (var4) {
            if (this.isReadOnly()) {
               throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null));
            }

            if (var1.getOwnerDocument() != this.ownerDocument && var1 != this.ownerDocument) {
               throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
            }

            if (!this.ownerDocument.isKidOK(this, var1)) {
               throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null));
            }

            if (var2 != null && var2.getParentNode() != this) {
               throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null));
            }

            boolean var5 = true;

            for(Object var6 = this; var5 && var6 != null; var6 = ((NodeImpl)var6).parentNode()) {
               var5 = var1 != var6;
            }

            if (!var5) {
               throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null));
            }
         }

         this.ownerDocument.insertingNode(this, var3);
         ChildNode var9 = (ChildNode)var1;
         NodeImpl var11 = var9.parentNode();
         if (var11 != null) {
            var11.removeChild(var9);
         }

         ChildNode var7 = (ChildNode)var2;
         var9.ownerNode = this;
         var9.isOwned(true);
         if (this.firstChild == null) {
            this.firstChild = var9;
            var9.isFirstChild(true);
            var9.previousSibling = var9;
         } else {
            ChildNode var8;
            if (var7 == null) {
               var8 = this.firstChild.previousSibling;
               var8.nextSibling = var9;
               var9.previousSibling = var8;
               this.firstChild.previousSibling = var9;
            } else if (var2 == this.firstChild) {
               this.firstChild.isFirstChild(false);
               var9.nextSibling = this.firstChild;
               var9.previousSibling = this.firstChild.previousSibling;
               this.firstChild.previousSibling = var9;
               this.firstChild = var9;
               var9.isFirstChild(true);
            } else {
               var8 = var7.previousSibling;
               var9.nextSibling = var7;
               var8.nextSibling = var9;
               var7.previousSibling = var9;
               var9.previousSibling = var8;
            }
         }

         this.changed();
         if (this.fNodeListCache != null) {
            if (this.fNodeListCache.fLength != -1) {
               ++this.fNodeListCache.fLength;
            }

            if (this.fNodeListCache.fChildIndex != -1) {
               if (this.fNodeListCache.fChild == var7) {
                  this.fNodeListCache.fChild = var9;
               } else {
                  this.fNodeListCache.fChildIndex = -1;
               }
            }
         }

         this.ownerDocument.insertedNode(this, var9, var3);
         this.checkNormalizationAfterInsert(var9);
         return var1;
      }
   }

   public Node removeChild(Node var1) throws DOMException {
      return this.internalRemoveChild(var1, false);
   }

   Node internalRemoveChild(Node var1, boolean var2) throws DOMException {
      CoreDocumentImpl var3 = this.ownerDocument();
      if (var3.errorChecking) {
         if (this.isReadOnly()) {
            throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null));
         }

         if (var1 != null && var1.getParentNode() != this) {
            throw new DOMException((short)8, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NOT_FOUND_ERR", (Object[])null));
         }
      }

      ChildNode var4 = (ChildNode)var1;
      var3.removingNode(this, var4, var2);
      if (this.fNodeListCache != null) {
         if (this.fNodeListCache.fLength != -1) {
            --this.fNodeListCache.fLength;
         }

         if (this.fNodeListCache.fChildIndex != -1) {
            if (this.fNodeListCache.fChild == var4) {
               --this.fNodeListCache.fChildIndex;
               this.fNodeListCache.fChild = var4.previousSibling();
            } else {
               this.fNodeListCache.fChildIndex = -1;
            }
         }
      }

      ChildNode var5;
      if (var4 == this.firstChild) {
         var4.isFirstChild(false);
         this.firstChild = var4.nextSibling;
         if (this.firstChild != null) {
            this.firstChild.isFirstChild(true);
            this.firstChild.previousSibling = var4.previousSibling;
         }
      } else {
         var5 = var4.previousSibling;
         ChildNode var6 = var4.nextSibling;
         var5.nextSibling = var6;
         if (var6 == null) {
            this.firstChild.previousSibling = var5;
         } else {
            var6.previousSibling = var5;
         }
      }

      var5 = var4.previousSibling();
      var4.ownerNode = var3;
      var4.isOwned(false);
      var4.nextSibling = null;
      var4.previousSibling = null;
      this.changed();
      var3.removedNode(this, var2);
      this.checkNormalizationAfterRemove(var5);
      return var4;
   }

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      this.ownerDocument.replacingNode(this);
      this.internalInsertBefore(var1, var2, true);
      if (var1 != var2) {
         this.internalRemoveChild(var2, true);
      }

      this.ownerDocument.replacedNode(this);
      return var2;
   }

   public String getTextContent() throws DOMException {
      Node var1 = this.getFirstChild();
      if (var1 != null) {
         Node var2 = var1.getNextSibling();
         if (var2 == null) {
            return this.hasTextContent(var1) ? ((NodeImpl)var1).getTextContent() : "";
         } else {
            if (super.fBufferStr == null) {
               super.fBufferStr = new StringBuffer();
            } else {
               super.fBufferStr.setLength(0);
            }

            this.getTextContent(super.fBufferStr);
            return super.fBufferStr.toString();
         }
      } else {
         return "";
      }
   }

   void getTextContent(StringBuffer var1) throws DOMException {
      for(Node var2 = this.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
         if (this.hasTextContent(var2)) {
            ((NodeImpl)var2).getTextContent(var1);
         }
      }

   }

   final boolean hasTextContent(Node var1) {
      return var1.getNodeType() != 8 && var1.getNodeType() != 7 && (var1.getNodeType() != 3 || !((TextImpl)var1).isIgnorableWhitespace());
   }

   public void setTextContent(String var1) throws DOMException {
      Node var2;
      while((var2 = this.getFirstChild()) != null) {
         this.removeChild(var2);
      }

      if (var1 != null && var1.length() != 0) {
         this.appendChild(this.ownerDocument().createTextNode(var1));
      }

   }

   private int nodeListGetLength() {
      if (this.fNodeListCache == null) {
         if (this.firstChild == null) {
            return 0;
         }

         if (this.firstChild == this.lastChild()) {
            return 1;
         }

         this.fNodeListCache = this.ownerDocument.getNodeListCache(this);
      }

      if (this.fNodeListCache.fLength == -1) {
         int var1;
         ChildNode var2;
         if (this.fNodeListCache.fChildIndex != -1 && this.fNodeListCache.fChild != null) {
            var1 = this.fNodeListCache.fChildIndex;
            var2 = this.fNodeListCache.fChild;
         } else {
            var2 = this.firstChild;
            var1 = 0;
         }

         while(var2 != null) {
            ++var1;
            var2 = var2.nextSibling;
         }

         this.fNodeListCache.fLength = var1;
      }

      return this.fNodeListCache.fLength;
   }

   public int getLength() {
      return this.nodeListGetLength();
   }

   private Node nodeListItem(int var1) {
      if (this.fNodeListCache == null) {
         if (this.firstChild == this.lastChild()) {
            return var1 == 0 ? this.firstChild : null;
         }

         this.fNodeListCache = this.ownerDocument.getNodeListCache(this);
      }

      int var2 = this.fNodeListCache.fChildIndex;
      ChildNode var3 = this.fNodeListCache.fChild;
      boolean var4 = true;
      if (var2 != -1 && var3 != null) {
         var4 = false;
         if (var2 < var1) {
            while(var2 < var1 && var3 != null) {
               ++var2;
               var3 = var3.nextSibling;
            }
         } else if (var2 > var1) {
            while(var2 > var1 && var3 != null) {
               --var2;
               var3 = var3.previousSibling();
            }
         }
      } else {
         if (var1 < 0) {
            return null;
         }

         var3 = this.firstChild;

         for(var2 = 0; var2 < var1 && var3 != null; ++var2) {
            var3 = var3.nextSibling;
         }
      }

      if (!var4 && (var3 == this.firstChild || var3 == this.lastChild())) {
         this.fNodeListCache.fChildIndex = -1;
         this.fNodeListCache.fChild = null;
         this.ownerDocument.freeNodeListCache(this.fNodeListCache);
      } else {
         this.fNodeListCache.fChildIndex = var2;
         this.fNodeListCache.fChild = var3;
      }

      return var3;
   }

   public Node item(int var1) {
      return this.nodeListItem(var1);
   }

   protected final NodeList getChildNodesUnoptimized() {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      return new NodeList() {
         public int getLength() {
            return ParentNode.this.nodeListGetLength();
         }

         public Node item(int var1) {
            return ParentNode.this.nodeListItem(var1);
         }
      };
   }

   public void normalize() {
      if (!this.isNormalized()) {
         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         for(ChildNode var1 = this.firstChild; var1 != null; var1 = var1.nextSibling) {
            var1.normalize();
         }

         this.isNormalized(true);
      }
   }

   public boolean isEqualNode(Node var1) {
      if (!super.isEqualNode(var1)) {
         return false;
      } else {
         Node var2 = this.getFirstChild();

         Node var3;
         for(var3 = var1.getFirstChild(); var2 != null && var3 != null; var3 = var3.getNextSibling()) {
            if (!((NodeImpl)var2).isEqualNode(var3)) {
               return false;
            }

            var2 = var2.getNextSibling();
         }

         return var2 == var3;
      }
   }

   public void setReadOnly(boolean var1, boolean var2) {
      super.setReadOnly(var1, var2);
      if (var2) {
         if (this.needsSyncChildren()) {
            this.synchronizeChildren();
         }

         for(ChildNode var3 = this.firstChild; var3 != null; var3 = var3.nextSibling) {
            if (var3.getNodeType() != 5) {
               var3.setReadOnly(var1, true);
            }
         }
      }

   }

   protected void synchronizeChildren() {
      this.needsSyncChildren(false);
   }

   void checkNormalizationAfterInsert(ChildNode var1) {
      if (var1.getNodeType() == 3) {
         ChildNode var2 = var1.previousSibling();
         ChildNode var3 = var1.nextSibling;
         if (var2 != null && var2.getNodeType() == 3 || var3 != null && var3.getNodeType() == 3) {
            this.isNormalized(false);
         }
      } else if (!var1.isNormalized()) {
         this.isNormalized(false);
      }

   }

   void checkNormalizationAfterRemove(ChildNode var1) {
      if (var1 != null && var1.getNodeType() == 3) {
         ChildNode var2 = var1.nextSibling;
         if (var2 != null && var2.getNodeType() == 3) {
            this.isNormalized(false);
         }
      }

   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      if (this.needsSyncChildren()) {
         this.synchronizeChildren();
      }

      var1.defaultWriteObject();
   }

   private void readObject(ObjectInputStream var1) throws ClassNotFoundException, IOException {
      var1.defaultReadObject();
      this.needsSyncChildren(false);
   }

   class UserDataRecord implements Serializable {
      private static final long serialVersionUID = 3258126977134310455L;
      Object fData;
      UserDataHandler fHandler;

      UserDataRecord(Object var2, UserDataHandler var3) {
         this.fData = var2;
         this.fHandler = var3;
      }
   }
}
