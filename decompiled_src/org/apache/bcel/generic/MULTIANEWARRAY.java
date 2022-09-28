package org.apache.bcel.generic;

import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.ExceptionConstants;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.util.ByteSequence;

public class MULTIANEWARRAY extends CPInstruction implements LoadClass, AllocationInstruction, ExceptionThrower {
   private short dimensions;

   MULTIANEWARRAY() {
   }

   public MULTIANEWARRAY(int index, short dimensions) {
      super((short)197, index);
      if (dimensions < 1) {
         throw new ClassGenException("Invalid dimensions value: " + dimensions);
      } else {
         this.dimensions = dimensions;
         super.length = 4;
      }
   }

   public void dump(DataOutputStream out) throws IOException {
      out.writeByte(super.opcode);
      out.writeShort(super.index);
      out.writeByte(this.dimensions);
   }

   protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException {
      super.initFromFile(bytes, wide);
      this.dimensions = (short)bytes.readByte();
      super.length = 4;
   }

   public final short getDimensions() {
      return this.dimensions;
   }

   public String toString(boolean verbose) {
      return super.toString(verbose) + " " + super.index + " " + this.dimensions;
   }

   public String toString(ConstantPool cp) {
      return super.toString(cp) + " " + this.dimensions;
   }

   public int consumeStack(ConstantPoolGen cpg) {
      return this.dimensions;
   }

   public Class[] getExceptions() {
      Class[] cs = new Class[2 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
      System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
      cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length + 1] = ExceptionConstants.NEGATIVE_ARRAY_SIZE_EXCEPTION;
      cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.ILLEGAL_ACCESS_ERROR;
      return cs;
   }

   public ObjectType getLoadClassType(ConstantPoolGen cpg) {
      Type t = this.getType(cpg);
      if (t instanceof ArrayType) {
         t = ((ArrayType)t).getBasicType();
      }

      return t instanceof ObjectType ? (ObjectType)t : null;
   }

   public void accept(Visitor v) {
      v.visitLoadClass(this);
      v.visitAllocationInstruction(this);
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitCPInstruction(this);
      v.visitMULTIANEWARRAY(this);
   }
}
