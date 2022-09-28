package org.apache.bcel.generic;

public class ACONST_NULL extends Instruction implements PushInstruction, TypedInstruction {
   public ACONST_NULL() {
      super((short)1, (short)1);
   }

   public Type getType(ConstantPoolGen cp) {
      return Type.NULL;
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitPushInstruction(this);
      v.visitTypedInstruction(this);
      v.visitACONST_NULL(this);
   }
}
