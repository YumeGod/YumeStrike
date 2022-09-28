package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class AncestorPattern extends RelativePathPattern {
   private final Pattern _left;
   private final RelativePathPattern _right;
   private InstructionHandle _loop;

   public AncestorPattern(RelativePathPattern right) {
      this((Pattern)null, right);
   }

   public AncestorPattern(Pattern left, RelativePathPattern right) {
      this._left = left;
      (this._right = right).setParent(this);
      if (left != null) {
         left.setParent(this);
      }

   }

   public InstructionHandle getLoopHandle() {
      return this._loop;
   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      if (this._left != null) {
         this._left.setParser(parser);
      }

      this._right.setParser(parser);
   }

   public boolean isWildcard() {
      return false;
   }

   public StepPattern getKernelPattern() {
      return this._right.getKernelPattern();
   }

   public void reduceKernelPattern() {
      this._right.reduceKernelPattern();
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (this._left != null) {
         this._left.typeCheck(stable);
      }

      return this._right.typeCheck(stable);
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      LocalVariableGen local = methodGen.addLocalVariable2("app", Util.getJCRefType("I"), il.getEnd());
      org.apache.bcel.generic.Instruction loadLocal = new ILOAD(local.getIndex());
      org.apache.bcel.generic.Instruction storeLocal = new ISTORE(local.getIndex());
      if (this._right instanceof StepPattern) {
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((org.apache.bcel.generic.Instruction)storeLocal);
         this._right.translate(classGen, methodGen);
         il.append(methodGen.loadDOM());
         il.append((org.apache.bcel.generic.Instruction)loadLocal);
      } else {
         this._right.translate(classGen, methodGen);
         if (this._right instanceof AncestorPattern) {
            il.append(methodGen.loadDOM());
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
         }
      }

      if (this._left != null) {
         int getParent = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
         InstructionHandle parent = il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getParent, 2)));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((org.apache.bcel.generic.Instruction)storeLocal);
         super._falseList.add(il.append((BranchInstruction)(new IFLT((InstructionHandle)null))));
         il.append((org.apache.bcel.generic.Instruction)loadLocal);
         this._left.translate(classGen, methodGen);
         SyntaxTreeNode p = this.getParent();
         if (p != null && !(p instanceof Instruction) && !(p instanceof TopLevelElement)) {
            il.append((org.apache.bcel.generic.Instruction)loadLocal);
         }

         BranchHandle exit = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
         this._loop = il.append(methodGen.loadDOM());
         il.append((org.apache.bcel.generic.Instruction)loadLocal);
         local.setEnd(this._loop);
         il.append((BranchInstruction)(new GOTO(parent)));
         exit.setTarget(il.append(InstructionConstants.NOP));
         this._left.backPatchFalseList(this._loop);
         super._trueList.append(this._left._trueList);
      } else {
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.POP2);
      }

      if (this._right instanceof AncestorPattern) {
         AncestorPattern ancestor = (AncestorPattern)this._right;
         super._falseList.backPatch(ancestor.getLoopHandle());
      }

      super._trueList.append(this._right._trueList);
      super._falseList.append(this._right._falseList);
   }

   public String toString() {
      return "AncestorPattern(" + this._left + ", " + this._right + ')';
   }
}
