package org.apache.bcel.generic;

import org.apache.bcel.ExceptionConstants;

public class LDIV extends ArithmeticInstruction implements ExceptionThrower {
   public LDIV() {
      super((short)109);
   }

   public Class[] getExceptions() {
      return new Class[]{ExceptionConstants.ARITHMETIC_EXCEPTION};
   }

   public void accept(Visitor v) {
      v.visitExceptionThrower(this);
      v.visitTypedInstruction(this);
      v.visitStackProducer(this);
      v.visitStackConsumer(this);
      v.visitArithmeticInstruction(this);
      v.visitLDIV(this);
   }
}
