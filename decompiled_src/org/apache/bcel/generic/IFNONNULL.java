package org.apache.bcel.generic;

public class IFNONNULL extends IfInstruction {
   IFNONNULL() {
   }

   public IFNONNULL(InstructionHandle target) {
      super((short)199, target);
   }

   public IfInstruction negate() {
      return new IFNULL(super.target);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitBranchInstruction(this);
      v.visitIfInstruction(this);
      v.visitIFNONNULL(this);
   }
}
