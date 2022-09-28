package org.apache.bcel.generic;

public class I2S extends ConversionInstruction {
   public I2S() {
      super((short)147);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitConversionInstruction(this);
      v.visitI2S(this);
   }
}
