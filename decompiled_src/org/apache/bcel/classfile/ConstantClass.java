package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class ConstantClass extends Constant implements ConstantObject {
   private int name_index;

   public ConstantClass(ConstantClass c) {
      this(c.getNameIndex());
   }

   ConstantClass(DataInputStream file) throws IOException {
      this(file.readUnsignedShort());
   }

   public ConstantClass(int name_index) {
      super((byte)7);
      this.name_index = name_index;
   }

   public void accept(Visitor v) {
      v.visitConstantClass(this);
   }

   public final void dump(DataOutputStream file) throws IOException {
      file.writeByte(super.tag);
      file.writeShort(this.name_index);
   }

   public final int getNameIndex() {
      return this.name_index;
   }

   public final void setNameIndex(int name_index) {
      this.name_index = name_index;
   }

   public Object getConstantValue(ConstantPool cp) {
      Constant c = cp.getConstant(this.name_index, (byte)1);
      return ((ConstantUtf8)c).getBytes();
   }

   public String getBytes(ConstantPool cp) {
      return (String)this.getConstantValue(cp);
   }

   public final String toString() {
      return super.toString() + "(name_index = " + this.name_index + ")";
   }
}
