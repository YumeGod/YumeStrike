package org.apache.bcel.generic;

public class DSUB extends ArithmeticInstruction {
   public DSUB() {
      super((short)103);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitDSUB(this);
   }
}
