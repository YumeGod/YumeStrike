package org.apache.bcel.generic;

public final class PUSH implements CompoundInstruction, VariableLengthInstruction, InstructionConstants {
   private Instruction instruction;

   public PUSH(ConstantPoolGen cp, int value) {
      if (value >= -1 && value <= 5) {
         this.instruction = InstructionConstants.INSTRUCTIONS[3 + value];
      } else if (value >= -128 && value <= 127) {
         this.instruction = new BIPUSH((byte)value);
      } else if (value >= -32768 && value <= 32767) {
         this.instruction = new SIPUSH((short)value);
      } else {
         this.instruction = new LDC(cp.addInteger(value));
      }

   }

   public PUSH(ConstantPoolGen cp, boolean value) {
      this.instruction = InstructionConstants.INSTRUCTIONS[3 + (value ? 1 : 0)];
   }

   public PUSH(ConstantPoolGen cp, float value) {
      if ((double)value == 0.0) {
         this.instruction = InstructionConstants.FCONST_0;
      } else if ((double)value == 1.0) {
         this.instruction = InstructionConstants.FCONST_1;
      } else if ((double)value == 2.0) {
         this.instruction = InstructionConstants.FCONST_2;
      } else {
         this.instruction = new LDC(cp.addFloat(value));
      }

   }

   public PUSH(ConstantPoolGen cp, long value) {
      if (value == 0L) {
         this.instruction = InstructionConstants.LCONST_0;
      } else if (value == 1L) {
         this.instruction = InstructionConstants.LCONST_1;
      } else {
         this.instruction = new LDC2_W(cp.addLong(value));
      }

   }

   public PUSH(ConstantPoolGen cp, double value) {
      if (value == 0.0) {
         this.instruction = InstructionConstants.DCONST_0;
      } else if (value == 1.0) {
         this.instruction = InstructionConstants.DCONST_1;
      } else {
         this.instruction = new LDC2_W(cp.addDouble(value));
      }

   }

   public PUSH(ConstantPoolGen cp, String value) {
      if (value == null) {
         this.instruction = InstructionConstants.ACONST_NULL;
      } else {
         this.instruction = new LDC(cp.addString(value));
      }

   }

   public PUSH(ConstantPoolGen cp, Number value) {
      if (!(value instanceof Integer) && !(value instanceof Short) && !(value instanceof Byte)) {
         if (value instanceof Double) {
            this.instruction = (new PUSH(cp, value.doubleValue())).instruction;
         } else if (value instanceof Float) {
            this.instruction = (new PUSH(cp, value.floatValue())).instruction;
         } else {
            if (!(value instanceof Long)) {
               throw new ClassGenException("What's this: " + value);
            }

            this.instruction = (new PUSH(cp, value.longValue())).instruction;
         }
      } else {
         this.instruction = (new PUSH(cp, value.intValue())).instruction;
      }

   }

   public PUSH(ConstantPoolGen cp, Character value) {
      this(cp, value);
   }

   public PUSH(ConstantPoolGen cp, Boolean value) {
      this(cp, value);
   }

   public final InstructionList getInstructionList() {
      return new InstructionList(this.instruction);
   }

   public final Instruction getInstruction() {
      return this.instruction;
   }

   public String toString() {
      return this.instruction.toString() + " (PUSH)";
   }
}
