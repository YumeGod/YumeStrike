package org.apache.batik.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class GenericAttr extends AbstractAttr {
   protected boolean readonly;

   protected GenericAttr() {
   }

   public GenericAttr(String var1, AbstractDocument var2) throws DOMException {
      super(var1, var2);
      this.setNodeName(var1);
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Node newNode() {
      return new GenericAttr();
   }
}
