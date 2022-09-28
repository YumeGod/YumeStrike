package org.apache.bcel.generic;

public class LUSHR extends ArithmeticInstruction {
   public LUSHR() {
      super((short)125);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitLUSHR(this);
   }
}
