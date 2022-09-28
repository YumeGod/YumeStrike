package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ProcessingInstructionPattern extends StepPattern {
   private String _name = null;
   private boolean _typeChecked = false;

   public ProcessingInstructionPattern(String name) {
      super(3, 7, (Vector)null);
      this._name = name;
   }

   public double getDefaultPriority() {
      return this._name != null ? 0.0 : -0.5;
   }

   public String toString() {
      return super._predicates == null ? "processing-instruction(" + this._name + ")" : "processing-instruction(" + this._name + ")" + super._predicates;
   }

   public void reduceKernelPattern() {
      this._typeChecked = true;
   }

   public boolean isWildcard() {
      return false;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (this.hasPredicates()) {
         int n = super._predicates.size();

         for(int i = 0; i < n; ++i) {
            Predicate pred = (Predicate)super._predicates.elementAt(i);
            pred.typeCheck(stable);
         }
      }

      return Type.NodeSet;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int gname = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeName", "(I)Ljava/lang/String;");
      int cmp = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
      il.append(methodGen.loadCurrentNode());
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.SWAP);
      il.append(methodGen.storeCurrentNode());
      int n;
      if (!this._typeChecked) {
         il.append(methodGen.loadCurrentNode());
         n = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
         il.append(methodGen.loadDOM());
         il.append(methodGen.loadCurrentNode());
         il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(n, 2)));
         il.append((CompoundInstruction)(new PUSH(cpg, 7)));
         super._falseList.add(il.append((BranchInstruction)(new IF_ICMPEQ((InstructionHandle)null))));
      }

      il.append((CompoundInstruction)(new PUSH(cpg, this._name)));
      il.append(methodGen.loadDOM());
      il.append(methodGen.loadCurrentNode());
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEINTERFACE(gname, 2)));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cmp)));
      super._falseList.add(il.append((BranchInstruction)(new IFEQ((InstructionHandle)null))));
      if (this.hasPredicates()) {
         n = super._predicates.size();

         for(int i = 0; i < n; ++i) {
            Predicate pred = (Predicate)super._predicates.elementAt(i);
            Expression exp = pred.getExpr();
            exp.translateDesynthesized(classGen, methodGen);
            super._trueList.append(exp._trueList);
            super._falseList.append(exp._falseList);
         }
      }

      InstructionHandle restore = il.append(methodGen.storeCurrentNode());
      this.backPatchTrueList(restore);
      BranchHandle skipFalse = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
      restore = il.append(methodGen.storeCurrentNode());
      this.backPatchFalseList(restore);
      super._falseList.add(il.append((BranchInstruction)(new GOTO((InstructionHandle)null))));
      skipFalse.setTarget(il.append(InstructionConstants.NOP));
   }
}
