package org.apache.bcel.generic;

public class IFGE extends IfInstruction {
   IFGE() {
   }

   public IFGE(InstructionHandle target) {
      super((short)156, target);
   }

   public IfInstruction negate() {
      return new IFLT(super.target);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitBranchInstruction(this);
      v.visitIfInstruction(this);
      v.visitIFGE(this);
   }
}
