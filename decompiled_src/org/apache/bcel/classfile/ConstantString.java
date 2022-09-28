package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ConstantString extends Constant implements ConstantObject {
   private int string_index;

   public ConstantString(ConstantString c) {
      this(c.getStringIndex());
   }

   ConstantString(DataInputStream file) throws IOException {
      this(file.readUnsignedShort());
   }

   public ConstantString(int string_index) {
      super((byte)8);
      this.string_index = string_index;
   }

   public void accept(Visitor v) {
      v.visitConstantString(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeByte(super.tag);
      file.writeShort(this.string_index);
   }

   public final int getStringIndex() {
      return this.string_index;
   }

   public final void setStringIndex(int string_index) {
      this.string_index = string_index;
   }

   public final String toString() {
      return super.toString() + "(string_index = " + this.string_index + ")";
   }

   public Object getConstantValue(ConstantPool cp) {
      Constant c = cp.getConstant(this.string_index, (byte)1);
      return ((ConstantUtf8)c).getBytes();
   }

   public String getBytes(ConstantPool cp) {
      return (String)this.getConstantValue(cp);
   }
}
