package org.apache.bcel.generic;

public class ARETURN extends ReturnInstruction {
   public ARETURN() {
      super((short)176);
   }

   public void accept(Visitor v) {
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitStackConsumer(this);
      v.visitReturnInstruction(this);
      v.visitARETURN(this);
   }
}
