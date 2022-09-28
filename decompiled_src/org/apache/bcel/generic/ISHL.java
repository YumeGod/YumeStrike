package org.apache.bcel.generic;

public class ISHL extends ArithmeticInstruction {
   public ISHL() {
      super((short)120);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitISHL(this);
   }
}
