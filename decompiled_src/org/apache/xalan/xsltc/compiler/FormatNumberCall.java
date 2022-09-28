package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class FormatNumberCall extends FunctionCall {
   private Expression _value = this.argument(0);
   private Expression _format = this.argument(1);
   private Expression _name = this.argumentCount() == 3 ? this.argument(2) : null;
   private QName _resolvedQName = null;

   public FormatNumberCall(QName fname, Vector arguments) {
      super(fname, arguments);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this.getStylesheet().numberFormattingUsed();
      Type tvalue = this._value.typeCheck(stable);
      if (!(tvalue instanceof RealType)) {
         this._value = new CastExpr(this._value, Type.Real);
      }

      Type tformat = this._format.typeCheck(stable);
      if (!(tformat instanceof StringType)) {
         this._format = new CastExpr(this._format, Type.String);
      }

      if (this.argumentCount() == 3) {
         Type tname = this._name.typeCheck(stable);
         if (this._name instanceof LiteralExpr) {
            LiteralExpr literal = (LiteralExpr)this._name;
            this._resolvedQName = this.getParser().getQNameIgnoreDefaultNs(literal.getValue());
         } else if (!(tname instanceof StringType)) {
            this._name = new CastExpr(this._name, Type.String);
         }
      }

      return super._type = Type.String;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      this._value.translate(classGen, methodGen);
      this._format.translate(classGen, methodGen);
      int fn3arg = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "formatNumber", "(DLjava/lang/String;Ljava/text/DecimalFormat;)Ljava/lang/String;");
      int get = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "getDecimalFormat", "(Ljava/lang/String;)Ljava/text/DecimalFormat;");
      il.append(classGen.loadTranslet());
      if (this._name == null) {
         il.append((CompoundInstruction)(new PUSH(cpg, "")));
      } else if (this._resolvedQName != null) {
         il.append((CompoundInstruction)(new PUSH(cpg, this._resolvedQName.toString())));
      } else {
         this._name.translate(classGen, methodGen);
      }

      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(get)));
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESTATIC(fn3arg)));
   }
}
