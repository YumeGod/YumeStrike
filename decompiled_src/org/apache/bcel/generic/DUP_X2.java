package org.apache.bcel.generic;

public class DUP_X2 extends StackInstruction {
   public DUP_X2() {
      super((short)91);
   }

   public void accept(Visitor v) {
      v.visitStackInstruction(this);
      v.visitDUP_X2(this);
   }
}
