package org.apache.bcel.verifier.structurals;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.JsrInstruction;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.RET;
import org.apache.bcel.generic.ReturnInstruction;
import org.apache.bcel.generic.ReturnaddressType;
import org.apache.bcel.generic.Type;
import org.apache.bcel.verifier.PassVerifier;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.exc.AssertionViolatedException;
import org.apache.bcel.verifier.exc.VerifierConstraintViolatedException;

public final class Pass3bVerifier extends PassVerifier {
   private static final boolean DEBUG = true;
   private Verifier myOwner;
   private int method_no;

   public Pass3bVerifier(Verifier owner, int method_no) {
      this.myOwner = owner;
      this.method_no = method_no;
   }

   private void circulationPump(ControlFlowGraph cfg, InstructionContext start, Frame vanillaFrame, InstConstraintVisitor icv, ExecutionVisitor ev) {
      new Random();
      InstructionContextQueue icq = new InstructionContextQueue();
      start.execute(vanillaFrame, new ArrayList(), icv, ev);
      icq.add(start, new ArrayList());

      while(!icq.isEmpty()) {
         InstructionContext u = icq.getIC(0);
         ArrayList ec = icq.getEC(0);
         icq.remove(0);
         ArrayList oldchain = (ArrayList)ec.clone();
         ArrayList newchain = (ArrayList)ec.clone();
         newchain.add(u);
         InstructionContext theSuccessor;
         int s;
         if (!(u.getInstruction().getInstruction() instanceof RET)) {
            InstructionContext[] succs = u.getSuccessors();

            for(s = 0; s < succs.length; ++s) {
               theSuccessor = succs[s];
               if (theSuccessor.execute(u.getOutFrame(oldchain), newchain, icv, ev)) {
                  icq.add(theSuccessor, (ArrayList)newchain.clone());
               }
            }
         } else {
            RET ret = (RET)u.getInstruction().getInstruction();
            ReturnaddressType t = (ReturnaddressType)u.getOutFrame(oldchain).getLocals().get(ret.getIndex());
            theSuccessor = cfg.contextOf(t.getTarget());
            InstructionContext lastJSR = null;
            int skip_jsr = 0;

            for(int ss = oldchain.size() - 1; ss >= 0; --ss) {
               if (skip_jsr < 0) {
                  throw new AssertionViolatedException("More RET than JSR in execution chain?!");
               }

               if (((InstructionContext)oldchain.get(ss)).getInstruction().getInstruction() instanceof JsrInstruction) {
                  if (skip_jsr == 0) {
                     lastJSR = (InstructionContext)oldchain.get(ss);
                     break;
                  }

                  --skip_jsr;
               }

               if (((InstructionContext)oldchain.get(ss)).getInstruction().getInstruction() instanceof RET) {
                  ++skip_jsr;
               }
            }

            if (lastJSR == null) {
               throw new AssertionViolatedException("RET without a JSR before in ExecutionChain?! EC: '" + oldchain + "'.");
            }

            JsrInstruction jsr = (JsrInstruction)lastJSR.getInstruction().getInstruction();
            if (theSuccessor != cfg.contextOf(jsr.physicalSuccessor())) {
               throw new AssertionViolatedException("RET '" + u.getInstruction() + "' info inconsistent: jump back to '" + theSuccessor + "' or '" + cfg.contextOf(jsr.physicalSuccessor()) + "'?");
            }

            if (theSuccessor.execute(u.getOutFrame(oldchain), newchain, icv, ev)) {
               icq.add(theSuccessor, (ArrayList)newchain.clone());
            }
         }

         ExceptionHandler[] exc_hds = u.getExceptionHandlers();

         for(s = 0; s < exc_hds.length; ++s) {
            theSuccessor = cfg.contextOf(exc_hds[s].getHandlerStart());
            if (theSuccessor.execute(new Frame(u.getOutFrame(oldchain).getLocals(), new OperandStack(u.getOutFrame(oldchain).getStack().maxStack(), exc_hds[s].getExceptionType() == null ? Type.THROWABLE : exc_hds[s].getExceptionType())), new ArrayList(), icv, ev)) {
               icq.add(theSuccessor, new ArrayList());
            }
         }
      }

      InstructionHandle ih = start.getInstruction();

      do {
         if (ih.getInstruction() instanceof ReturnInstruction && !cfg.isDead(ih)) {
            InstructionContext ic = cfg.contextOf(ih);
            Frame f = ic.getOutFrame(new ArrayList());
            LocalVariables lvs = f.getLocals();

            for(int i = 0; i < lvs.maxLocals(); ++i) {
               if (lvs.get(i) instanceof UninitializedObjectType) {
                  this.addMessage("Warning: ReturnInstruction '" + ic + "' may leave method with an uninitialized object in the local variables array '" + lvs + "'.");
               }
            }

            OperandStack os = f.getStack();

            for(int i = 0; i < os.size(); ++i) {
               if (os.peek(i) instanceof UninitializedObjectType) {
                  this.addMessage("Warning: ReturnInstruction '" + ic + "' may leave method with an uninitialized object on the operand stack '" + os + "'.");
               }
            }
         }
      } while((ih = ih.getNext()) != null);

   }

   public VerificationResult do_verify() {
      if (!this.myOwner.doPass3a(this.method_no).equals(VerificationResult.VR_OK)) {
         return VerificationResult.VR_NOTYET;
      } else {
         JavaClass jc = Repository.lookupClass(this.myOwner.getClassName());
         ConstantPoolGen constantPoolGen = new ConstantPoolGen(jc.getConstantPool());
         InstConstraintVisitor icv = new InstConstraintVisitor();
         icv.setConstantPoolGen(constantPoolGen);
         ExecutionVisitor ev = new ExecutionVisitor();
         ev.setConstantPoolGen(constantPoolGen);
         Method[] methods = jc.getMethods();

         try {
            MethodGen mg = new MethodGen(methods[this.method_no], this.myOwner.getClassName(), constantPoolGen);
            icv.setMethodGen(mg);
            if (!mg.isAbstract() && !mg.isNative()) {
               ControlFlowGraph cfg = new ControlFlowGraph(mg);
               Frame f = new Frame(mg.getMaxLocals(), mg.getMaxStack());
               if (!mg.isStatic()) {
                  if (mg.getName().equals("<init>")) {
                     Frame._this = new UninitializedObjectType(new ObjectType(jc.getClassName()));
                     f.getLocals().set(0, Frame._this);
                  } else {
                     Frame._this = null;
                     f.getLocals().set(0, new ObjectType(jc.getClassName()));
                  }
               }

               Type[] argtypes = mg.getArgumentTypes();
               int twoslotoffset = 0;

               for(int j = 0; j < argtypes.length; ++j) {
                  if (argtypes[j] == Type.SHORT || argtypes[j] == Type.BYTE || argtypes[j] == Type.CHAR || argtypes[j] == Type.BOOLEAN) {
                     argtypes[j] = Type.INT;
                  }

                  f.getLocals().set(twoslotoffset + j + (mg.isStatic() ? 0 : 1), argtypes[j]);
                  if (argtypes[j].getSize() == 2) {
                     ++twoslotoffset;
                     f.getLocals().set(twoslotoffset + j + (mg.isStatic() ? 0 : 1), Type.UNKNOWN);
                  }
               }

               this.circulationPump(cfg, cfg.contextOf(mg.getInstructionList().getStart()), f, icv, ev);
            }
         } catch (VerifierConstraintViolatedException var12) {
            var12.extendMessage("Constraint violated in method '" + methods[this.method_no] + "':\n", "");
            return new VerificationResult(2, var12.getMessage());
         } catch (RuntimeException var13) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            var13.printStackTrace(pw);
            throw new AssertionViolatedException("Some RuntimeException occured while verify()ing class '" + jc.getClassName() + "', method '" + methods[this.method_no] + "'. Original RuntimeException's stack trace:\n---\n" + sw + "---\n");
         }

         return VerificationResult.VR_OK;
      }
   }

   public int getMethodNo() {
      return this.method_no;
   }

   private static final class InstructionContextQueue {
      private Vector ics;
      private Vector ecs;

      private InstructionContextQueue() {
         this.ics = new Vector();
         this.ecs = new Vector();
      }

      public void add(InstructionContext ic, ArrayList executionChain) {
         this.ics.add(ic);
         this.ecs.add(executionChain);
      }

      public boolean isEmpty() {
         return this.ics.isEmpty();
      }

      public void remove() {
         this.remove(0);
      }

      public void remove(int i) {
         this.ics.remove(i);
         this.ecs.remove(i);
      }

      public InstructionContext getIC(int i) {
         return (InstructionContext)this.ics.get(i);
      }

      public ArrayList getEC(int i) {
         return (ArrayList)this.ecs.get(i);
      }

      public int size() {
         return this.ics.size();
      }

      // $FF: synthetic method
      InstructionContextQueue(Object x0) {
         this();
      }
   }
}
