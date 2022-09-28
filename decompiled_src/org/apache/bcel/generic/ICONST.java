package org.apache.bcel.generic;

public class ICONST extends Instruction implements ConstantPushInstruction, TypedInstruction {
   private int value;

   ICONST() {
   }

   public ICONST(int i) {
      super((short)3, (short)1);
      if (i >= -1 && i <= 5) {
         super.opcode = (short)(3 + i);
         this.value = i;
      } else {
         throw new ClassGenException("ICONST can be used only for value between -1 and 5: " + i);
      }
   }

   public Number getValue() {
      return new Integer(this.value);
   }

   public Type getType(ConstantPoolGen cp) {
      return Type.INT;
   }

   public void accept(Visitor v) {
      v.visitPushInstruction(this);
      v.visitStackProducer(this);
      v.visitTypedInstruction(this);
      v.visitConstantPushInstruction(this);
      v.visitICONST(this);
   }
}
