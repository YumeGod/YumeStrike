package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ObjectType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class CastCall extends FunctionCall {
   private String _className;
   private Expression _right;

   public CastCall(QName fname, Vector arguments) {
      super(fname, arguments);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (this.argumentCount() != 2) {
         throw new TypeCheckError(new ErrorMsg("ILLEGAL_ARG_ERR", this.getName(), this));
      } else {
         Expression exp = this.argument(0);
         if (exp instanceof LiteralExpr) {
            this._className = ((LiteralExpr)exp).getValue();
            super._type = Type.newObjectType(this._className);
            this._right = this.argument(1);
            Type tright = this._right.typeCheck(stable);
            if (tright != Type.Reference && !(tright instanceof ObjectType)) {
               throw new TypeCheckError(new ErrorMsg("DATA_CONVERSION_ERR", tright, super._type, this));
            } else {
               return super._type;
            }
         } else {
            throw new TypeCheckError(new ErrorMsg("NEED_LITERAL_ERR", this.getName(), this));
         }
      }
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      this._right.translate(classGen, methodGen);
      il.append((org.apache.bcel.generic.Instruction)(new CHECKCAST(cpg.addClass(this._className))));
   }
}
