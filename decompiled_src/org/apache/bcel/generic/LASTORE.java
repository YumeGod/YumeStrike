package org.apache.bcel.generic;

public class LASTORE extends ArrayInstruction implements StackConsumer {
   public LASTORE() {
      super((short)80);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitArrayInstruction(this);
      v.visitLASTORE(this);
   }
}
