package org.apache.bcel.generic;

public class LMUL extends ArithmeticInstruction {
   public LMUL() {
      super((short)105);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitLMUL(this);
   }
}
