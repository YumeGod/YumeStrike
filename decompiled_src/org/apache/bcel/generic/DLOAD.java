package org.apache.bcel.generic;

public class DLOAD extends LoadInstruction {
   DLOAD() {
      super((short)24, (short)38);
   }

   public DLOAD(int n) {
      super((short)24, (short)38, n);
   }

   public void accept(Visitor v) {
      super.accept(v);
      v.visitDLOAD(this);
   }
}
