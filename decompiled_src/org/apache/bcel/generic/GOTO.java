package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;

public class GOTO extends GotoInstruction implements VariableLengthInstruction {
   GOTO() {
   }

   public GOTO(InstructionHandle target) {
      super((short)167, target);
   }

   public void dump(DataOutputStream out) throws IOException {
      super.index = this.getTargetOffset();
      if (super.opcode == 167) {
         super.dump(out);
      } else {
         super.index = this.getTargetOffset();
         out.writeByte(super.opcode);
         out.writeInt(super.index);
      }

   }

   protected int updatePosition(int offset, int max_offset) {
      int i = this.getTargetOffset();
      super.position += offset;
      if (Math.abs(i) >= 32767 - max_offset) {
         super.opcode = 200;
         super.length = 5;
         return 2;
      } else {
         return 0;
      }
   }

   public void accept(Visitor v) {
      v.visitVariableLengthInstruction(this);
      v.visitUnconditionalBranch(this);
      v.visitBranchInstruction(this);
      v.visitGotoInstruction(this);
      v.visitGOTO(this);
   }
}
