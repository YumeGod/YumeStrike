package org.apache.bcel.generic;

public class FASTORE extends ArrayInstruction implements StackConsumer {
   public FASTORE() {
      super((short)81);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitArrayInstruction(this);
      v.visitFASTORE(this);
   }
}
