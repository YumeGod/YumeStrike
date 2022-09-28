package org.apache.bcel.generic;

public class BALOAD extends ArrayInstruction implements StackProducer {
   public BALOAD() {
      super((short)51);
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitArrayInstruction(this);
      v.visitBALOAD(this);
   }
}
