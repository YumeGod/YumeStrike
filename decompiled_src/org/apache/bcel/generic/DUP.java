package org.apache.bcel.generic;

public class DUP extends StackInstruction implements PushInstruction {
   public DUP() {
      super((short)89);
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitPushInstruction(this);
      v.visitStackInstruction(this);
      v.visitDUP(this);
   }
}
