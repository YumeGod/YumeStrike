package org.apache.xerces.impl.dtd.models;

public class CMUniOp extends CMNode {
   private CMNode fChild;

   public CMUniOp(int var1, CMNode var2) {
      super(var1);
      if (this.type() != 1 && this.type() != 2 && this.type() != 3) {
         throw new RuntimeException("ImplementationMessages.VAL_UST");
      } else {
         this.fChild = var2;
      }
   }

   final CMNode getChild() {
      return this.fChild;
   }

   public boolean isNullable() {
      return this.type() == 3 ? this.fChild.isNullable() : true;
   }

   protected void calcFirstPos(CMStateSet var1) {
      var1.setTo(this.fChild.firstPos());
   }

   protected void calcLastPos(CMStateSet var1) {
      var1.setTo(this.fChild.lastPos());
   }
}
