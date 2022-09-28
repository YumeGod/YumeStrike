package org.apache.bcel.generic;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.ConstantPool;

public abstract class FieldInstruction extends FieldOrMethod implements TypedInstruction {
   FieldInstruction() {
   }

   protected FieldInstruction(short opcode, int index) {
      super(opcode, index);
   }

   public String toString(ConstantPool cp) {
      return Constants.OPCODE_NAMES[super.opcode] + " " + cp.constantToString(super.index, (byte)9);
   }

   protected int getFieldSize(ConstantPoolGen cpg) {
      return this.getType(cpg).getSize();
   }

   public Type getType(ConstantPoolGen cpg) {
      return this.getFieldType(cpg);
   }

   public Type getFieldType(ConstantPoolGen cpg) {
      return Type.getType(this.getSignature(cpg));
   }

   public String getFieldName(ConstantPoolGen cpg) {
      return this.getName(cpg);
   }
}
