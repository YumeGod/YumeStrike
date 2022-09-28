package org.apache.bcel.generic;

public class IALOAD extends ArrayInstruction implements StackProducer {
   public IALOAD() {
      super((short)46);
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitArrayInstruction(this);
      v.visitIALOAD(this);
   }
}
