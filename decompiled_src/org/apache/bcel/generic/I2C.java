package org.apache.bcel.generic;

public class I2C extends ConversionInstruction {
   public I2C() {
      super((short)146);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitConversionInstruction(this);
      v.visitI2C(this);
   }
}
