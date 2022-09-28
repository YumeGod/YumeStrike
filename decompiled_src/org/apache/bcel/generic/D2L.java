package org.apache.bcel.generic;

public class D2L extends ConversionInstruction {
   public D2L() {
      super((short)143);
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitConversionInstruction(this);
      v.visitD2L(this);
   }
}
