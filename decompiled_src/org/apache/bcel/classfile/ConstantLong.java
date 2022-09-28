package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ConstantLong extends Constant implements ConstantObject {
   private long bytes;

   public ConstantLong(long bytes) {
      super((byte)5);
      this.bytes = bytes;
   }

   public ConstantLong(ConstantLong c) {
      this(c.getBytes());
   }

   ConstantLong(DataInputStream file) throws IOException {
      this(file.readLong());
   }

   public void accept(Visitor v) {
      v.visitConstantLong(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeByte(super.tag);
      file.writeLong(this.bytes);
   }

   public final long getBytes() {
      return this.bytes;
   }

   public final void setBytes(long bytes) {
      this.bytes = bytes;
   }

   public final String toString() {
      return super.toString() + "(bytes = " + this.bytes + ")";
   }

   public Object getConstantValue(ConstantPool cp) {
      return new Long(this.bytes);
   }
}
