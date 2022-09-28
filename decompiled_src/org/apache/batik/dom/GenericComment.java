package org.apache.batik.dom;

import org.w3c.dom.Node;

public class GenericComment extends AbstractComment {
   protected boolean readonly;

   public GenericComment() {
   }

   public GenericComment(String var1, AbstractDocument var2) {
      this.ownerDocument = var2;
      this.setNodeValue(var1);
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Node newNode() {
      return new GenericComment();
   }
}
