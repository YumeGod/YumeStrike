package org.apache.bcel.generic;

public class ISTORE extends StoreInstruction {
   ISTORE() {
      super((short)54, (short)59);
   }

   public ISTORE(int n) {
      super((short)54, (short)59, n);
   }

   public void accept(Visitor v) {
      super.accept(v);
      v.visitISTORE(this);
   }
}
