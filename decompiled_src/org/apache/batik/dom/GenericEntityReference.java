package org.apache.batik.dom;

import org.w3c.dom.Node;

public class GenericEntityReference extends AbstractEntityReference {
   protected boolean readonly;

   protected GenericEntityReference() {
   }

   public GenericEntityReference(String var1, AbstractDocument var2) {
      super(var1, var2);
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Node newNode() {
      return new GenericEntityReference();
   }
}
