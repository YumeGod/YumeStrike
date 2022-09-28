package org.apache.bcel.generic;

public class FRETURN extends ReturnInstruction {
   public FRETURN() {
      super((short)174);
   }

   public void accept(Visitor v) {
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitStackConsumer(this);
      v.visitReturnInstruction(this);
      v.visitFRETURN(this);
   }
}
