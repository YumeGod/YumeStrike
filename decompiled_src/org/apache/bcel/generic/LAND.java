package org.apache.bcel.generic;

public class LAND extends ArithmeticInstruction {
   public LAND() {
      super((short)127);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitLAND(this);
   }
}
