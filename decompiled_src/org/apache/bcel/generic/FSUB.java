package org.apache.bcel.generic;

public class FSUB extends ArithmeticInstruction {
   public FSUB() {
      super((short)102);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitFSUB(this);
   }
}
