package org.apache.bcel.generic;

public class FSTORE extends StoreInstruction {
   FSTORE() {
      super((short)56, (short)67);
   }

   public FSTORE(int n) {
      super((short)56, (short)67, n);
   }

   public void accept(Visitor v) {
      super.accept(v);
      v.visitFSTORE(this);
   }
}
