package org.apache.bcel.generic;

import org.apache.bcel.classfile.ConstantCP;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantUtf8;

public abstract class FieldOrMethod extends CPInstruction implements LoadClass {
   FieldOrMethod() {
   }

   protected FieldOrMethod(short opcode, int index) {
      super(opcode, index);
   }

   public String getSignature(ConstantPoolGen cpg) {
      ConstantPool cp = cpg.getConstantPool();
      ConstantCP cmr = (ConstantCP)cp.getConstant(super.index);
      ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(cmr.getNameAndTypeIndex());
      return ((ConstantUtf8)cp.getConstant(cnat.getSignatureIndex())).getBytes();
   }

   public String getName(ConstantPoolGen cpg) {
      ConstantPool cp = cpg.getConstantPool();
      ConstantCP cmr = (ConstantCP)cp.getConstant(super.index);
      ConstantNameAndType cnat = (ConstantNameAndType)cp.getConstant(cmr.getNameAndTypeIndex());
      return ((ConstantUtf8)cp.getConstant(cnat.getNameIndex())).getBytes();
   }

   public String getClassName(ConstantPoolGen cpg) {
      ConstantPool cp = cpg.getConstantPool();
      ConstantCP cmr = (ConstantCP)cp.getConstant(super.index);
      return cp.getConstantString(cmr.getClassIndex(), (byte)7).replace('/', '.');
   }

   public ObjectType getClassType(ConstantPoolGen cpg) {
      return new ObjectType(this.getClassName(cpg));
   }

   public ObjectType getLoadClassType(ConstantPoolGen cpg) {
      return this.getClassType(cpg);
   }
}
