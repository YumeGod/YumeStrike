package org.apache.bcel.generic;

public abstract class JsrInstruction extends BranchInstruction implements UnconditionalBranch, TypedInstruction, StackProducer {
   JsrInstruction(short opcode, InstructionHandle target) {
      super(opcode, target);
   }

   JsrInstruction() {
   }

   public Type getType(ConstantPoolGen cp) {
      return new ReturnaddressType(this.physicalSuccessor());
   }

   public InstructionHandle physicalSuccessor() {
      InstructionHandle ih;
      for(ih = super.target; ih.getPrev() != null; ih = ih.getPrev()) {
      }

      while(ih.getInstruction() != this) {
         ih = ih.getNext();
      }

      InstructionHandle toThis = ih;

      do {
         if (ih == null) {
            return toThis.getNext();
         }

         ih = ih.getNext();
      } while(ih == null || ih.getInstruction() != this);

      throw new RuntimeException("physicalSuccessor() called on a shared JsrInstruction.");
   }
}
