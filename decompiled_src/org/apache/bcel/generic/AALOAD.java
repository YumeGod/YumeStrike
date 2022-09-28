package org.apache.bcel.generic;

public class AALOAD extends ArrayInstruction implements StackProducer {
   public AALOAD() {
      super((short)50);
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitArrayInstruction(this);
      v.visitAALOAD(this);
   }
}
