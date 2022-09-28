package org.apache.bcel.generic;

public class FLOAD extends LoadInstruction {
   FLOAD() {
      super((short)23, (short)34);
   }

   public FLOAD(int n) {
      super((short)23, (short)34, n);
   }

   public void accept(Visitor v) {
      super.accept(v);
      v.visitFLOAD(this);
   }
}
