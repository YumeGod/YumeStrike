package org.apache.bcel.generic;

public class DCMPL extends Instruction implements TypedInstruction, StackProducer, StackConsumer {
   public DCMPL() {
      super((short)151, (short)1);
   }

   public Type getType(ConstantPoolGen cp) {
      return Type.DOUBLE;
   }

   public void accept(Visitor v) {
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitDCMPL(this);
   }
}
