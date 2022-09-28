package org.apache.bcel.generic;

public abstract class LoadInstruction extends LocalVariableInstruction implements PushInstruction {
   LoadInstruction(short canon_tag, short c_tag) {
      super(canon_tag, c_tag);
   }

   protected LoadInstruction(short opcode, short c_tag, int n) {
      super(opcode, c_tag, n);
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitPushInstruction(this);
      v.visitTypedInstruction(this);
      v.visitLocalVariableInstruction(this);
      v.visitLoadInstruction(this);
   }
}
