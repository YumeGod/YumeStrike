package org.apache.bcel.verifier.structurals;

import org.apache.bcel.generic.InstructionHandle;

public interface Subroutine {
   InstructionHandle[] getEnteringJsrInstructions();

   InstructionHandle getLeavingRET();

   InstructionHandle[] getInstructions();

   boolean contains(InstructionHandle var1);

   int[] getAccessedLocalsIndices();

   int[] getRecursivelyAccessedLocalsIndices();

   Subroutine[] subSubs();
}
