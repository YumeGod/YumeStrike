package org.apache.bcel.generic;

public class LSHL extends ArithmeticInstruction {
   public LSHL() {
      super((short)121);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitLSHL(this);
   }
}
