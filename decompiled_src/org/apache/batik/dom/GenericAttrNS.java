package org.apache.batik.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public class GenericAttrNS extends AbstractAttrNS {
   protected boolean readonly;

   protected GenericAttrNS() {
   }

   public GenericAttrNS(String var1, String var2, AbstractDocument var3) throws DOMException {
      super(var1, var2, var3);
      this.setNodeName(var2);
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Node newNode() {
      return new GenericAttrNS();
   }
}
