package org.apache.bcel.generic;

public class IF_ICMPLE extends IfInstruction {
   IF_ICMPLE() {
   }

   public IF_ICMPLE(InstructionHandle target) {
      super((short)164, target);
   }

   public IfInstruction negate() {
      return new IF_ICMPGT(super.target);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitBranchInstruction(this);
      v.visitIfInstruction(this);
      v.visitIF_ICMPLE(this);
   }
}
