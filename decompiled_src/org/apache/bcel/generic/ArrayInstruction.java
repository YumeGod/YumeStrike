package org.apache.bcel.generic;

import org.apache.bcel.ExceptionConstants;

public abstract class ArrayInstruction extends Instruction implements ExceptionThrower, TypedInstruction {
   ArrayInstruction() {
   }

   protected ArrayInstruction(short opcode) {
      super(opcode, (short)1);
   }

   public Class[] getExceptions() {
      return ExceptionConstants.EXCS_ARRAY_EXCEPTION;
   }

   public Type getType(ConstantPoolGen cp) {
      switch (super.opcode) {
         case 46:
         case 79:
            return Type.INT;
         case 47:
         case 80:
            return Type.LONG;
         case 48:
         case 81:
            return Type.FLOAT;
         case 49:
         case 82:
            return Type.DOUBLE;
         case 50:
         case 83:
            return Type.OBJECT;
         case 51:
         case 84:
            return Type.BYTE;
         case 52:
         case 85:
            return Type.CHAR;
         case 53:
         case 86:
            return Type.SHORT;
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         default:
            throw new ClassGenException("Oops: unknown case in switch" + super.opcode);
      }
   }
}
