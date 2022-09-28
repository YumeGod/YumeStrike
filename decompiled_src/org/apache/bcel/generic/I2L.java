package org.apache.bcel.generic;

public class I2L extends ConversionInstruction {
   public I2L() {
      super((short)133);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitConversionInstruction(this);
      v.visitI2L(this);
   }
}
