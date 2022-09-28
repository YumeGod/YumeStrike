package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public final class PMGClass extends Attribute {
   private int pmg_class_index;
   private int pmg_index;

   public PMGClass(PMGClass c) {
      this(c.getNameIndex(), c.getLength(), c.getPMGIndex(), c.getPMGClassIndex(), c.getConstantPool());
   }

   PMGClass(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
      this(name_index, length, file.readUnsignedShort(), file.readUnsignedShort(), constant_pool);
   }

   public PMGClass(int name_index, int length, int pmg_index, int pmg_class_index, ConstantPool constant_pool) {
      super((byte)9, name_index, length, constant_pool);
      this.pmg_index = pmg_index;
      this.pmg_class_index = pmg_class_index;
   }

   public void accept(Visitor v) {
      System.err.println("Visiting non-standard PMGClass object");
   }

   public final void dump(DataOutputStream file) throws IOException {
      super.dump(file);
      file.writeShort(this.pmg_index);
      file.writeShort(this.pmg_class_index);
   }

   public final int getPMGClassIndex() {
      return this.pmg_class_index;
   }

   public final void setPMGClassIndex(int pmg_class_index) {
      this.pmg_class_index = pmg_class_index;
   }

   public final int getPMGIndex() {
      return this.pmg_index;
   }

   public final void setPMGIndex(int pmg_index) {
      this.pmg_index = pmg_index;
   }

   public final String getPMGName() {
      ConstantUtf8 c = (ConstantUtf8)super.constant_pool.getConstant(this.pmg_index, (byte)1);
      return c.getBytes();
   }

   public final String getPMGClassName() {
      ConstantUtf8 c = (ConstantUtf8)super.constant_pool.getConstant(this.pmg_class_index, (byte)1);
      return c.getBytes();
   }

   public final String toString() {
      return "PMGClass(" + this.getPMGName() + ", " + this.getPMGClassName() + ")";
   }

   public Attribute copy(ConstantPool constant_pool) {
      return (PMGClass)this.clone();
   }
}
