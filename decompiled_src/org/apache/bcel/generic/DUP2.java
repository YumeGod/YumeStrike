package org.apache.bcel.generic;

public class DUP2 extends StackInstruction implements PushInstruction {
   public DUP2() {
      super((short)92);
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitPushInstruction(this);
      v.visitStackInstruction(this);
      v.visitDUP2(this);
   }
}
