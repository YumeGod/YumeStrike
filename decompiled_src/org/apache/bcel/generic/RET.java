package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public class RET extends Instruction implements IndexedInstruction, TypedInstruction {
   private boolean wide;
   private int index;

   RET() {
   }

   public RET(int index) {
      super((short)169, (short)2);
      this.setIndex(index);
   }

   public void dump(DataOutputStream out) throws IOException {
      if (this.wide) {
         out.writeByte(196);
      }

      out.writeByte(super.opcode);
      if (this.wide) {
         out.writeShort(this.index);
      } else {
         out.writeByte(this.index);
      }

   }

   private final void setWide() {
      if (this.wide = this.index > 255) {
         super.length = 4;
      } else {
         super.length = 2;
      }

   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      this.wide = wide;
      if (wide) {
         this.index = bytes.readUnsignedShort();
         super.length = 4;
      } else {
         this.index = bytes.readUnsignedByte();
         super.length = 2;
      }

   }

   public final int getIndex() {
      return this.index;
   }

   public final void setIndex(int n) {
      if (n < 0) {
         throw new ClassGenException("Negative index value: " + n);
      } else {
         this.index = n;
         this.setWide();
      }
   }

   public String toString(boolean verbose) {
      return super.toString(verbose) + " " + this.index;
   }

   public Type getType(ConstantPoolGen cp) {
      return ReturnaddressType.NO_TARGET;
   }

   public void accept(Visitor v) {
      v.visitRET(this);
   }
}
