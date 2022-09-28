package org.apache.bcel.generic;

public class D2I extends ConversionInstruction {
   public D2I() {
      super((short)142);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitConversionInstruction(this);
      v.visitD2I(this);
   }
}
