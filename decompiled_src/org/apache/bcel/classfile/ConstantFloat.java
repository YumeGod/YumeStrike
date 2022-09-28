package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ConstantFloat extends Constant implements ConstantObject {
   private float bytes;

   public ConstantFloat(float bytes) {
      super((byte)4);
      this.bytes = bytes;
   }

   public ConstantFloat(ConstantFloat c) {
      this(c.getBytes());
   }

   ConstantFloat(DataInputStream file) throws IOException {
      this(file.readFloat());
   }

   public void accept(Visitor v) {
      v.visitConstantFloat(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeByte(super.tag);
      file.writeFloat(this.bytes);
   }

   public final float getBytes() {
      return this.bytes;
   }

   public final void setBytes(float bytes) {
      this.bytes = bytes;
   }

   public final String toString() {
      return super.toString() + "(bytes = " + this.bytes + ")";
   }

   public Object getConstantValue(ConstantPool cp) {
      return new Float(this.bytes);
   }
}
