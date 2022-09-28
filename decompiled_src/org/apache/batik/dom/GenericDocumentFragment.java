package org.apache.batik.dom;

import org.w3c.dom.Node;

public class GenericDocumentFragment extends AbstractDocumentFragment {
   protected boolean readonly;

   protected GenericDocumentFragment() {
   }

   public GenericDocumentFragment(AbstractDocument var1) {
      this.ownerDocument = var1;
   }

   public boolean isReadonly() {
      return this.readonly;
   }

   public void setReadonly(boolean var1) {
      this.readonly = var1;
   }

   protected Node newNode() {
      return new GenericDocumentFragment();
   }
}
