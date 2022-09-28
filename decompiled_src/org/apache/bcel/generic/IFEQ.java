package org.apache.bcel.generic;

public class IFEQ extends IfInstruction {
   IFEQ() {
   }

   public IFEQ(InstructionHandle target) {
      super((short)153, target);
   }

   public IfInstruction negate() {
      return new IFNE(super.target);
   }

   public void accept(Visitor v) {
      v.visitStackConsumer(this);
      v.visitBranchInstruction(this);
      v.visitIfInstruction(this);
      v.visitIFEQ(this);
   }
}
