package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public class SIPUSH extends Instruction implements ConstantPushInstruction {
   private short b;

   SIPUSH() {
   }

   public SIPUSH(short b) {
      super((short)17, (short)3);
      this.b = b;
   }

   public void dump(DataOutputStream out) throws IOException {
      super.dump(out);
      out.writeShort(this.b);
   }

   public String toString(boolean verbose) {
      return super.toString(verbose) + " " + this.b;
   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      super.length = 3;
      this.b = bytes.readShort();
   }

   public Number getValue() {
      return new Integer(this.b);
   }

   public Type getType(ConstantPoolGen cp) {
      return Type.SHORT;
   }

   public void accept(Visitor v) {
      v.visitPushInstruction(this);
      v.visitStackProducer(this);
      v.visitTypedInstruction(this);
      v.visitConstantPushInstruction(this);
      v.visitSIPUSH(this);
   }
}
