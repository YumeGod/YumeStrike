package org.apache.bcel.generic;

public abstract class IfInstruction extends BranchInstruction implements StackConsumer {
   IfInstruction() {
   }

   protected IfInstruction(short opcode, InstructionHandle target) {
      super(opcode, target);
   }

   public abstract IfInstruction negate();
}
