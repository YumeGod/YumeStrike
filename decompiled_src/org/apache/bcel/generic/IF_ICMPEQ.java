package org.apache.bcel.generic;

public class IF_ICMPEQ extends IfInstruction {
   IF_ICMPEQ() {
   }

   public IF_ICMPEQ(InstructionHandle target) {
      super((short)159, target);
   }

   public IfInstruction negate() {
      return new IF_ICMPNE(super.target);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitBranchInstruction(this);
      v.visitIfInstruction(this);
      v.visitIF_ICMPEQ(this);
   }
}
