package org.apache.bcel.generic;

import org.apache.bcel.ExceptionConstants;

public abstract class ReturnInstruction extends Instruction implements ExceptionThrower, TypedInstruction, StackConsumer {
   ReturnInstruction() {
   }

   protected ReturnInstruction(short opcode) {
      super(opcode, (short)1);
   }

   public Type getType() {
      switch (super.opcode) {
         case 172:
            return Type.INT;
         case 173:
            return Type.LONG;
         case 174:
            return Type.FLOAT;
         case 175:
            return Type.DOUBLE;
         case 176:
            return Type.OBJECT;
         case 177:
            return Type.VOID;
         default:
            throw new ClassGenException("Unknown type " + super.opcode);
      }
   }

   public Class[] getExceptions() {
      return new Class[]{ExceptionConstants.ILLEGAL_MONITOR_STATE};
   }

   public Type getType(ConstantPoolGen cp) {
      return this.getType();
   }
}
