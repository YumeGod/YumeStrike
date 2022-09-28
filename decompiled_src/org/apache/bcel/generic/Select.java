package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public abstract class Select extends BranchInstruction implements VariableLengthInstruction, StackProducer {
   protected int[] match;
   protected int[] indices;
   protected InstructionHandle[] targets;
   protected int fixed_length;
   protected int match_length;
   protected int padding = 0;

   Select() {
   }

   Select(short opcode, int[] match, InstructionHandle[] targets, InstructionHandle target) {
      super(opcode, target);
      this.targets = targets;

      for(int i = 0; i < targets.length; ++i) {
         BranchInstruction.notifyTarget((InstructionHandle)null, targets[i], this);
      }

      this.match = match;
      if ((this.match_length = match.length) != targets.length) {
         throw new ClassGenException("Match and target array have not the same length");
      } else {
         this.indices = new int[this.match_length];
      }
   }

   protected int updatePosition(int offset, int max_offset) {
      super.position += offset;
      short old_length = super.length;
      this.padding = (4 - (super.position + 1) % 4) % 4;
      super.length = (short)(this.fixed_length + this.padding);
      return super.length - old_length;
   }

   public void dump(DataOutputStream out) throws IOException {
      out.writeByte(super.opcode);

      for(int i = 0; i < this.padding; ++i) {
         out.writeByte(0);
      }

      super.index = this.getTargetOffset();
      out.writeInt(super.index);
   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      this.padding = (4 - bytes.getIndex() % 4) % 4;

      for(int i = 0; i < this.padding; ++i) {
         byte b;
         if ((b = bytes.readByte()) != 0) {
            throw new ClassGenException("Padding byte != 0: " + b);
         }
      }

      super.index = bytes.readInt();
   }

   public String toString(boolean verbose) {
      StringBuffer buf = new StringBuffer(super.toString(verbose));
      if (verbose) {
         for(int i = 0; i < this.match_length; ++i) {
            String s = "null";
            if (this.targets[i] != null) {
               s = this.targets[i].getInstruction().toString();
            }

            buf.append("(" + this.match[i] + ", " + s + " = {" + this.indices[i] + "})");
         }
      } else {
         buf.append(" ...");
      }

      return buf.toString();
   }

   public void setTarget(int i, InstructionHandle target) {
      BranchInstruction.notifyTarget(this.targets[i], target, this);
      this.targets[i] = target;
   }

   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
      boolean targeted = false;
      if (super.target == old_ih) {
         targeted = true;
         this.setTarget(new_ih);
      }

      for(int i = 0; i < this.targets.length; ++i) {
         if (this.targets[i] == old_ih) {
            targeted = true;
            this.setTarget(i, new_ih);
         }
      }

      if (!targeted) {
         throw new ClassGenException("Not targeting " + old_ih);
      }
   }

   public boolean containsTarget(InstructionHandle ih) {
      if (super.target == ih) {
         return true;
      } else {
         for(int i = 0; i < this.targets.length; ++i) {
            if (this.targets[i] == ih) {
               return true;
            }
         }

         return false;
      }
   }

   void dispose() {
      super.dispose();

      for(int i = 0; i < this.targets.length; ++i) {
         this.targets[i].removeTargeter(this);
      }

   }

   public int[] getMatchs() {
      return this.match;
   }

   public int[] getIndices() {
      return this.indices;
   }

   public InstructionHandle[] getTargets() {
      return this.targets;
   }
}
