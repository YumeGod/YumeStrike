package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.IOException;

public final class ConstantMethodref extends ConstantCP {
   public ConstantMethodref(ConstantMethodref c) {
      super((byte)10, c.getClassIndex(), c.getNameAndTypeIndex());
   }

   ConstantMethodref(DataInputStream file) throws IOException {
      super((byte)10, file);
   }

   public ConstantMethodref(int class_index, int name_and_type_index) {
      super((byte)10, class_index, name_and_type_index);
   }

   public void accept(Visitor v) {
      v.visitConstantMethodref(this);
   }
}
