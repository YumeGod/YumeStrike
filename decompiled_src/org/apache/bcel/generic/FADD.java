package org.apache.bcel.generic;

public class FADD extends ArithmeticInstruction {
   public FADD() {
      super((short)98);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitFADD(this);
   }
}
