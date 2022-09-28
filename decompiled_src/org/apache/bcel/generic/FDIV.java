package org.apache.bcel.generic;

public class FDIV extends ArithmeticInstruction {
   public FDIV() {
      super((short)110);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitFDIV(this);
   }
}
