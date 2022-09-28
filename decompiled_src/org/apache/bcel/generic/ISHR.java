package org.apache.bcel.generic;

public class ISHR extends ArithmeticInstruction {
   public ISHR() {
      super((short)122);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitISHR(this);
   }
}
