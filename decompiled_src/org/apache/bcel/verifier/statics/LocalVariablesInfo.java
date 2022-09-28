package org.apache.bcel.verifier.statics;

import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.LocalVariableInfoInconsistentException;

public class LocalVariablesInfo {
   private LocalVariableInfo[] localVariableInfos;
   private IntList instruction_offsets = new IntList();

   LocalVariablesInfo(int max_locals) {
      this.localVariableInfos = new LocalVariableInfo[max_locals];

      for(int i = 0; i < max_locals; ++i) {
         this.localVariableInfos[i] = new LocalVariableInfo();
      }

   }

   public LocalVariableInfo getLocalVariableInfo(int slot) {
      if (slot >= 0 && slot < this.localVariableInfos.length) {
         return this.localVariableInfos[slot];
      } else {
         throw new AssertionViolatedException("Slot number for local variable information out of range.");
      }
   }

   public void add(int slot, String name, int startpc, int length, Type t) throws LocalVariableInfoInconsistentException {
      if (slot >= 0 && slot < this.localVariableInfos.length) {
         this.localVariableInfos[slot].add(name, startpc, length, t);
         if (t == Type.LONG) {
            this.localVariableInfos[slot + 1].add(name, startpc, length, LONG_Upper.theInstance());
         }

         if (t == Type.DOUBLE) {
            this.localVariableInfos[slot + 1].add(name, startpc, length, DOUBLE_Upper.theInstance());
         }

      } else {
         throw new AssertionViolatedException("Slot number for local variable information out of range.");
      }
   }
}
