package org.apache.bcel.generic;

public class DCMPG extends Instruction implements TypedInstruction, StackProducer, StackConsumer {
   public DCMPG() {
      super((short)152, (short)1);
   }

   public Type getType(ConstantPoolGen cp) {
      return Type.DOUBLE;
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitDCMPG(this);
   }
}
