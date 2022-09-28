package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public abstract class BranchInstruction extends Instruction implements InstructionTargeter {
   protected int index;
   protected InstructionHandle target;
   protected int position;

   BranchInstruction() {
   }

   protected BranchInstruction(short opcode, InstructionHandle target) {
      super(opcode, (short)3);
      this.setTarget(target);
   }

   public void dump(DataOutputStream out) throws IOException {
      out.writeByte(super.opcode);
      this.index = this.getTargetOffset();
      if (Math.abs(this.index) >= 32767) {
         throw new ClassGenException("Branch target offset too large for short");
      } else {
         out.writeShort(this.index);
      }
   }

   protected int getTargetOffset(InstructionHandle target) {
      if (target == null) {
         throw new ClassGenException("Target of " + super.toString(true) + " is invalid null handle");
      } else {
         int t = target.getPosition();
         if (t < 0) {
            throw new ClassGenException("Invalid branch target position offset for " + super.toString(true) + ":" + t + ":" + target);
         } else {
            return t - this.position;
         }
      }
   }

   protected int getTargetOffset() {
      return this.getTargetOffset(this.target);
   }

   protected int updatePosition(int offset, int max_offset) {
      this.position += offset;
      return 0;
   }

   public String toString(boolean verbose) {
      String s = super.toString(verbose);
      String t = "null";
      if (verbose) {
         if (this.target != null) {
            if (this.target.getInstruction() == this) {
               t = "<points to itself>";
            } else if (this.target.getInstruction() == null) {
               t = "<null instruction!!!?>";
            } else {
               t = this.target.getInstruction().toString(false);
            }
         }
      } else if (this.target != null) {
         this.index = this.getTargetOffset();
         t = "" + (this.index + this.position);
      }

      return s + " -> " + t;
   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      super.length = 3;
      this.index = bytes.readShort();
   }

   public final int getIndex() {
      return this.index;
   }

   public InstructionHandle getTarget() {
      return this.target;
   }

   public void setTarget(InstructionHandle target) {
      notifyTarget(this.target, target, this);
      this.target = target;
   }

   static final void notifyTarget(InstructionHandle old_ih, InstructionHandle new_ih, InstructionTargeter t) {
      if (old_ih != null) {
         old_ih.removeTargeter(t);
      }

      if (new_ih != null) {
         new_ih.addTargeter(t);
      }

   }

   public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
      if (this.target == old_ih) {
         this.setTarget(new_ih);
      } else {
         throw new ClassGenException("Not targeting " + old_ih + ", but " + this.target);
      }
   }

   public boolean containsTarget(InstructionHandle ih) {
      return this.target == ih;
   }

   void dispose() {
      this.setTarget((InstructionHandle)null);
      this.index = -1;
      this.position = -1;
   }
}
