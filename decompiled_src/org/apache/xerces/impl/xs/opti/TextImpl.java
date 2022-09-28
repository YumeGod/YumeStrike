package org.apache.xerces.impl.xs.opti;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class TextImpl extends DefaultText {
   String fData = null;
   SchemaDOM fSchemaDOM = null;
   int fRow;
   int fCol;

   public TextImpl(StringBuffer var1, SchemaDOM var2, int var3, int var4) {
      this.fData = var1.toString();
      this.fSchemaDOM = var2;
      this.fRow = var3;
      this.fCol = var4;
      super.rawname = super.prefix = super.localpart = super.uri = null;
      super.nodeType = 3;
   }

   public Node getParentNode() {
      return this.fSchemaDOM.relations[this.fRow][0];
   }

   public Node getPreviousSibling() {
      return this.fCol == 1 ? null : this.fSchemaDOM.relations[this.fRow][this.fCol - 1];
   }

   public Node getNextSibling() {
      return this.fCol == this.fSchemaDOM.relations[this.fRow].length - 1 ? null : this.fSchemaDOM.relations[this.fRow][this.fCol + 1];
   }

   public String getData() throws DOMException {
      return this.fData;
   }

   public int getLength() {
      return this.fData == null ? 0 : this.fData.length();
   }

   public String substringData(int var1, int var2) throws DOMException {
      if (this.fData == null) {
         return null;
      } else if (var2 >= 0 && var1 >= 0 && var1 <= this.fData.length()) {
         return var1 + var2 >= this.fData.length() ? this.fData.substring(var1) : this.fData.substring(var1, var1 + var2);
      } else {
         throw new DOMException((short)1, "parameter error");
      }
   }
}
