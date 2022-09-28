package org.apache.bcel.generic;

public class IFLE extends IfInstruction {
   IFLE() {
   }

   public IFLE(InstructionHandle target) {
      super((short)158, target);
   }

   public IfInstruction negate() {
      return new IFGT(super.target);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitBranchInstruction(this);
      v.visitIfInstruction(this);
      v.visitIFLE(this);
   }
}
