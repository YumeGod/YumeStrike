package org.apache.bcel.generic;

public class NOP extends Instruction {
   public NOP() {
      super((short)0, (short)1);
   }

   public void accept(Visitor v) {
      v.visitNOP(this);
   }
}
