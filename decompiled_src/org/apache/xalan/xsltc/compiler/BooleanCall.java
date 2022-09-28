package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class BooleanCall extends FunctionCall {
   private Expression _arg = null;

   public BooleanCall(QName fname, Vector arguments) {
      super(fname, arguments);
      this._arg = this.argument(0);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this._arg.typeCheck(stable);
      return super._type = Type.Boolean;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      this._arg.translate(classGen, methodGen);
      Type targ = this._arg.getType();
      if (!targ.identicalTo(Type.Boolean)) {
         this._arg.startIterator(classGen, methodGen);
         targ.translateTo(classGen, methodGen, Type.Boolean);
      }

   }
}
