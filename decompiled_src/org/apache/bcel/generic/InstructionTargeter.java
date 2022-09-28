package org.apache.bcel.generic;

public interface InstructionTargeter {
   boolean containsTarget(InstructionHandle var1);

   void updateTarget(InstructionHandle var1, InstructionHandle var2);
}
