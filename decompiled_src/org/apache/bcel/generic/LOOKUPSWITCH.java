package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public class LOOKUPSWITCH extends Select {
   LOOKUPSWITCH() {
   }

   public LOOKUPSWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target) {
      super((short)171, match, targets, target);
      super.length = (short)(9 + super.match_length * 8);
      super.fixed_length = super.length;
   }

   public void dump(DataOutputStream out) throws IOException {
      super.dump(out);
      out.writeInt(super.match_length);

      for(int i = 0; i < super.match_length; ++i) {
         out.writeInt(super.match[i]);
         out.writeInt(super.indices[i] = this.getTargetOffset(super.targets[i]));
      }

   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      super.initFromFile(bytes, wide);
      super.match_length = bytes.readInt();
      super.fixed_length = (short)(9 + super.match_length * 8);
      super.length = (short)(super.fixed_length + super.padding);
      super.match = new int[super.match_length];
      super.indices = new int[super.match_length];
      super.targets = new InstructionHandle[super.match_length];

      for(int i = 0; i < super.match_length; ++i) {
         super.match[i] = bytes.readInt();
         super.indices[i] = bytes.readInt();
      }

   }

   public void accept(Visitor v) {
      v.visitVariableLengthInstruction(this);
      v.visitStackProducer(this);
      v.visitBranchInstruction(this);
      v.visitSelect(this);
      v.visitLOOKUPSWITCH(this);
   }
}
