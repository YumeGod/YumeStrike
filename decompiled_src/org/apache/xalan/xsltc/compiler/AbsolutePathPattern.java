package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class AbsolutePathPattern extends LocationPathPattern {
   private final RelativePathPattern _left;

   public AbsolutePathPattern(RelativePathPattern left) {
      this._left = left;
      if (left != null) {
         left.setParent(this);
      }

   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      if (this._left != null) {
         this._left.setParser(parser);
      }

   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      return this._left == null ? Type.Root : this._left.typeCheck(stable);
   }

   public boolean isWildcard() {
      return false;
   }

   public StepPattern getKernelPattern() {
      return this._left != null ? this._left.getKernelPattern() : null;
   }

   public void reduceKernelPattern() {
      this._left.reduceKernelPattern();
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      if (this._left != null) {
         if (this._left instanceof StepPattern) {
            LocalVariableGen local = methodGen.addLocalVariable2("apptmp", Util.getJCRefType("I"), il.getEnd());
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((org.apache.bcel.generic.Instruction)(new ISTORE(local.getIndex())));
            this._left.translate(classGen, methodGen);
            il.append(methodGen.loadDOM());
            local.setEnd(il.append((org.apache.bcel.generic.Instruction)(new ILOAD(local.getIndex()))));
            methodGen.removeLocalVariable(local);
         } else {
            this._left.translate(classGen, methodGen);
         }
      }

      int getParent = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
      int getType = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
      InstructionHandle begin = il.append(methodGen.loadDOM());
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getParent, 2)));
      if (this._left instanceof AncestorPattern) {
         il.append(methodGen.loadDOM());
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
      }

      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(getType, 2)));
      il.append((CompoundInstruction)(new PUSH(cpg, 9)));
      BranchHandle skip = il.append((BranchInstruction)(new IF_ICMPEQ((InstructionHandle)null)));
      super._falseList.add(il.append((BranchInstruction)(new GOTO_W((InstructionHandle)null))));
      skip.setTarget(il.append(InstructionConstants.NOP));
      if (this._left != null) {
         this._left.backPatchTrueList(begin);
         if (this._left instanceof AncestorPattern) {
            AncestorPattern ancestor = (AncestorPattern)this._left;
            super._falseList.backPatch(ancestor.getLoopHandle());
         }

         super._falseList.append(this._left._falseList);
      }

   }

   public String toString() {
      return "absolutePathPattern(" + (this._left != null ? this._left.toString() : ")");
   }
}
