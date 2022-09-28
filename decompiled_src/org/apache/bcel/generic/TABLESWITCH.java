package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public class TABLESWITCH extends Select {
   TABLESWITCH() {
   }

   public TABLESWITCH(int[] match, InstructionHandle[] targets, InstructionHandle target) {
      super((short)170, match, targets, target);
      super.length = (short)(13 + super.match_length * 4);
      super.fixed_length = super.length;
   }

   public void dump(DataOutputStream out) throws IOException {
      super.dump(out);
      int low = super.match_length > 0 ? super.match[0] : 0;
      out.writeInt(low);
      int high = super.match_length > 0 ? super.match[super.match_length - 1] : 0;
      out.writeInt(high);

      for(int i = 0; i < super.match_length; ++i) {
         out.writeInt(super.indices[i] = this.getTargetOffset(super.targets[i]));
      }

   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      super.initFromFile(bytes, wide);
      int low = bytes.readInt();
      int high = bytes.readInt();
      super.match_length = high - low + 1;
      super.fixed_length = (short)(13 + super.match_length * 4);
      super.length = (short)(super.fixed_length + super.padding);
      super.match = new int[super.match_length];
      super.indices = new int[super.match_length];
      super.targets = new InstructionHandle[super.match_length];

      for(int i = low; i <= high; super.match[i - low] = i++) {
      }

      for(int i = 0; i < super.match_length; ++i) {
         super.indices[i] = bytes.readInt();
      }

   }

   public void accept(Visitor v) {
      v.visitVariableLengthInstruction(this);
      v.visitStackProducer(this);
      v.visitBranchInstruction(this);
      v.visitSelect(this);
      v.visitTABLESWITCH(this);
   }
}
