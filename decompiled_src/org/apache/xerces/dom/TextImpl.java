package org.apache.xerces.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class TextImpl extends CharacterDataImpl implements CharacterData, Text {
   static final long serialVersionUID = -5294980852957403469L;

   public TextImpl() {
   }

   public TextImpl(CoreDocumentImpl var1, String var2) {
      super(var1, var2);
   }

   public void setValues(CoreDocumentImpl var1, String var2) {
      super.flags = 0;
      super.nextSibling = null;
      super.previousSibling = null;
      this.setOwnerDocument(var1);
      super.data = var2;
   }

   public short getNodeType() {
      return 3;
   }

   public String getNodeName() {
      return "#text";
   }

   public void setIgnorableWhitespace(boolean var1) {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      this.isIgnorableWhitespace(var1);
   }

   public boolean isElementContentWhitespace() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.internalIsIgnorableWhitespace();
   }

   public String getWholeText() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      if (super.fBufferStr == null) {
         super.fBufferStr = new StringBuffer();
      } else {
         super.fBufferStr.setLength(0);
      }

      if (super.data != null && super.data.length() != 0) {
         super.fBufferStr.append(super.data);
      }

      this.getWholeTextBackward(this.getPreviousSibling(), super.fBufferStr, this.getParentNode());
      String var1 = super.fBufferStr.toString();
      super.fBufferStr.setLength(0);
      this.getWholeTextForward(this.getNextSibling(), super.fBufferStr, this.getParentNode());
      return var1 + super.fBufferStr.toString();
   }

   protected void insertTextContent(StringBuffer var1) throws DOMException {
      String var2 = this.getNodeValue();
      if (var2 != null) {
         var1.insert(0, var2);
      }

   }

   private boolean getWholeTextForward(Node var1, StringBuffer var2, Node var3) {
      boolean var4 = false;
      if (var3 != null) {
         var4 = var3.getNodeType() == 5;
      }

      for(; var1 != null; var1 = var1.getNextSibling()) {
         short var5 = var1.getNodeType();
         if (var5 == 5) {
            if (this.getWholeTextForward(var1.getFirstChild(), var2, var1)) {
               return true;
            }
         } else {
            if (var5 != 3 && var5 != 4) {
               return true;
            }

            ((NodeImpl)var1).getTextContent(var2);
         }
      }

      if (var4) {
         this.getWholeTextForward(var3.getNextSibling(), var2, var3.getParentNode());
         return true;
      } else {
         return false;
      }
   }

   private boolean getWholeTextBackward(Node var1, StringBuffer var2, Node var3) {
      boolean var4 = false;
      if (var3 != null) {
         var4 = var3.getNodeType() == 5;
      }

      for(; var1 != null; var1 = var1.getPreviousSibling()) {
         short var5 = var1.getNodeType();
         if (var5 == 5) {
            if (this.getWholeTextBackward(var1.getLastChild(), var2, var1)) {
               return true;
            }
         } else {
            if (var5 != 3 && var5 != 4) {
               return true;
            }

            ((TextImpl)var1).insertTextContent(var2);
         }
      }

      if (var4) {
         this.getWholeTextBackward(var3.getPreviousSibling(), var2, var3.getParentNode());
         return true;
      } else {
         return false;
      }
   }

   public Text replaceWholeText(String var1) throws DOMException {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      Node var2 = this.getParentNode();
      if (var1 != null && var1.length() != 0) {
         if (this.ownerDocument().errorChecking) {
            if (!this.canModifyPrev(this)) {
               throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null));
            }

            if (!this.canModifyNext(this)) {
               throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null));
            }
         }

         Object var3 = null;
         if (this.isReadOnly()) {
            Text var4 = this.ownerDocument().createTextNode(var1);
            if (var2 == null) {
               return var4;
            }

            var2.insertBefore(var4, this);
            var2.removeChild(this);
            var3 = var4;
         } else {
            this.setData(var1);
            var3 = this;
         }

         for(Node var6 = ((Node)var3).getPreviousSibling(); var6 != null && (var6.getNodeType() == 3 || var6.getNodeType() == 4 || var6.getNodeType() == 5 && this.hasTextOnlyChildren(var6)); var6 = ((Node)var3).getPreviousSibling()) {
            var2.removeChild(var6);
         }

         for(Node var5 = ((Node)var3).getNextSibling(); var5 != null && (var5.getNodeType() == 3 || var5.getNodeType() == 4 || var5.getNodeType() == 5 && this.hasTextOnlyChildren(var5)); var5 = ((Node)var3).getNextSibling()) {
            var2.removeChild(var5);
         }

         return (Text)var3;
      } else {
         if (var2 != null) {
            var2.removeChild(this);
         }

         return null;
      }
   }

   private boolean canModifyPrev(Node var1) {
      boolean var2 = false;

      for(Node var3 = var1.getPreviousSibling(); var3 != null; var3 = var3.getPreviousSibling()) {
         short var4 = var3.getNodeType();
         if (var4 != 5) {
            if (var4 != 3 && var4 != 4) {
               return true;
            }
         } else {
            Node var5 = var3.getLastChild();
            if (var5 == null) {
               return false;
            }

            for(; var5 != null; var5 = var5.getPreviousSibling()) {
               short var6 = var5.getNodeType();
               if (var6 != 3 && var6 != 4) {
                  if (var6 != 5) {
                     if (var2) {
                        return false;
                     }

                     return true;
                  }

                  if (!this.canModifyPrev(var5)) {
                     return false;
                  }

                  var2 = true;
               } else {
                  var2 = true;
               }
            }
         }
      }

      return true;
   }

   private boolean canModifyNext(Node var1) {
      boolean var2 = false;

      for(Node var3 = var1.getNextSibling(); var3 != null; var3 = var3.getNextSibling()) {
         short var4 = var3.getNodeType();
         if (var4 != 5) {
            if (var4 != 3 && var4 != 4) {
               return true;
            }
         } else {
            Node var5 = var3.getFirstChild();
            if (var5 == null) {
               return false;
            }

            for(; var5 != null; var5 = var5.getNextSibling()) {
               short var6 = var5.getNodeType();
               if (var6 != 3 && var6 != 4) {
                  if (var6 != 5) {
                     if (var2) {
                        return false;
                     }

                     return true;
                  }

                  if (!this.canModifyNext(var5)) {
                     return false;
                  }

                  var2 = true;
               } else {
                  var2 = true;
               }
            }
         }
      }

      return true;
   }

   private boolean hasTextOnlyChildren(Node var1) {
      if (var1 == null) {
         return false;
      } else {
         for(Node var2 = var1.getFirstChild(); var2 != null; var2 = var2.getNextSibling()) {
            short var3 = var2.getNodeType();
            if (var3 == 5) {
               return this.hasTextOnlyChildren(var2);
            }

            if (var3 != 3 && var3 != 4 && var3 != 5) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean isIgnorableWhitespace() {
      if (this.needsSyncData()) {
         this.synchronizeData();
      }

      return this.internalIsIgnorableWhitespace();
   }

   public Text splitText(int var1) throws DOMException {
      if (this.isReadOnly()) {
         throw new DOMException((short)7, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", (Object[])null));
      } else {
         if (this.needsSyncData()) {
            this.synchronizeData();
         }

         if (var1 >= 0 && var1 <= super.data.length()) {
            Text var2 = this.getOwnerDocument().createTextNode(super.data.substring(var1));
            this.setNodeValue(super.data.substring(0, var1));
            Node var3 = this.getParentNode();
            if (var3 != null) {
               var3.insertBefore(var2, super.nextSibling);
            }

            return var2;
         } else {
            throw new DOMException((short)1, DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", (Object[])null));
         }
      }
   }

   public void replaceData(String var1) {
      super.data = var1;
   }

   public String removeData() {
      String var1 = super.data;
      super.data = "";
      return var1;
   }
}
