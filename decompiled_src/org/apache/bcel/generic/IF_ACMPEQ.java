package org.apache.bcel.generic;

public class IF_ACMPEQ extends IfInstruction {
   IF_ACMPEQ() {
   }

   public IF_ACMPEQ(InstructionHandle target) {
      super((short)165, target);
   }

   public IfInstruction negate() {
      return new IF_ACMPNE(super.target);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitBranchInstruction(this);
      v.visitIfInstruction(this);
      v.visitIF_ACMPEQ(this);
   }
}
