package org.apache.bcel.generic;

public class IADD extends ArithmeticInstruction {
   public IADD() {
      super((short)96);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitIADD(this);
   }
}
