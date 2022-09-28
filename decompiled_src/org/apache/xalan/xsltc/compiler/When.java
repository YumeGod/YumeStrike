package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

final class When extends Instruction {
   private Expression _test;
   private boolean _ignore = false;

   public void display(int indent) {
      this.indent(indent);
      Util.println("When");
      this.indent(indent + 4);
      System.out.print("test ");
      Util.println(this._test.toString());
      this.displayContents(indent + 4);
   }

   public Expression getTest() {
      return this._test;
   }

   public boolean ignore() {
      return this._ignore;
   }

   public void parseContents(Parser parser) {
      this._test = parser.parseExpression(this, "test", (String)null);
      Object result = this._test.evaluateAtCompileTime();
      if (result != null && result instanceof Boolean) {
         this._ignore = !(Boolean)result;
      }

      this.parseChildren(parser);
      if (this._test.isDummy()) {
         this.reportError(this, parser, "REQUIRED_ATTR_ERR", "test");
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
      ErrorMsg msg = new ErrorMsg("STRAY_WHEN_ERR", this);
      this.getParser().reportError(3, msg);
   }
}
