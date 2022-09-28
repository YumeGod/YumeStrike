package org.apache.bcel.generic;

public class DRETURN extends ReturnInstruction {
   public DRETURN() {
      super((short)175);
   }

   public void accept(Visitor v) {
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitStackConsumer(this);
      v.visitReturnInstruction(this);
      v.visitDRETURN(this);
   }
}
