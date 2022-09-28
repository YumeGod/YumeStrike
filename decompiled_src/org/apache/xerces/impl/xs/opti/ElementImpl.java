package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class ElementImpl extends DefaultElement {
   SchemaDOM schemaDOM;
   Attr[] attrs;
   int row;
   int col;
   int parentRow;
   int line;
   int column;
   int charOffset;
   String fSyntheticAnnotation;

   public ElementImpl(int var1, int var2, int var3) {
      this.row = -1;
      this.col = -1;
      this.parentRow = -1;
      super.nodeType = 1;
      this.line = var1;
      this.column = var2;
      this.charOffset = var3;
   }

   public ElementImpl(int var1, int var2) {
      this(var1, var2, -1);
   }

   public ElementImpl(String var1, String var2, String var3, String var4, int var5, int var6, int var7) {
      super(var1, var2, var3, var4, (short)1);
      this.row = -1;
      this.col = -1;
      this.parentRow = -1;
      this.line = var5;
      this.column = var6;
      this.charOffset = var7;
   }

   public ElementImpl(String var1, String var2, String var3, String var4, int var5, int var6) {
      this(var1, var2, var3, var4, var5, var6, -1);
   }

   public Document getOwnerDocument() {
      return this.schemaDOM;
   }

   public Node getParentNode() {
      return this.schemaDOM.relations[this.row][0];
   }

   public boolean hasChildNodes() {
      return this.parentRow != -1;
   }

   public Node getFirstChild() {
      return this.parentRow == -1 ? null : this.schemaDOM.relations[this.parentRow][1];
   }

   public Node getLastChild() {
      if (this.parentRow == -1) {
         return null;
      } else {
         int var1;
         for(var1 = 1; var1 < this.schemaDOM.relations[this.parentRow].length; ++var1) {
            if (this.schemaDOM.relations[this.parentRow][var1] == null) {
               return this.schemaDOM.relations[this.parentRow][var1 - 1];
            }
         }

         if (var1 == 1) {
            ++var1;
         }

         return this.schemaDOM.relations[this.parentRow][var1 - 1];
      }
   }

   public Node getPreviousSibling() {
      return this.col == 1 ? null : this.schemaDOM.relations[this.row][this.col - 1];
   }

   public Node getNextSibling() {
      return this.col == this.schemaDOM.relations[this.row].length - 1 ? null : this.schemaDOM.relations[this.row][this.col + 1];
   }

   public NamedNodeMap getAttributes() {
      return new NamedNodeMapImpl(this.attrs);
   }

   public boolean hasAttributes() {
      return this.attrs.length != 0;
   }

   public String getTagName() {
      return super.rawname;
   }

   public String getAttribute(String var1) {
      for(int var2 = 0; var2 < this.attrs.length; ++var2) {
         if (this.attrs[var2].getName().equals(var1)) {
            return this.attrs[var2].getValue();
         }
      }

      return "";
   }

   public Attr getAttributeNode(String var1) {
      for(int var2 = 0; var2 < this.attrs.length; ++var2) {
         if (this.attrs[var2].getName().equals(var1)) {
            return this.attrs[var2];
         }
      }

      return null;
   }

   public String getAttributeNS(String var1, String var2) {
      for(int var3 = 0; var3 < this.attrs.length; ++var3) {
         if (this.attrs[var3].getLocalName().equals(var2) && this.attrs[var3].getNamespaceURI().equals(var1)) {
            return this.attrs[var3].getValue();
         }
      }

      return "";
   }

   public Attr getAttributeNodeNS(String var1, String var2) {
      for(int var3 = 0; var3 < this.attrs.length; ++var3) {
         if (this.attrs[var3].getName().equals(var2) && this.attrs[var3].getNamespaceURI().equals(var1)) {
            return this.attrs[var3];
         }
      }

      return null;
   }

   public boolean hasAttribute(String var1) {
      for(int var2 = 0; var2 < this.attrs.length; ++var2) {
         if (this.attrs[var2].getName().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasAttributeNS(String var1, String var2) {
      for(int var3 = 0; var3 < this.attrs.length; ++var3) {
         if (this.attrs[var3].getName().equals(var2) && this.attrs[var3].getNamespaceURI().equals(var1)) {
            return true;
         }
      }

      return false;
   }

   public void setAttribute(String var1, String var2) {
      for(int var3 = 0; var3 < this.attrs.length; ++var3) {
         if (this.attrs[var3].getName().equals(var1)) {
            this.attrs[var3].setValue(var2);
            return;
         }
      }

   }

   public int getLineNumber() {
      return this.line;
   }

   public int getColumnNumber() {
      return this.column;
   }

   public int getCharacterOffset() {
      return this.charOffset;
   }

   public String getSyntheticAnnotation() {
      return this.fSyntheticAnnotation;
   }
}
