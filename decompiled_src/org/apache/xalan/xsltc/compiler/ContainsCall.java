package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ContainsCall extends FunctionCall {
   private Expression _base = null;
   private Expression _token = null;

   public ContainsCall(QName fname, Vector arguments) {
      super(fname, arguments);
   }

   public boolean isBoolean() {
      return true;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      if (this.argumentCount() != 2) {
         throw new TypeCheckError("ILLEGAL_ARG_ERR", this.getName(), this);
      } else {
         this._base = this.argument(0);
         Type baseType = this._base.typeCheck(stable);
         if (baseType != Type.String) {
            this._base = new CastExpr(this._base, Type.String);
         }

         this._token = this.argument(1);
         Type tokenType = this._token.typeCheck(stable);
         if (tokenType != Type.String) {
            this._token = new CastExpr(this._token, Type.String);
         }

         return super._type = Type.Boolean;
      }
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      this.translateDesynthesized(classGen, methodGen);
      this.synthesize(classGen, methodGen);
   }

   public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      this._base.translate(classGen, methodGen);
      this._token.translate(classGen, methodGen);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.String", "indexOf", "(Ljava/lang/String;)I"))));
      super._falseList.add(il.append((BranchInstruction)(new IFLT((InstructionHandle)null))));
   }
}
