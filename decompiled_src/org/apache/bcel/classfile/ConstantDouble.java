package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ConstantDouble extends Constant implements ConstantObject {
   private double bytes;

   public ConstantDouble(double bytes) {
      super((byte)6);
      this.bytes = bytes;
   }

   public ConstantDouble(ConstantDouble c) {
      this(c.getBytes());
   }

   ConstantDouble(DataInputStream file) throws IOException {
      this(file.readDouble());
   }

   public void accept(Visitor v) {
      v.visitConstantDouble(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeByte(super.tag);
      file.writeDouble(this.bytes);
   }

   public final double getBytes() {
      return this.bytes;
   }

   public final void setBytes(double bytes) {
      this.bytes = bytes;
   }

   public final String toString() {
      return super.toString() + "(bytes = " + this.bytes + ")";
   }

   public Object getConstantValue(ConstantPool cp) {
      return new Double(this.bytes);
   }
}
