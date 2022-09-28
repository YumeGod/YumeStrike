package org.apache.bcel.generic;

public class LLOAD extends LoadInstruction {
   LLOAD() {
      super((short)22, (short)30);
   }

   public LLOAD(int n) {
      super((short)22, (short)30, n);
   }

   public void accept(Visitor v) {
      super.accept(v);
      v.visitLLOAD(this);
   }
}
