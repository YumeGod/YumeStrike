package org.apache.bcel.generic;

public abstract class StoreInstruction extends LocalVariableInstruction implements PopInstruction {
   StoreInstruction(short canon_tag, short c_tag) {
      super(canon_tag, c_tag);
   }

   protected StoreInstruction(short opcode, short c_tag, int n) {
      super(opcode, c_tag, n);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitPopInstruction(this);
      v.visitStoreInstruction(this);
      v.visitTypedInstruction(this);
      v.visitLocalVariableInstruction(this);
      v.visitStoreInstruction(this);
   }
}
