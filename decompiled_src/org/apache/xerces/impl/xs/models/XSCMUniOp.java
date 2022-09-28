package org.apache.xerces.impl.xs.models;

import org.apache.xerces.impl.dtd.models.CMNode;
import org.apache.xerces.impl.dtd.models.CMStateSet;

public class XSCMUniOp extends CMNode {
   private CMNode fChild;

   public XSCMUniOp(int var1, CMNode var2) {
      super(var1);
      if (this.type() != 5 && this.type() != 4 && this.type() != 6) {
         throw new RuntimeException("ImplementationMessages.VAL_UST");
      } else {
         this.fChild = var2;
      }
   }

   final CMNode getChild() {
      return this.fChild;
   }

   public boolean isNullable() {
      return this.type() == 6 ? this.fChild.isNullable() : true;
   }

   protected void calcFirstPos(CMStateSet var1) {
      var1.setTo(this.fChild.firstPos());
   }

   protected void calcLastPos(CMStateSet var1) {
      var1.setTo(this.fChild.lastPos());
   }
}
