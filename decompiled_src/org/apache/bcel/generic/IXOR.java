package org.apache.bcel.generic;

public class IXOR extends ArithmeticInstruction {
   public IXOR() {
      super((short)130);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitIXOR(this);
   }
}
