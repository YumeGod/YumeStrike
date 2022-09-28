package org.apache.bcel.generic;

public class I2D extends ConversionInstruction {
   public I2D() {
      super((short)135);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitConversionInstruction(this);
      v.visitI2D(this);
   }
}
