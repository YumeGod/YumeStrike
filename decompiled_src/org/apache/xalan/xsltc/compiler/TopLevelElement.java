package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

class TopLevelElement extends SyntaxTreeNode {
   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      return this.typeCheckContents(stable);
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ErrorMsg msg = new ErrorMsg("NOT_IMPLEMENTED_ERR", this.getClass(), this);
      this.getParser().reportError(2, msg);
   }

   public InstructionList compile(ClassGenerator classGen, MethodGenerator methodGen) {
      InstructionList save = methodGen.getInstructionList();
      InstructionList result;
      methodGen.setInstructionList(result = new InstructionList());
      this.translate(classGen, methodGen);
      methodGen.setInstructionList(save);
      return result;
   }

   public void display(int indent) {
      this.indent(indent);
      Util.println("TopLevelElement");
      this.displayContents(indent + 4);
   }
}
