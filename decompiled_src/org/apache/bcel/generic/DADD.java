package org.apache.bcel.generic;

public class DADD extends ArithmeticInstruction {
   public DADD() {
      super((short)99);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitDADD(this);
   }
}
