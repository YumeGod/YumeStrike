package org.apache.bcel.generic;

public class IASTORE extends ArrayInstruction implements StackConsumer {
   public IASTORE() {
      super((short)79);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitArrayInstruction(this);
      v.visitIASTORE(this);
   }
}
