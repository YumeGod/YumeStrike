package org.apache.bcel.generic;

import org.apache.bcel.ExceptionConstants;

public class CHECKCAST extends CPInstruction implements LoadClass, ExceptionThrower, StackProducer, StackConsumer {
   CHECKCAST() {
   }

   public CHECKCAST(int index) {
      super((short)192, index);
   }

   public Class[] getExceptions() {
      Class[] cs = new Class[1 + ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];
      System.arraycopy(ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0, cs, 0, ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
      cs[ExceptionConstants.EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = ExceptionConstants.CLASS_CAST_EXCEPTION;
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
      v.visitExceptionThrower(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitTypedInstruction(this);
      v.visitCPInstruction(this);
      v.visitCHECKCAST(this);
   }
}
