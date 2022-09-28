package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class Synthetic extends Attribute {
   private byte[] bytes;

   public Synthetic(Synthetic c) {
      this(c.getNameIndex(), c.getLength(), c.getBytes(), c.getConstantPool());
   }

   public Synthetic(int name_index, int length, byte[] bytes, ConstantPool constant_pool) {
      super((byte)7, name_index, length, constant_pool);
      this.bytes = bytes;
   }

   Synthetic(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(name_index, length, (byte[])null, constant_pool);
      if (length > 0) {
         this.bytes = new byte[length];
         file.readFully(this.bytes);
         System.err.println("Synthetic attribute with length > 0");
      }

   }

   public void accept(Visitor v) {
      v.visitSynthetic(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      super.dump(file);
      if (super.length > 0) {
         file.write(this.bytes, 0, super.length);
      }

   }

   public final byte[] getBytes() {
      return this.bytes;
   }

   public final void setBytes(byte[] bytes) {
      this.bytes = bytes;
   }

   public final String toString() {
      StringBuffer buf = new StringBuffer("Synthetic");
      if (super.length > 0) {
         buf.append(" " + Utility.toHexString(this.bytes));
      }

      return buf.toString();
   }

   public Attribute copy(ConstantPool constant_pool) {
      Synthetic c = (Synthetic)this.clone();
      if (this.bytes != null) {
         c.bytes = (byte[])this.bytes.clone();
      }

      c.constant_pool = constant_pool;
      return c;
   }
}
