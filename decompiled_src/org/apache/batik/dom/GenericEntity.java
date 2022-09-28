package org.apache.batik.dom;

import org.w3c.dom.Node;

public class GenericEntity extends AbstractEntity {
   protected boolean readonly;

   protected GenericEntity() {
   }

   public GenericEntity(String var1, String var2, String var3, AbstractDocument var4) {
      this.ownerDocument = var4;
      this.setNodeName(var1);
      this.setPublicId(var2);
      this.setSystemId(var3);
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Node newNode() {
      return new GenericEntity();
   }
}
