package org.apache.bcel.generic;

public class DDIV extends ArithmeticInstruction {
   public DDIV() {
      super((short)111);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitDDIV(this);
   }
}
