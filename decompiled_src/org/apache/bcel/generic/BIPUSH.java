package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.util.ByteSequence;

public class BIPUSH extends Instruction implements ConstantPushInstruction {
   private byte b;

   BIPUSH() {
   }

   public BIPUSH(byte b) {
      super((short)16, (short)2);
      this.b = b;
   }

   public void dump(DataOutputStream out) throws IOException {
      super.dump(out);
      out.writeByte(this.b);
   }

   public String toString(boolean verbose) {
      return super.toString(verbose) + " " + this.b;
   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      super.length = 2;
      this.b = bytes.readByte();
   }

   public Number getValue() {
      return new Integer(this.b);
   }

   public Type getType(ConstantPoolGen cp) {
      return Type.BYTE;
   }

   public void accept(Visitor v) {
      v.visitPushInstruction(this);
      v.visitStackProducer(this);
      v.visitTypedInstruction(this);
      v.visitConstantPushInstruction(this);
      v.visitBIPUSH(this);
   }
}
