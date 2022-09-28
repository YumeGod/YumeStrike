package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ConstantUtf8 extends Constant {
   private String bytes;

   public ConstantUtf8(ConstantUtf8 c) {
      this(c.getBytes());
   }

   ConstantUtf8(DataInputStream file) throws IOException {
      super((byte)1);
      this.bytes = file.readUTF();
   }

   public ConstantUtf8(String bytes) {
      super((byte)1);
      this.bytes = bytes;
   }

   public void accept(Visitor v) {
      v.visitConstantUtf8(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeByte(super.tag);
      file.writeUTF(this.bytes);
   }

   public final String getBytes() {
      return this.bytes;
   }

   public final void setBytes(String bytes) {
      this.bytes = bytes;
   }

   public final String toString() {
      return super.toString() + "(\"" + Utility.replace(this.bytes, "\n", "\\n") + "\")";
   }
}
