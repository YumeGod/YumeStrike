package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;

public final class Deprecated extends Attribute {
   private byte[] bytes;

   public Deprecated(Deprecated c) {
      this(c.getNameIndex(), c.getLength(), c.getBytes(), c.getConstantPool());
   }

   public Deprecated(int name_index, int length, byte[] bytes, ConstantPool constant_pool) {
      super((byte)8, name_index, length, constant_pool);
      this.bytes = bytes;
   }

   Deprecated(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(name_index, length, (byte[])null, constant_pool);
      if (length > 0) {
         this.bytes = new byte[length];
         file.readFully(this.bytes);
         System.err.println("Deprecated attribute with length > 0");
      }

   }

   public void accept(Visitor v) {
      v.visitDeprecated(this);
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
      return Constants.ATTRIBUTE_NAMES[8];
   }

   public Attribute copy(ConstantPool constant_pool) {
      Deprecated c = (Deprecated)this.clone();
      if (this.bytes != null) {
         c.bytes = (byte[])this.bytes.clone();
      }

      c.constant_pool = constant_pool;
      return c;
   }
}
