package org.apache.bcel.generic;

public class IOR extends ArithmeticInstruction {
   public IOR() {
      super((short)128);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitIOR(this);
   }
}
