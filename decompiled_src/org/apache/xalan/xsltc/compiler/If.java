package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class If extends Instruction {
   private Expression _test;
   private boolean _ignore = false;

   public void display(int indent) {
      this.indent(indent);
      Util.println("If");
      this.indent(indent + 4);
      System.out.print("test ");
      Util.println(this._test.toString());
      this.displayContents(indent + 4);
   }

   public void parseContents(Parser parser) {
      this._test = parser.parseExpression(this, "test", (String)null);
      if (this._test.isDummy()) {
         this.reportError(this, parser, "REQUIRED_ATTR_ERR", "test");
      } else {
         Object result = this._test.evaluateAtCompileTime();
         if (result != null && result instanceof Boolean) {
            this._ignore = !(Boolean)result;
         }

         this.parseChildren(parser);
      }
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (!(this._test.typeCheck(stable) instanceof BooleanType)) {
         this._test = new CastExpr(this._test, Type.Boolean);
      }

      if (!this._ignore) {
         this.typeCheckContents(stable);
      }

      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      InstructionList il = methodGen.getInstructionList();
      this._test.translateDesynthesized(classGen, methodGen);
      InstructionHandle truec = il.getEnd();
      if (!this._ignore) {
         this.translateContents(classGen, methodGen);
      }

      this._test.backPatchFalseList(il.append(InstructionConstants.NOP));
      this._test.backPatchTrueList(truec.getNext());
   }
}
