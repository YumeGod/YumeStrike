package org.apache.xerces.dom;

import java.io.Serializable;

class NodeListCache implements Serializable {
   private static final long serialVersionUID = 3258135743263224377L;
   int fLength = -1;
   int fChildIndex = -1;
   ChildNode fChild;
   ParentNode fOwner;
   NodeListCache next;

   NodeListCache(ParentNode var1) {
      this.fOwner = var1;
   }
}
