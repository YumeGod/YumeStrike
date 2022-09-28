package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.IOException;

public final class ConstantInterfaceMethodref extends ConstantCP {
   public ConstantInterfaceMethodref(ConstantInterfaceMethodref c) {
      super((byte)11, c.getClassIndex(), c.getNameAndTypeIndex());
   }

   ConstantInterfaceMethodref(DataInputStream file) throws IOException {
      super((byte)11, file);
   }

   public ConstantInterfaceMethodref(int class_index, int name_and_type_index) {
      super((byte)11, class_index, name_and_type_index);
   }

   public void accept(Visitor v) {
      v.visitConstantInterfaceMethodref(this);
   }
}
