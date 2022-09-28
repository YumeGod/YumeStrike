package org.apache.bcel.generic;

public final class TargetLostException extends Exception {
   private InstructionHandle[] targets;

   TargetLostException(InstructionHandle[] t, String mesg) {
      super(mesg);
      this.targets = t;
   }

   public InstructionHandle[] getTargets() {
      return this.targets;
   }
}
