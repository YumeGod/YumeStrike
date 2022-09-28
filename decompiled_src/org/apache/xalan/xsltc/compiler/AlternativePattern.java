package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class AlternativePattern extends Pattern {
   private final Pattern _left;
   private final Pattern _right;

   public AlternativePattern(Pattern left, Pattern right) {
      this._left = left;
      this._right = right;
   }

   public void setParser(Parser parser) {
      super.setParser(parser);
      this._left.setParser(parser);
      this._right.setParser(parser);
   }

   public Pattern getLeft() {
      return this._left;
   }

   public Pattern getRight() {
      return this._right;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this._left.typeCheck(stable);
      this._right.typeCheck(stable);
      return null;
   }

   public double getPriority() {
      double left = this._left.getPriority();
      double right = this._right.getPriority();
      return left < right ? left : right;
   }

   public String toString() {
      return "alternative(" + this._left + ", " + this._right + ')';
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      InstructionList il = methodGen.getInstructionList();
      this._left.translate(classGen, methodGen);
      InstructionHandle gotot = il.append((BranchInstruction)(new GOTO((InstructionHandle)null)));
      il.append(methodGen.loadContextNode());
      this._right.translate(classGen, methodGen);
      this._left._trueList.backPatch(gotot);
      this._left._falseList.backPatch(gotot.getNext());
      super._trueList.append(this._right._trueList.add(gotot));
      super._falseList.append(this._right._falseList);
   }
}
