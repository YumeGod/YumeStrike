package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public class IINC extends LocalVariableInstruction {
   private boolean wide;
   private int c;

   IINC() {
   }

   public IINC(int n, int c) {
      super.opcode = 132;
      super.length = 3;
      this.setIndex(n);
      this.setIncrement(c);
   }

   public void dump(DataOutputStream out) throws IOException {
      if (this.wide) {
         out.writeByte(196);
      }

      out.writeByte(super.opcode);
      if (this.wide) {
         out.writeShort(super.n);
         out.writeShort(this.c);
      } else {
         out.writeByte(super.n);
         out.writeByte(this.c);
      }

   }

   private final void setWide() {
      if (this.wide = super.n > 65535 || Math.abs(this.c) > 127) {
         super.length = 6;
      } else {
         super.length = 3;
      }

   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      this.wide = wide;
      if (wide) {
         super.length = 6;
         super.n = bytes.readUnsignedShort();
         this.c = bytes.readShort();
      } else {
         super.length = 3;
         super.n = bytes.readUnsignedByte();
         this.c = bytes.readByte();
      }

   }

   public String toString(boolean verbose) {
      return super.toString(verbose) + " " + this.c;
   }

   public final void setIndex(int n) {
      if (n < 0) {
         throw new ClassGenException("Negative index value: " + n);
      } else {
         super.n = n;
         this.setWide();
      }
   }

   public final int getIncrement() {
      return this.c;
   }

   public final void setIncrement(int c) {
      this.c = c;
      this.setWide();
   }

   public Type getType(ConstantPoolGen cp) {
      return Type.INT;
   }

   public void accept(Visitor v) {
      v.visitLocalVariableInstruction(this);
      v.visitIINC(this);
   }
}
