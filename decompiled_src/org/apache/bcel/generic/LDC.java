package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.ExceptionConstants;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantFloat;
import org.apache.bcel.classfile.ConstantInteger;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.util.ByteSequence;

public class LDC extends CPInstruction implements PushInstruction, ExceptionThrower, TypedInstruction {
   LDC() {
   }

   public LDC(int index) {
      super((short)19, index);
      this.setSize();
   }

   protected final void setSize() {
      if (super.index <= 255) {
         super.opcode = 18;
         super.length = 2;
      } else {
         super.opcode = 19;
         super.length = 3;
      }

   }

   public void dump(DataOutputStream out) throws IOException {
      out.writeByte(super.opcode);
      if (super.length == 2) {
         out.writeByte(super.index);
      } else {
         out.writeShort(super.index);
      }

   }

   public final void setIndex(int index) {
      super.setIndex(index);
      this.setSize();
   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      super.length = 2;
      super.index = bytes.readUnsignedByte();
   }

   public Object getValue(ConstantPoolGen cpg) {
      Constant c = cpg.getConstantPool().getConstant(super.index);
      switch (c.getTag()) {
         case 3:
            return new Integer(((ConstantInteger)c).getBytes());
         case 4:
            return new Float(((ConstantFloat)c).getBytes());
         case 8:
            int i = ((ConstantString)c).getStringIndex();
            c = cpg.getConstantPool().getConstant(i);
            return ((ConstantUtf8)c).getBytes();
         default:
            throw new RuntimeException("Unknown or invalid constant type at " + super.index);
      }
   }

   public Type getType(ConstantPoolGen cpg) {
      switch (cpg.getConstantPool().getConstant(super.index).getTag()) {
         case 3:
            return Type.INT;
         case 4:
            return Type.FLOAT;
         case 8:
            return Type.STRING;
         default:
            throw new RuntimeException("Unknown or invalid constant type at " + super.index);
      }
   }

   public Class[] getExceptions() {
      return ExceptionConstants.EXCS_STRING_RESOLUTION;
   }

   public void accept(Visitor v) {
      v.visitStackProducer(this);
      v.visitPushInstruction(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitCPInstruction(this);
      v.visitLDC(this);
   }
}
