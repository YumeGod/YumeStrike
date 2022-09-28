package org.apache.bcel.verifier.structurals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ATHROW;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CodeExceptionGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GotoInstruction;
import org.apache.bcel.generic.IndexedInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.LocalVariableInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.Select;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.StructuralCodeConstraintException;

public class Subroutines {
   private Hashtable subroutines = new Hashtable();
   public final Subroutine TOPLEVEL;

   public Subroutines(MethodGen mg) {
      InstructionHandle[] all = mg.getInstructionList().getInstructionHandles();
      CodeExceptionGen[] handlers = mg.getExceptionHandlers();
      this.TOPLEVEL = new SubroutineImpl();
      HashSet sub_leaders = new HashSet();
      InstructionHandle ih = all[0];

      for(int i = 0; i < all.length; ++i) {
         Instruction inst = all[i].getInstruction();
         if (inst instanceof JsrInstruction) {
            sub_leaders.add(((JsrInstruction)inst).getTarget());
         }
      }

      Iterator iter = sub_leaders.iterator();

      while(iter.hasNext()) {
         SubroutineImpl sr = new SubroutineImpl();
         InstructionHandle astore = (InstructionHandle)iter.next();
         sr.setLocalVariable(((ASTORE)astore.getInstruction()).getIndex());
         this.subroutines.put(astore, sr);
      }

      this.subroutines.put(all[0], this.TOPLEVEL);
      sub_leaders.add(all[0]);

      for(int i = 0; i < all.length; ++i) {
         Instruction inst = all[i].getInstruction();
         if (inst instanceof JsrInstruction) {
            InstructionHandle leader = ((JsrInstruction)inst).getTarget();
            ((SubroutineImpl)this.getSubroutine(leader)).addEnteringJsrInstruction(all[i]);
         }
      }

      HashSet instructions_assigned = new HashSet();
      Hashtable colors = new Hashtable();
      iter = sub_leaders.iterator();

      while(iter.hasNext()) {
         InstructionHandle actual = (InstructionHandle)iter.next();

         for(int i = 0; i < all.length; ++i) {
            colors.put(all[i], Color.white);
         }

         colors.put(actual, Color.gray);
         ArrayList Q = new ArrayList();
         Q.add(actual);
         int i;
         if (actual == all[0]) {
            for(i = 0; i < handlers.length; ++i) {
               colors.put(handlers[i].getHandlerPC(), Color.gray);
               Q.add(handlers[i].getHandlerPC());
            }
         }

         while(Q.size() != 0) {
            InstructionHandle u = (InstructionHandle)Q.remove(0);
            InstructionHandle[] successors = getSuccessors(u);

            for(int i = 0; i < successors.length; ++i) {
               if ((Color)colors.get(successors[i]) == Color.white) {
                  colors.put(successors[i], Color.gray);
                  Q.add(successors[i]);
               }
            }

            colors.put(u, Color.black);
         }

         for(i = 0; i < all.length; ++i) {
            if (colors.get(all[i]) == Color.black) {
               ((SubroutineImpl)(actual == all[0] ? this.getTopLevel() : this.getSubroutine(actual))).addInstruction(all[i]);
               if (instructions_assigned.contains(all[i])) {
                  throw new StructuralCodeConstraintException("Instruction '" + all[i] + "' is part of more than one subroutine (or of the top level and a subroutine).");
               }

               instructions_assigned.add(all[i]);
            }
         }

         if (actual != all[0]) {
            ((SubroutineImpl)this.getSubroutine(actual)).setLeavingRET();
         }
      }

      for(int i = 0; i < handlers.length; ++i) {
         for(InstructionHandle _protected = handlers[i].getStartPC(); _protected != handlers[i].getEndPC().getNext(); _protected = _protected.getNext()) {
            Enumeration subs = this.subroutines.elements();

            while(subs.hasMoreElements()) {
               Subroutine sub = (Subroutine)subs.nextElement();
               if (sub != this.subroutines.get(all[0]) && sub.contains(_protected)) {
                  throw new StructuralCodeConstraintException("Subroutine instruction '" + _protected + "' is protected by an exception handler, '" + handlers[i] + "'. This is forbidden by the JustIce verifier due to its clear definition of subroutines.");
               }
            }
         }
      }

      this.noRecursiveCalls(this.getTopLevel(), new HashSet());
   }

   private void noRecursiveCalls(Subroutine sub, HashSet set) {
      Subroutine[] subs = sub.subSubs();

      for(int i = 0; i < subs.length; ++i) {
         int index = ((RET)subs[i].getLeavingRET().getInstruction()).getIndex();
         if (!set.add(new Integer(index))) {
            SubroutineImpl si = (SubroutineImpl)subs[i];
            throw new StructuralCodeConstraintException("Subroutine with local variable '" + si.localVariable + "', JSRs '" + si.theJSRs + "', RET '" + si.theRET + "' is called by a subroutine which uses the same local variable index as itself; maybe even a recursive call? JustIce's clean definition of a subroutine forbids both.");
         }

         this.noRecursiveCalls(subs[i], set);
         set.remove(new Integer(index));
      }

   }

   public Subroutine getSubroutine(InstructionHandle leader) {
      Subroutine ret = (Subroutine)this.subroutines.get(leader);
      if (ret == null) {
         throw new AssertionViolatedException("Subroutine requested for an InstructionHandle that is not a leader of a subroutine.");
      } else if (ret == this.TOPLEVEL) {
         throw new AssertionViolatedException("TOPLEVEL special subroutine requested; use getTopLevel().");
      } else {
         return ret;
      }
   }

   public Subroutine subroutineOf(InstructionHandle any) {
      Iterator i = this.subroutines.values().iterator();

      while(i.hasNext()) {
         Subroutine s = (Subroutine)i.next();
         if (s.contains(any)) {
            return s;
         }
      }

      System.err.println("DEBUG: Please verify '" + any + "' lies in dead code.");
      return null;
   }

   public Subroutine getTopLevel() {
      return this.TOPLEVEL;
   }

   private static InstructionHandle[] getSuccessors(InstructionHandle instruction) {
      InstructionHandle[] empty = new InstructionHandle[0];
      InstructionHandle[] single = new InstructionHandle[1];
      InstructionHandle[] pair = new InstructionHandle[2];
      Instruction inst = instruction.getInstruction();
      if (inst instanceof RET) {
         return empty;
      } else if (inst instanceof ReturnInstruction) {
         return empty;
      } else if (inst instanceof ATHROW) {
         return empty;
      } else if (inst instanceof JsrInstruction) {
         single[0] = instruction.getNext();
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
            pair[0] = instruction.getNext();
            pair[1] = ((BranchInstruction)inst).getTarget();
            return pair;
         }
      } else {
         single[0] = instruction.getNext();
         return single;
      }
   }

   public String toString() {
      return "---\n" + this.subroutines.toString() + "\n---\n";
   }

   private class SubroutineImpl implements Subroutine {
      private final int UNSET = -1;
      private int localVariable = -1;
      private HashSet instructions = new HashSet();
      private HashSet theJSRs = new HashSet();
      private InstructionHandle theRET;

      public boolean contains(InstructionHandle inst) {
         return this.instructions.contains(inst);
      }

      public String toString() {
         String ret = "Subroutine: Local variable is '" + this.localVariable + "', JSRs are '" + this.theJSRs + "', RET is '" + this.theRET + "', Instructions: '" + this.instructions.toString() + "'.";
         ret = ret + " Accessed local variable slots: '";
         int[] alv = this.getAccessedLocalsIndices();

         for(int i = 0; i < alv.length; ++i) {
            ret = ret + alv[i] + " ";
         }

         ret = ret + "'.";
         ret = ret + " Recursively (via subsub...routines) accessed local variable slots: '";
         alv = this.getRecursivelyAccessedLocalsIndices();

         for(int ix = 0; ix < alv.length; ++ix) {
            ret = ret + alv[ix] + " ";
         }

         ret = ret + "'.";
         return ret;
      }

      void setLeavingRET() {
         if (this.localVariable == -1) {
            throw new AssertionViolatedException("setLeavingRET() called for top-level 'subroutine' or forgot to set local variable first.");
         } else {
            Iterator iter = this.instructions.iterator();
            InstructionHandle ret = null;

            while(iter.hasNext()) {
               InstructionHandle actual = (InstructionHandle)iter.next();
               if (actual.getInstruction() instanceof RET) {
                  if (ret != null) {
                     throw new StructuralCodeConstraintException("Subroutine with more then one RET detected: '" + ret + "' and '" + actual + "'.");
                  }

                  ret = actual;
               }
            }

            if (ret == null) {
               throw new StructuralCodeConstraintException("Subroutine without a RET detected.");
            } else if (((RET)ret.getInstruction()).getIndex() != this.localVariable) {
               throw new StructuralCodeConstraintException("Subroutine uses '" + ret + "' which does not match the correct local variable '" + this.localVariable + "'.");
            } else {
               this.theRET = ret;
            }
         }
      }

      public InstructionHandle[] getEnteringJsrInstructions() {
         if (this == Subroutines.this.TOPLEVEL) {
            throw new AssertionViolatedException("getLeavingRET() called on top level pseudo-subroutine.");
         } else {
            InstructionHandle[] jsrs = new InstructionHandle[this.theJSRs.size()];
            return (InstructionHandle[])this.theJSRs.toArray(jsrs);
         }
      }

      public void addEnteringJsrInstruction(InstructionHandle jsrInst) {
         if (jsrInst != null && jsrInst.getInstruction() instanceof JsrInstruction) {
            if (this.localVariable == -1) {
               throw new AssertionViolatedException("Set the localVariable first!");
            } else if (this.localVariable != ((ASTORE)((JsrInstruction)jsrInst.getInstruction()).getTarget().getInstruction()).getIndex()) {
               throw new AssertionViolatedException("Setting a wrong JsrInstruction.");
            } else {
               this.theJSRs.add(jsrInst);
            }
         } else {
            throw new AssertionViolatedException("Expecting JsrInstruction InstructionHandle.");
         }
      }

      public InstructionHandle getLeavingRET() {
         if (this == Subroutines.this.TOPLEVEL) {
            throw new AssertionViolatedException("getLeavingRET() called on top level pseudo-subroutine.");
         } else {
            return this.theRET;
         }
      }

      public InstructionHandle[] getInstructions() {
         InstructionHandle[] ret = new InstructionHandle[this.instructions.size()];
         return (InstructionHandle[])this.instructions.toArray(ret);
      }

      void addInstruction(InstructionHandle ih) {
         if (this.theRET != null) {
            throw new AssertionViolatedException("All instructions must have been added before invoking setLeavingRET().");
         } else {
            this.instructions.add(ih);
         }
      }

      public int[] getRecursivelyAccessedLocalsIndices() {
         HashSet s = new HashSet();
         int[] lvs = this.getAccessedLocalsIndices();

         for(int jx = 0; jx < lvs.length; ++jx) {
            s.add(new Integer(lvs[jx]));
         }

         this._getRecursivelyAccessedLocalsIndicesHelper(s, this.subSubs());
         int[] ret = new int[s.size()];
         Iterator i = s.iterator();

         for(int j = -1; i.hasNext(); ret[j] = (Integer)i.next()) {
            ++j;
         }

         return ret;
      }

      private void _getRecursivelyAccessedLocalsIndicesHelper(HashSet s, Subroutine[] subs) {
         for(int i = 0; i < subs.length; ++i) {
            int[] lvs = subs[i].getAccessedLocalsIndices();

            for(int j = 0; j < lvs.length; ++j) {
               s.add(new Integer(lvs[j]));
            }

            if (subs[i].subSubs().length != 0) {
               this._getRecursivelyAccessedLocalsIndicesHelper(s, subs[i].subSubs());
            }
         }

      }

      public int[] getAccessedLocalsIndices() {
         HashSet acc = new HashSet();
         if (this.theRET == null && this != Subroutines.this.TOPLEVEL) {
            throw new AssertionViolatedException("This subroutine object must be built up completely before calculating accessed locals.");
         } else {
            Iterator i = this.instructions.iterator();

            while(true) {
               InstructionHandle ih;
               int idx;
               do {
                  if (!i.hasNext()) {
                     int[] ret = new int[acc.size()];
                     i = acc.iterator();

                     for(idx = -1; i.hasNext(); ret[idx] = (Integer)i.next()) {
                        ++idx;
                     }

                     return ret;
                  }

                  ih = (InstructionHandle)i.next();
               } while(!(ih.getInstruction() instanceof LocalVariableInstruction) && !(ih.getInstruction() instanceof RET));

               idx = ((IndexedInstruction)ih.getInstruction()).getIndex();
               acc.add(new Integer(idx));

               try {
                  if (ih.getInstruction() instanceof LocalVariableInstruction) {
                     int s = ((LocalVariableInstruction)ih.getInstruction()).getType((ConstantPoolGen)null).getSize();
                     if (s == 2) {
                        acc.add(new Integer(idx + 1));
                     }
                  }
               } catch (RuntimeException var6) {
                  throw new AssertionViolatedException("Oops. BCEL did not like NULL as a ConstantPoolGen object.");
               }
            }
         }
      }

      public Subroutine[] subSubs() {
         HashSet h = new HashSet();
         Iterator i = this.instructions.iterator();

         while(i.hasNext()) {
            Instruction inst = ((InstructionHandle)i.next()).getInstruction();
            if (inst instanceof JsrInstruction) {
               InstructionHandle targ = ((JsrInstruction)inst).getTarget();
               h.add(Subroutines.this.getSubroutine(targ));
            }
         }

         Subroutine[] ret = new Subroutine[h.size()];
         return (Subroutine[])h.toArray(ret);
      }

      void setLocalVariable(int i) {
         if (this.localVariable != -1) {
            throw new AssertionViolatedException("localVariable set twice.");
         } else {
            this.localVariable = i;
         }
      }

      public SubroutineImpl() {
      }
   }
}
