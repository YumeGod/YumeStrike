package org.apache.bcel.generic;

public class LSHR extends ArithmeticInstruction {
   public LSHR() {
      super((short)123);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitLSHR(this);
   }
}
