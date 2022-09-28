package org.apache.xerces.dom;

import java.util.Vector;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.ranges.RangeException;

public class RangeImpl implements Range {
   DocumentImpl fDocument;
   Node fStartContainer;
   Node fEndContainer;
   int fStartOffset;
   int fEndOffset;
   boolean fIsCollapsed;
   boolean fDetach = false;
   Node fInsertNode = null;
   Node fDeleteNode = null;
   Node fSplitNode = null;
   boolean fInsertedFromRange = false;
   Node fRemoveChild = null;
   static final int EXTRACT_CONTENTS = 1;
   static final int CLONE_CONTENTS = 2;
   static final int DELETE_CONTENTS = 3;

   public RangeImpl(DocumentImpl var1) {
      this.fDocument = var1;
      this.fStartContainer = var1;
      this.fEndContainer = var1;
      this.fStartOffset = 0;
      this.fEndOffset = 0;
      this.fDetach = false;
   }

   public Node getStartContainer() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         return this.fStartContainer;
      }
   }

   public int getStartOffset() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         return this.fStartOffset;
      }
   }

   public Node getEndContainer() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         return this.fEndContainer;
      }
   }

   public int getEndOffset() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         return this.fEndOffset;
      }
   }

   public boolean getCollapsed() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         return this.fStartContainer == this.fEndContainer && this.fStartOffset == this.fEndOffset;
      }
   }

   public Node getCommonAncestorContainer() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         Vector var1 = new Vector();

         Node var2;
         for(var2 = this.fStartContainer; var2 != null; var2 = var2.getParentNode()) {
            var1.addElement(var2);
         }

         Vector var3 = new Vector();

         for(var2 = this.fEndContainer; var2 != null; var2 = var2.getParentNode()) {
            var3.addElement(var2);
         }

         int var4 = var1.size() - 1;
         int var5 = var3.size() - 1;

         Object var6;
         for(var6 = null; var4 >= 0 && var5 >= 0 && var1.elementAt(var4) == var3.elementAt(var5); --var5) {
            var6 = var1.elementAt(var4);
            --var4;
         }

         return (Node)var6;
      }
   }

   public void setStart(Node var1, int var2) throws RangeException, DOMException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (!this.isLegalContainer(var1)) {
            throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
         }

         if (this.fDocument != var1.getOwnerDocument() && this.fDocument != var1) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      this.checkIndex(var1, var2);
      this.fStartContainer = var1;
      this.fStartOffset = var2;
      if (this.getCommonAncestorContainer() == null || this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset) {
         this.collapse(true);
      }

   }

   public void setEnd(Node var1, int var2) throws RangeException, DOMException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (!this.isLegalContainer(var1)) {
            throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
         }

         if (this.fDocument != var1.getOwnerDocument() && this.fDocument != var1) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      this.checkIndex(var1, var2);
      this.fEndContainer = var1;
      this.fEndOffset = var2;
      if (this.getCommonAncestorContainer() == null || this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset) {
         this.collapse(false);
      }

   }

   public void setStartBefore(Node var1) throws RangeException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (!this.hasLegalRootContainer(var1) || !this.isLegalContainedNode(var1)) {
            throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
         }

         if (this.fDocument != var1.getOwnerDocument() && this.fDocument != var1) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      this.fStartContainer = var1.getParentNode();
      int var2 = 0;

      for(Node var3 = var1; var3 != null; var3 = var3.getPreviousSibling()) {
         ++var2;
      }

      this.fStartOffset = var2 - 1;
      if (this.getCommonAncestorContainer() == null || this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset) {
         this.collapse(true);
      }

   }

   public void setStartAfter(Node var1) throws RangeException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (!this.hasLegalRootContainer(var1) || !this.isLegalContainedNode(var1)) {
            throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
         }

         if (this.fDocument != var1.getOwnerDocument() && this.fDocument != var1) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      this.fStartContainer = var1.getParentNode();
      int var2 = 0;

      for(Node var3 = var1; var3 != null; var3 = var3.getPreviousSibling()) {
         ++var2;
      }

      this.fStartOffset = var2;
      if (this.getCommonAncestorContainer() == null || this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset) {
         this.collapse(true);
      }

   }

   public void setEndBefore(Node var1) throws RangeException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (!this.hasLegalRootContainer(var1) || !this.isLegalContainedNode(var1)) {
            throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
         }

         if (this.fDocument != var1.getOwnerDocument() && this.fDocument != var1) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      this.fEndContainer = var1.getParentNode();
      int var2 = 0;

      for(Node var3 = var1; var3 != null; var3 = var3.getPreviousSibling()) {
         ++var2;
      }

      this.fEndOffset = var2 - 1;
      if (this.getCommonAncestorContainer() == null || this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset) {
         this.collapse(false);
      }

   }

   public void setEndAfter(Node var1) throws RangeException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (!this.hasLegalRootContainer(var1) || !this.isLegalContainedNode(var1)) {
            throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
         }

         if (this.fDocument != var1.getOwnerDocument() && this.fDocument != var1) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      this.fEndContainer = var1.getParentNode();
      int var2 = 0;

      for(Node var3 = var1; var3 != null; var3 = var3.getPreviousSibling()) {
         ++var2;
      }

      this.fEndOffset = var2;
      if (this.getCommonAncestorContainer() == null || this.fStartContainer == this.fEndContainer && this.fEndOffset < this.fStartOffset) {
         this.collapse(false);
      }

   }

   public void collapse(boolean var1) {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         if (var1) {
            this.fEndContainer = this.fStartContainer;
            this.fEndOffset = this.fStartOffset;
         } else {
            this.fStartContainer = this.fEndContainer;
            this.fStartOffset = this.fEndOffset;
         }

      }
   }

   public void selectNode(Node var1) throws RangeException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (!this.isLegalContainer(var1.getParentNode()) || !this.isLegalContainedNode(var1)) {
            throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
         }

         if (this.fDocument != var1.getOwnerDocument() && this.fDocument != var1) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      Node var2 = var1.getParentNode();
      if (var2 != null) {
         this.fStartContainer = var2;
         this.fEndContainer = var2;
         int var3 = 0;

         for(Node var4 = var1; var4 != null; var4 = var4.getPreviousSibling()) {
            ++var3;
         }

         this.fStartOffset = var3 - 1;
         this.fEndOffset = this.fStartOffset + 1;
      }

   }

   public void selectNodeContents(Node var1) throws RangeException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (!this.isLegalContainer(var1)) {
            throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
         }

         if (this.fDocument != var1.getOwnerDocument() && this.fDocument != var1) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      this.fStartContainer = var1;
      this.fEndContainer = var1;
      Node var2 = var1.getFirstChild();
      this.fStartOffset = 0;
      if (var2 == null) {
         this.fEndOffset = 0;
      } else {
         int var3 = 0;

         for(Node var4 = var2; var4 != null; var4 = var4.getNextSibling()) {
            ++var3;
         }

         this.fEndOffset = var3;
      }

   }

   public short compareBoundaryPoints(short var1, Range var2) throws DOMException {
      if (this.fDocument.errorChecking) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         }

         if (this.fDocument != var2.getStartContainer().getOwnerDocument() && this.fDocument != var2.getStartContainer() && var2.getStartContainer() != null || this.fDocument != var2.getEndContainer().getOwnerDocument() && this.fDocument != var2.getEndContainer() && var2.getStartContainer() != null) {
            throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
         }
      }

      Node var3;
      Node var4;
      int var5;
      int var6;
      if (var1 == 0) {
         var3 = var2.getStartContainer();
         var4 = this.fStartContainer;
         var5 = var2.getStartOffset();
         var6 = this.fStartOffset;
      } else if (var1 == 1) {
         var3 = var2.getStartContainer();
         var4 = this.fEndContainer;
         var5 = var2.getStartOffset();
         var6 = this.fEndOffset;
      } else if (var1 == 3) {
         var3 = var2.getEndContainer();
         var4 = this.fStartContainer;
         var5 = var2.getEndOffset();
         var6 = this.fStartOffset;
      } else {
         var3 = var2.getEndContainer();
         var4 = this.fEndContainer;
         var5 = var2.getEndOffset();
         var6 = this.fEndOffset;
      }

      if (var3 == var4) {
         if (var5 < var6) {
            return 1;
         } else {
            return (short)(var5 == var6 ? 0 : -1);
         }
      } else {
         Node var7 = var4;

         for(Node var8 = var4.getParentNode(); var8 != null; var8 = var8.getParentNode()) {
            if (var8 == var3) {
               int var9 = this.indexOf(var7, var3);
               if (var5 <= var9) {
                  return 1;
               }

               return -1;
            }

            var7 = var8;
         }

         Node var17 = var3;

         int var11;
         for(Node var10 = var3.getParentNode(); var10 != null; var10 = var10.getParentNode()) {
            if (var10 == var4) {
               var11 = this.indexOf(var17, var4);
               if (var11 < var6) {
                  return 1;
               }

               return -1;
            }

            var17 = var10;
         }

         var11 = 0;

         for(Node var12 = var3; var12 != null; var12 = var12.getParentNode()) {
            ++var11;
         }

         for(Node var13 = var4; var13 != null; var13 = var13.getParentNode()) {
            --var11;
         }

         while(var11 > 0) {
            var3 = var3.getParentNode();
            --var11;
         }

         while(var11 < 0) {
            var4 = var4.getParentNode();
            ++var11;
         }

         Node var14 = var3.getParentNode();

         for(Node var15 = var4.getParentNode(); var14 != var15; var15 = var15.getParentNode()) {
            var3 = var14;
            var4 = var15;
            var14 = var14.getParentNode();
         }

         for(Node var16 = var3.getNextSibling(); var16 != null; var16 = var16.getNextSibling()) {
            if (var16 == var4) {
               return 1;
            }
         }

         return -1;
      }
   }

   public void deleteContents() throws DOMException {
      this.traverseContents(3);
   }

   public DocumentFragment extractContents() throws DOMException {
      return this.traverseContents(1);
   }

   public DocumentFragment cloneContents() throws DOMException {
      return this.traverseContents(2);
   }

   public void insertNode(Node var1) throws DOMException, RangeException {
      if (var1 != null) {
         short var2 = var1.getNodeType();
         if (this.fDocument.errorChecking) {
            if (this.fDetach) {
               throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
            }

            if (this.fDocument != var1.getOwnerDocument()) {
               throw new DOMException((short)4, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "WRONG_DOCUMENT_ERR", (Object[])null));
            }

            if (var2 == 2 || var2 == 6 || var2 == 12 || var2 == 9) {
               throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
            }
         }

         int var5 = 0;
         this.fInsertedFromRange = true;
         if (this.fStartContainer.getNodeType() == 3) {
            Node var6 = this.fStartContainer.getParentNode();
            var5 = var6.getChildNodes().getLength();
            Node var3 = this.fStartContainer.cloneNode(false);
            ((TextImpl)var3).setNodeValueInternal(var3.getNodeValue().substring(this.fStartOffset));
            ((TextImpl)this.fStartContainer).setNodeValueInternal(this.fStartContainer.getNodeValue().substring(0, this.fStartOffset));
            Node var7 = this.fStartContainer.getNextSibling();
            if (var7 != null) {
               if (var6 != null) {
                  var6.insertBefore(var1, var7);
                  var6.insertBefore(var3, var7);
               }
            } else if (var6 != null) {
               var6.appendChild(var1);
               var6.appendChild(var3);
            }

            if (this.fEndContainer == this.fStartContainer) {
               this.fEndContainer = var3;
               this.fEndOffset -= this.fStartOffset;
            } else if (this.fEndContainer == var6) {
               this.fEndOffset += var6.getChildNodes().getLength() - var5;
            }

            this.signalSplitData(this.fStartContainer, var3, this.fStartOffset);
         } else {
            if (this.fEndContainer == this.fStartContainer) {
               var5 = this.fEndContainer.getChildNodes().getLength();
            }

            Node var4 = this.fStartContainer.getFirstChild();
            boolean var8 = false;

            for(int var9 = 0; var9 < this.fStartOffset && var4 != null; ++var9) {
               var4 = var4.getNextSibling();
            }

            if (var4 != null) {
               this.fStartContainer.insertBefore(var1, var4);
            } else {
               this.fStartContainer.appendChild(var1);
            }

            if (this.fEndContainer == this.fStartContainer && this.fEndOffset != 0) {
               this.fEndOffset += this.fEndContainer.getChildNodes().getLength() - var5;
            }
         }

         this.fInsertedFromRange = false;
      }
   }

   public void surroundContents(Node var1) throws DOMException, RangeException {
      if (var1 != null) {
         short var2 = var1.getNodeType();
         if (this.fDocument.errorChecking) {
            if (this.fDetach) {
               throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
            }

            if (var2 == 2 || var2 == 6 || var2 == 12 || var2 == 10 || var2 == 9 || var2 == 11) {
               throw new RangeExceptionImpl((short)2, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_NODE_TYPE_ERR", (Object[])null));
            }
         }

         Node var3 = this.fStartContainer;
         Node var4 = this.fEndContainer;
         if (this.fStartContainer.getNodeType() == 3) {
            var3 = this.fStartContainer.getParentNode();
         }

         if (this.fEndContainer.getNodeType() == 3) {
            var4 = this.fEndContainer.getParentNode();
         }

         if (var3 != var4) {
            throw new RangeExceptionImpl((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "BAD_BOUNDARYPOINTS_ERR", (Object[])null));
         } else {
            DocumentFragment var5 = this.extractContents();
            this.insertNode(var1);
            var1.appendChild(var5);
            this.selectNode(var1);
         }
      }
   }

   public Range cloneRange() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         Range var1 = this.fDocument.createRange();
         var1.setStart(this.fStartContainer, this.fStartOffset);
         var1.setEnd(this.fEndContainer, this.fEndOffset);
         return var1;
      }
   }

   public String toString() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         Node var1 = this.fStartContainer;
         Node var2 = this.fEndContainer;
         StringBuffer var3 = new StringBuffer();
         int var4;
         if (this.fStartContainer.getNodeType() != 3 && this.fStartContainer.getNodeType() != 4) {
            var1 = var1.getFirstChild();
            if (this.fStartOffset > 0) {
               for(var4 = 0; var4 < this.fStartOffset && var1 != null; ++var4) {
                  var1 = var1.getNextSibling();
               }
            }

            if (var1 == null) {
               var1 = this.nextNode(this.fStartContainer, false);
            }
         } else {
            if (this.fStartContainer == this.fEndContainer) {
               var3.append(this.fStartContainer.getNodeValue().substring(this.fStartOffset, this.fEndOffset));
               return var3.toString();
            }

            var3.append(this.fStartContainer.getNodeValue().substring(this.fStartOffset));
            var1 = this.nextNode(var1, true);
         }

         if (this.fEndContainer.getNodeType() != 3 && this.fEndContainer.getNodeType() != 4) {
            var4 = this.fEndOffset;

            for(var2 = this.fEndContainer.getFirstChild(); var4 > 0 && var2 != null; var2 = var2.getNextSibling()) {
               --var4;
            }

            if (var2 == null) {
               var2 = this.nextNode(this.fEndContainer, false);
            }
         }

         for(; var1 != var2 && var1 != null; var1 = this.nextNode(var1, true)) {
            if (var1.getNodeType() == 3 || var1.getNodeType() == 4) {
               var3.append(var1.getNodeValue());
            }
         }

         if (this.fEndContainer.getNodeType() == 3 || this.fEndContainer.getNodeType() == 4) {
            var3.append(this.fEndContainer.getNodeValue().substring(0, this.fEndOffset));
         }

         return var3.toString();
      }
   }

   public void detach() {
      if (this.fDetach) {
         throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
      } else {
         this.fDetach = true;
         this.fDocument.removeRange(this);
      }
   }

   void signalSplitData(Node var1, Node var2, int var3) {
      this.fSplitNode = var1;
      this.fDocument.splitData(var1, var2, var3);
      this.fSplitNode = null;
   }

   void receiveSplitData(Node var1, Node var2, int var3) {
      if (var1 != null && var2 != null) {
         if (this.fSplitNode != var1) {
            if (var1 == this.fStartContainer && this.fStartContainer.getNodeType() == 3 && this.fStartOffset > var3) {
               this.fStartOffset -= var3;
               this.fStartContainer = var2;
            }

            if (var1 == this.fEndContainer && this.fEndContainer.getNodeType() == 3 && this.fEndOffset > var3) {
               this.fEndOffset -= var3;
               this.fEndContainer = var2;
            }

         }
      }
   }

   void deleteData(CharacterData var1, int var2, int var3) {
      this.fDeleteNode = var1;
      var1.deleteData(var2, var3);
      this.fDeleteNode = null;
   }

   void receiveDeletedText(Node var1, int var2, int var3) {
      if (var1 != null) {
         if (this.fDeleteNode != var1) {
            if (var1 == this.fStartContainer && this.fStartContainer.getNodeType() == 3) {
               if (this.fStartOffset > var2 + var3) {
                  this.fStartOffset = var2 + (this.fStartOffset - (var2 + var3));
               } else if (this.fStartOffset > var2) {
                  this.fStartOffset = var2;
               }
            }

            if (var1 == this.fEndContainer && this.fEndContainer.getNodeType() == 3) {
               if (this.fEndOffset > var2 + var3) {
                  this.fEndOffset = var2 + (this.fEndOffset - (var2 + var3));
               } else if (this.fEndOffset > var2) {
                  this.fEndOffset = var2;
               }
            }

         }
      }
   }

   void insertData(CharacterData var1, int var2, String var3) {
      this.fInsertNode = var1;
      var1.insertData(var2, var3);
      this.fInsertNode = null;
   }

   void receiveInsertedText(Node var1, int var2, int var3) {
      if (var1 != null) {
         if (this.fInsertNode != var1) {
            if (var1 == this.fStartContainer && this.fStartContainer.getNodeType() == 3 && var2 < this.fStartOffset) {
               this.fStartOffset += var3;
            }

            if (var1 == this.fEndContainer && this.fEndContainer.getNodeType() == 3 && var2 < this.fEndOffset) {
               this.fEndOffset += var3;
            }

         }
      }
   }

   void receiveReplacedText(Node var1) {
      if (var1 != null) {
         if (var1 == this.fStartContainer && this.fStartContainer.getNodeType() == 3) {
            this.fStartOffset = 0;
         }

         if (var1 == this.fEndContainer && this.fEndContainer.getNodeType() == 3) {
            this.fEndOffset = 0;
         }

      }
   }

   public void insertedNodeFromDOM(Node var1) {
      if (var1 != null) {
         if (this.fInsertNode != var1) {
            if (!this.fInsertedFromRange) {
               Node var2 = var1.getParentNode();
               int var3;
               if (var2 == this.fStartContainer) {
                  var3 = this.indexOf(var1, this.fStartContainer);
                  if (var3 < this.fStartOffset) {
                     ++this.fStartOffset;
                  }
               }

               if (var2 == this.fEndContainer) {
                  var3 = this.indexOf(var1, this.fEndContainer);
                  if (var3 < this.fEndOffset) {
                     ++this.fEndOffset;
                  }
               }

            }
         }
      }
   }

   Node removeChild(Node var1, Node var2) {
      this.fRemoveChild = var2;
      Node var3 = var1.removeChild(var2);
      this.fRemoveChild = null;
      return var3;
   }

   void removeNode(Node var1) {
      if (var1 != null) {
         if (this.fRemoveChild != var1) {
            Node var2 = var1.getParentNode();
            int var3;
            if (var2 == this.fStartContainer) {
               var3 = this.indexOf(var1, this.fStartContainer);
               if (var3 < this.fStartOffset) {
                  --this.fStartOffset;
               }
            }

            if (var2 == this.fEndContainer) {
               var3 = this.indexOf(var1, this.fEndContainer);
               if (var3 < this.fEndOffset) {
                  --this.fEndOffset;
               }
            }

            if (var2 != this.fStartContainer || var2 != this.fEndContainer) {
               if (this.isAncestorOf(var1, this.fStartContainer)) {
                  this.fStartContainer = var2;
                  this.fStartOffset = this.indexOf(var1, var2);
               }

               if (this.isAncestorOf(var1, this.fEndContainer)) {
                  this.fEndContainer = var2;
                  this.fEndOffset = this.indexOf(var1, var2);
               }
            }

         }
      }
   }

   private DocumentFragment traverseContents(int var1) throws DOMException {
      if (this.fStartContainer != null && this.fEndContainer != null) {
         if (this.fDetach) {
            throw new DOMException((short)11, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_STATE_ERR", (Object[])null));
         } else if (this.fStartContainer == this.fEndContainer) {
            return this.traverseSameContainer(var1);
         } else {
            int var2 = 0;
            Node var3 = this.fEndContainer;

            for(Node var4 = var3.getParentNode(); var4 != null; var4 = var4.getParentNode()) {
               if (var4 == this.fStartContainer) {
                  return this.traverseCommonStartContainer(var3, var1);
               }

               ++var2;
               var3 = var4;
            }

            int var5 = 0;
            Node var6 = this.fStartContainer;

            for(Node var7 = var6.getParentNode(); var7 != null; var7 = var7.getParentNode()) {
               if (var7 == this.fEndContainer) {
                  return this.traverseCommonEndContainer(var6, var1);
               }

               ++var5;
               var6 = var7;
            }

            int var8 = var5 - var2;

            Node var9;
            for(var9 = this.fStartContainer; var8 > 0; --var8) {
               var9 = var9.getParentNode();
            }

            Node var10;
            for(var10 = this.fEndContainer; var8 < 0; ++var8) {
               var10 = var10.getParentNode();
            }

            Node var11 = var9.getParentNode();

            for(Node var12 = var10.getParentNode(); var11 != var12; var12 = var12.getParentNode()) {
               var9 = var11;
               var10 = var12;
               var11 = var11.getParentNode();
            }

            return this.traverseCommonAncestors(var9, var10, var1);
         }
      } else {
         return null;
      }
   }

   private DocumentFragment traverseSameContainer(int var1) {
      DocumentFragment var2 = null;
      if (var1 != 3) {
         var2 = this.fDocument.createDocumentFragment();
      }

      if (this.fStartOffset == this.fEndOffset) {
         return var2;
      } else if (this.fStartContainer.getNodeType() == 3) {
         String var7 = this.fStartContainer.getNodeValue();
         String var8 = var7.substring(this.fStartOffset, this.fEndOffset);
         if (var1 != 2) {
            ((TextImpl)this.fStartContainer).deleteData(this.fStartOffset, this.fEndOffset - this.fStartOffset);
            this.collapse(true);
         }

         if (var1 == 3) {
            return null;
         } else {
            var2.appendChild(this.fDocument.createTextNode(var8));
            return var2;
         }
      } else {
         Node var3 = this.getSelectedNode(this.fStartContainer, this.fStartOffset);

         Node var5;
         for(int var4 = this.fEndOffset - this.fStartOffset; var4 > 0; var3 = var5) {
            var5 = var3.getNextSibling();
            Node var6 = this.traverseFullySelected(var3, var1);
            if (var2 != null) {
               var2.appendChild(var6);
            }

            --var4;
         }

         if (var1 != 2) {
            this.collapse(true);
         }

         return var2;
      }
   }

   private DocumentFragment traverseCommonStartContainer(Node var1, int var2) {
      DocumentFragment var3 = null;
      if (var2 != 3) {
         var3 = this.fDocument.createDocumentFragment();
      }

      Node var4 = this.traverseRightBoundary(var1, var2);
      if (var3 != null) {
         var3.appendChild(var4);
      }

      int var5 = this.indexOf(var1, this.fStartContainer);
      int var6 = var5 - this.fStartOffset;
      if (var6 <= 0) {
         if (var2 != 2) {
            this.setEndBefore(var1);
            this.collapse(false);
         }

         return var3;
      } else {
         Node var7;
         for(var4 = var1.getPreviousSibling(); var6 > 0; var4 = var7) {
            var7 = var4.getPreviousSibling();
            Node var8 = this.traverseFullySelected(var4, var2);
            if (var3 != null) {
               var3.insertBefore(var8, var3.getFirstChild());
            }

            --var6;
         }

         if (var2 != 2) {
            this.setEndBefore(var1);
            this.collapse(false);
         }

         return var3;
      }
   }

   private DocumentFragment traverseCommonEndContainer(Node var1, int var2) {
      DocumentFragment var3 = null;
      if (var2 != 3) {
         var3 = this.fDocument.createDocumentFragment();
      }

      Node var4 = this.traverseLeftBoundary(var1, var2);
      if (var3 != null) {
         var3.appendChild(var4);
      }

      int var5 = this.indexOf(var1, this.fEndContainer);
      ++var5;
      int var6 = this.fEndOffset - var5;

      Node var7;
      for(var4 = var1.getNextSibling(); var6 > 0; var4 = var7) {
         var7 = var4.getNextSibling();
         Node var8 = this.traverseFullySelected(var4, var2);
         if (var3 != null) {
            var3.appendChild(var8);
         }

         --var6;
      }

      if (var2 != 2) {
         this.setStartAfter(var1);
         this.collapse(true);
      }

      return var3;
   }

   private DocumentFragment traverseCommonAncestors(Node var1, Node var2, int var3) {
      DocumentFragment var4 = null;
      if (var3 != 3) {
         var4 = this.fDocument.createDocumentFragment();
      }

      Node var5 = this.traverseLeftBoundary(var1, var3);
      if (var4 != null) {
         var4.appendChild(var5);
      }

      Node var6 = var1.getParentNode();
      int var7 = this.indexOf(var1, var6);
      int var8 = this.indexOf(var2, var6);
      ++var7;
      int var9 = var8 - var7;

      for(Node var10 = var1.getNextSibling(); var9 > 0; --var9) {
         Node var11 = var10.getNextSibling();
         var5 = this.traverseFullySelected(var10, var3);
         if (var4 != null) {
            var4.appendChild(var5);
         }

         var10 = var11;
      }

      var5 = this.traverseRightBoundary(var2, var3);
      if (var4 != null) {
         var4.appendChild(var5);
      }

      if (var3 != 2) {
         this.setStartAfter(var1);
         this.collapse(true);
      }

      return var4;
   }

   private Node traverseRightBoundary(Node var1, int var2) {
      Node var3 = this.getSelectedNode(this.fEndContainer, this.fEndOffset - 1);
      boolean var4 = var3 != this.fEndContainer;
      if (var3 == var1) {
         return this.traverseNode(var3, var4, false, var2);
      } else {
         Node var5 = var3.getParentNode();

         Node var7;
         for(Node var6 = this.traverseNode(var5, false, false, var2); var5 != null; var6 = var7) {
            while(var3 != null) {
               var7 = var3.getPreviousSibling();
               Node var8 = this.traverseNode(var3, var4, false, var2);
               if (var2 != 3) {
                  var6.insertBefore(var8, var6.getFirstChild());
               }

               var4 = true;
               var3 = var7;
            }

            if (var5 == var1) {
               return var6;
            }

            var3 = var5.getPreviousSibling();
            var5 = var5.getParentNode();
            var7 = this.traverseNode(var5, false, false, var2);
            if (var2 != 3) {
               var7.appendChild(var6);
            }
         }

         return null;
      }
   }

   private Node traverseLeftBoundary(Node var1, int var2) {
      Node var3 = this.getSelectedNode(this.getStartContainer(), this.getStartOffset());
      boolean var4 = var3 != this.getStartContainer();
      if (var3 == var1) {
         return this.traverseNode(var3, var4, true, var2);
      } else {
         Node var5 = var3.getParentNode();

         Node var7;
         for(Node var6 = this.traverseNode(var5, false, true, var2); var5 != null; var6 = var7) {
            while(var3 != null) {
               var7 = var3.getNextSibling();
               Node var8 = this.traverseNode(var3, var4, true, var2);
               if (var2 != 3) {
                  var6.appendChild(var8);
               }

               var4 = true;
               var3 = var7;
            }

            if (var5 == var1) {
               return var6;
            }

            var3 = var5.getNextSibling();
            var5 = var5.getParentNode();
            var7 = this.traverseNode(var5, false, true, var2);
            if (var2 != 3) {
               var7.appendChild(var6);
            }
         }

         return null;
      }
   }

   private Node traverseNode(Node var1, boolean var2, boolean var3, int var4) {
      if (var2) {
         return this.traverseFullySelected(var1, var4);
      } else {
         return var1.getNodeType() == 3 ? this.traverseTextNode(var1, var3, var4) : this.traversePartiallySelected(var1, var4);
      }
   }

   private Node traverseFullySelected(Node var1, int var2) {
      switch (var2) {
         case 1:
            if (var1.getNodeType() == 10) {
               throw new DOMException((short)3, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "HIERARCHY_REQUEST_ERR", (Object[])null));
            }

            return var1;
         case 2:
            return var1.cloneNode(true);
         case 3:
            var1.getParentNode().removeChild(var1);
            return null;
         default:
            return null;
      }
   }

   private Node traversePartiallySelected(Node var1, int var2) {
      switch (var2) {
         case 1:
         case 2:
            return var1.cloneNode(false);
         case 3:
            return null;
         default:
            return null;
      }
   }

   private Node traverseTextNode(Node var1, boolean var2, int var3) {
      String var4 = var1.getNodeValue();
      String var5;
      String var6;
      int var7;
      if (var2) {
         var7 = this.getStartOffset();
         var5 = var4.substring(var7);
         var6 = var4.substring(0, var7);
      } else {
         var7 = this.getEndOffset();
         var5 = var4.substring(0, var7);
         var6 = var4.substring(var7);
      }

      if (var3 != 2) {
         var1.setNodeValue(var6);
      }

      if (var3 == 3) {
         return null;
      } else {
         Node var8 = var1.cloneNode(false);
         var8.setNodeValue(var5);
         return var8;
      }
   }

   void checkIndex(Node var1, int var2) throws DOMException {
      if (var2 < 0) {
         throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", (Object[])null));
      } else {
         short var3 = var1.getNodeType();
         if (var3 != 3 && var3 != 4 && var3 != 8 && var3 != 7) {
            if (var2 > var1.getChildNodes().getLength()) {
               throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", (Object[])null));
            }
         } else if (var2 > var1.getNodeValue().length()) {
            throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", (Object[])null));
         }

      }
   }

   private Node getRootContainer(Node var1) {
      if (var1 == null) {
         return null;
      } else {
         while(var1.getParentNode() != null) {
            var1 = var1.getParentNode();
         }

         return var1;
      }
   }

   private boolean isLegalContainer(Node var1) {
      if (var1 == null) {
         return false;
      } else {
         while(var1 != null) {
            switch (var1.getNodeType()) {
               case 6:
               case 10:
               case 12:
                  return false;
               default:
                  var1 = var1.getParentNode();
            }
         }

         return true;
      }
   }

   private boolean hasLegalRootContainer(Node var1) {
      if (var1 == null) {
         return false;
      } else {
         Node var2 = this.getRootContainer(var1);
         switch (var2.getNodeType()) {
            case 2:
            case 9:
            case 11:
               return true;
            default:
               return false;
         }
      }
   }

   private boolean isLegalContainedNode(Node var1) {
      if (var1 == null) {
         return false;
      } else {
         switch (var1.getNodeType()) {
            case 2:
            case 6:
            case 9:
            case 11:
            case 12:
               return false;
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            case 10:
            default:
               return true;
         }
      }
   }

   Node nextNode(Node var1, boolean var2) {
      if (var1 == null) {
         return null;
      } else {
         Node var3;
         if (var2) {
            var3 = var1.getFirstChild();
            if (var3 != null) {
               return var3;
            }
         }

         var3 = var1.getNextSibling();
         if (var3 != null) {
            return var3;
         } else {
            for(Node var4 = var1.getParentNode(); var4 != null && var4 != this.fDocument; var4 = var4.getParentNode()) {
               var3 = var4.getNextSibling();
               if (var3 != null) {
                  return var3;
               }
            }

            return null;
         }
      }
   }

   boolean isAncestorOf(Node var1, Node var2) {
      for(Node var3 = var2; var3 != null; var3 = var3.getParentNode()) {
         if (var3 == var1) {
            return true;
         }
      }

      return false;
   }

   int indexOf(Node var1, Node var2) {
      if (var1.getParentNode() != var2) {
         return -1;
      } else {
         int var3 = 0;

         for(Node var4 = var2.getFirstChild(); var4 != var1; var4 = var4.getNextSibling()) {
            ++var3;
         }

         return var3;
      }
   }

   private Node getSelectedNode(Node var1, int var2) {
      if (var1.getNodeType() == 3) {
         return var1;
      } else if (var2 < 0) {
         return var1;
      } else {
         Node var3;
         for(var3 = var1.getFirstChild(); var3 != null && var2 > 0; var3 = var3.getNextSibling()) {
            --var2;
         }

         return var3 != null ? var3 : var1;
      }
   }
}
