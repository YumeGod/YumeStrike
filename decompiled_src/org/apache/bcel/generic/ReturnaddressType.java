package org.apache.bcel.generic;

public class ReturnaddressType extends Type {
   public static final ReturnaddressType NO_TARGET = new ReturnaddressType();
   private InstructionHandle returnTarget;

   private ReturnaddressType() {
      super((byte)16, "<return address>");
   }

   public ReturnaddressType(InstructionHandle returnTarget) {
      super((byte)16, "<return address targeting " + returnTarget + ">");
      this.returnTarget = returnTarget;
   }

   public boolean equals(Object rat) {
      return !(rat instanceof ReturnaddressType) ? false : ((ReturnaddressType)rat).returnTarget.equals(this.returnTarget);
   }

   public InstructionHandle getTarget() {
      return this.returnTarget;
   }
}
