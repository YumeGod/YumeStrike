package org.apache.bcel.generic;

public class L2D extends ConversionInstruction {
   public L2D() {
      super((short)138);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitConversionInstruction(this);
      v.visitL2D(this);
   }
}
