package org.apache.bcel.generic;

public class LALOAD extends ArrayInstruction implements StackProducer {
   public LALOAD() {
      super((short)47);
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitArrayInstruction(this);
      v.visitLALOAD(this);
   }
}
