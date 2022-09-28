package org.apache.bcel.verifier.structurals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;

public class ControlFlowGraph {
   private final MethodGen method_gen;
   private final Subroutines subroutines;
   private final ExceptionHandlers exceptionhandlers;
   private Hashtable instructionContexts = new Hashtable();

   public ControlFlowGraph(MethodGen method_gen) {
      this.subroutines = new Subroutines(method_gen);
      this.exceptionhandlers = new ExceptionHandlers(method_gen);
      InstructionHandle[] instructionhandles = method_gen.getInstructionList().getInstructionHandles();

      for(int i = 0; i < instructionhandles.length; ++i) {
         this.instructionContexts.put(instructionhandles[i], new InstructionContextImpl(instructionhandles[i]));
      }

      this.method_gen = method_gen;
   }

   public InstructionContext contextOf(InstructionHandle inst) {
      InstructionContext ic = (InstructionContext)this.instructionContexts.get(inst);
      if (ic == null) {
         throw new AssertionViolatedException("InstructionContext requested for an InstructionHandle that's not known!");
      } else {
         return ic;
      }
   }

   public InstructionContext[] contextsOf(InstructionHandle[] insts) {
      InstructionContext[] ret = new InstructionContext[insts.length];

      for(int i = 0; i < insts.length; ++i) {
         ret[i] = this.contextOf(insts[i]);
      }

      return ret;
   }

   public InstructionContext[] getInstructionContexts() {
      InstructionContext[] ret = new InstructionContext[this.instructionContexts.values().size()];
      return (InstructionContext[])this.instructionContexts.values().toArray(ret);
   }

   public boolean isDead(InstructionHandle i) {
      return this.instructionContexts.containsKey(i);
   }

   private class InstructionContextImpl implements InstructionContext {
      private int TAG;
      private InstructionHandle instruction;
      private HashMap inFrames;
      private HashMap outFrames;
      private ArrayList executionPredecessors = null;

      public InstructionContextImpl(InstructionHandle inst) {
         if (inst == null) {
            throw new AssertionViolatedException("Cannot instantiate InstructionContextImpl from NULL.");
         } else {
            this.instruction = inst;
            this.inFrames = new HashMap();
            this.outFrames = new HashMap();
         }
      }

      public int getTag() {
         return this.TAG;
      }

      public void setTag(int tag) {
         this.TAG = tag;
      }

      public ExceptionHandler[] getExceptionHandlers() {
         return ControlFlowGraph.this.exceptionhandlers.getExceptionHandlers(this.getInstruction());
      }

      public Frame getOutFrame(ArrayList execChain) {
         this.executionPredecessors = execChain;
         InstructionContext jsr = this.lastExecutionJSR();
         Frame org = (Frame)this.outFrames.get(jsr);
         if (org == null) {
            throw new AssertionViolatedException("outFrame not set! This:\n" + this + "\nExecutionChain: " + this.getExecutionChain() + "\nOutFrames: '" + this.outFrames + "'.");
         } else {
            return org.getClone();
         }
      }

      public boolean execute(Frame inFrame, ArrayList execPreds, InstConstraintVisitor icv, ExecutionVisitor ev) {
         this.executionPredecessors = (ArrayList)execPreds.clone();
         if (this.lastExecutionJSR() == null && ControlFlowGraph.this.subroutines.subroutineOf(this.getInstruction()) != ControlFlowGraph.this.subroutines.getTopLevel()) {
            throw new AssertionViolatedException("Huh?! Am I '" + this + "' part of a subroutine or not?");
         } else if (this.lastExecutionJSR() != null && ControlFlowGraph.this.subroutines.subroutineOf(this.getInstruction()) == ControlFlowGraph.this.subroutines.getTopLevel()) {
            throw new AssertionViolatedException("Huh?! Am I '" + this + "' part of a subroutine or not?");
         } else {
            Frame inF = (Frame)this.inFrames.get(this.lastExecutionJSR());
            if (inF == null) {
               this.inFrames.put(this.lastExecutionJSR(), inFrame);
               inF = inFrame;
            } else {
               if (inF.equals(inFrame)) {
                  return false;
               }

               if (!this.mergeInFrames(inFrame)) {
                  return false;
               }
            }

            Frame workingFrame = inF.getClone();

            try {
               icv.setFrame(workingFrame);
               this.getInstruction().accept(icv);
            } catch (StructuralCodeConstraintException var8) {
               var8.extendMessage("", "\nInstructionHandle: " + this.getInstruction() + "\n");
               var8.extendMessage("", "\nExecution Frame:\n" + workingFrame);
               this.extendMessageWithFlow(var8);
               throw var8;
            }

            ev.setFrame(workingFrame);
            this.getInstruction().accept(ev);
            this.outFrames.put(this.lastExecutionJSR(), workingFrame);
            return true;
         }
      }

      public String toString() {
         String ret = this.getInstruction().toString(false) + "\t[InstructionContext]";
         return ret;
      }

      private boolean mergeInFrames(Frame inFrame) {
         Frame inF = (Frame)this.inFrames.get(this.lastExecutionJSR());
         OperandStack oldstack = inF.getStack().getClone();
         LocalVariables oldlocals = inF.getLocals().getClone();

         try {
            inF.getStack().merge(inFrame.getStack());
            inF.getLocals().merge(inFrame.getLocals());
         } catch (StructuralCodeConstraintException var6) {
            this.extendMessageWithFlow(var6);
            throw var6;
         }

         return !oldstack.equals(inF.getStack()) || !oldlocals.equals(inF.getLocals());
      }

      private String getExecutionChain() {
         String s = this.toString();

         for(int i = this.executionPredecessors.size() - 1; i >= 0; --i) {
            s = this.executionPredecessors.get(i) + "\n" + s;
         }

         return s;
      }

      private void extendMessageWithFlow(StructuralCodeConstraintException e) {
         String s = "Execution flow:\n";
         e.extendMessage("", s + this.getExecutionChain());
      }

      public InstructionHandle getInstruction() {
         return this.instruction;
      }

      private InstructionContextImpl lastExecutionJSR() {
         int size = this.executionPredecessors.size();
         int retcount = 0;

         for(int i = size - 1; i >= 0; --i) {
            InstructionContextImpl current = (InstructionContextImpl)this.executionPredecessors.get(i);
            Instruction currentlast = current.getInstruction().getInstruction();
            if (currentlast instanceof RET) {
               ++retcount;
            }

            if (currentlast instanceof JsrInstruction) {
               --retcount;
               if (retcount == -1) {
                  return current;
               }
            }
         }

         return null;
      }

      public InstructionContext[] getSuccessors() {
         return ControlFlowGraph.this.contextsOf(this._getSuccessors());
      }

      private InstructionHandle[] _getSuccessors() {
         InstructionHandle[] empty = new InstructionHandle[0];
         InstructionHandle[] single = new InstructionHandle[1];
         InstructionHandle[] pair = new InstructionHandle[2];
         Instruction inst = this.getInstruction().getInstruction();
         if (inst instanceof RET) {
            Subroutine s = ControlFlowGraph.this.subroutines.subroutineOf(this.getInstruction());
            if (s == null) {
               throw new AssertionViolatedException("Asking for successors of a RET in dead code?!");
            } else {
               throw new AssertionViolatedException("DID YOU REALLY WANT TO ASK FOR RET'S SUCCS?");
            }
         } else if (inst instanceof ReturnInstruction) {
            return empty;
         } else if (inst instanceof ATHROW) {
            return empty;
         } else if (inst instanceof JsrInstruction) {
            single[0] = ((JsrInstruction)inst).getTarget();
            return single;
         } else if (inst instanceof GotoInstruction) {
            single[0] = ((GotoInstruction)inst).getTarget();
            return single;
         } else if (inst instanceof BranchInstruction) {
            if (inst instanceof Select) {
               InstructionHandle[] matchTargets = ((Select)inst).getTargets();
               InstructionHandle[] ret = new InstructionHandle[matchTargets.length + 1];
               ret[0] = ((Select)inst).getTarget();
               System.arraycopy(matchTargets, 0, ret, 1, matchTargets.length);
               return ret;
            } else {
               pair[0] = this.getInstruction().getNext();
               pair[1] = ((BranchInstruction)inst).getTarget();
               return pair;
            }
         } else {
            single[0] = this.getInstruction().getNext();
            return single;
         }
      }
   }
}
