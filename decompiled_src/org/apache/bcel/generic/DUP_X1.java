package org.apache.bcel.generic;

public class DUP_X1 extends StackInstruction {
   public DUP_X1() {
      super((short)90);
   }

   public void accept(Visitor v) {
      v.visitStackInstruction(this);
      v.visitDUP_X1(this);
   }
}
