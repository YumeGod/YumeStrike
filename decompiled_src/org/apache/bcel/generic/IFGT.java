package org.apache.bcel.generic;

public class IFGT extends IfInstruction {
   IFGT() {
   }

   public IFGT(InstructionHandle target) {
      super((short)157, target);
   }

   public IfInstruction negate() {
      return new IFLE(super.target);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitBranchInstruction(this);
      v.visitIfInstruction(this);
      v.visitIFGT(this);
   }
}
