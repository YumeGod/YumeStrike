package org.apache.bcel.generic;

import org.apache.bcel.ExceptionConstants;

public class PUTSTATIC extends FieldInstruction implements ExceptionThrower, PopInstruction {
   PUTSTATIC() {
   }

   public PUTSTATIC(int index) {
      super((short)179, index);
   }

   public int consumeStack(ConstantPoolGen cpg) {
      return this.getFieldSize(cpg);
   }

   public Class[] getExceptions() {
      Class[] cs = new Class[1 + ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length];
      System.arraycopy(ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length);
      cs[ExceptionConstants.EXCS_FIELD_AND_METHOD_RESOLUTION.length] = ExceptionConstants.INCOMPATIBLE_CLASS_CHANGE_ERROR;
      return cs;
   }

   public void accept(Visitor v) {
      v.visitExceptionThrower(this);
      v.visitStackConsumer(this);
      v.visitPopInstruction(this);
      v.visitTypedInstruction(this);
      v.visitLoadClass(this);
      v.visitCPInstruction(this);
      v.visitFieldOrMethod(this);
      v.visitFieldInstruction(this);
      v.visitPUTSTATIC(this);
   }
}
