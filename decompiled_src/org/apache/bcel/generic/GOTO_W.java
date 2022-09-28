package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public class GOTO_W extends GotoInstruction {
   GOTO_W() {
   }

   public GOTO_W(InstructionHandle target) {
      super((short)200, target);
      super.length = 5;
   }

   public void dump(DataOutputStream out) throws IOException {
      super.index = this.getTargetOffset();
      out.writeByte(super.opcode);
      out.writeInt(super.index);
   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      super.index = bytes.readInt();
      super.length = 5;
   }

   public void accept(Visitor v) {
      v.visitUnconditionalBranch(this);
      v.visitBranchInstruction(this);
      v.visitGotoInstruction(this);
      v.visitGOTO_W(this);
   }
}
