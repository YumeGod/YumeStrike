package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.CompoundInstruction;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class ConcatCall extends FunctionCall {
   public ConcatCall(QName fname, Vector arguments) {
      super(fname, arguments);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      for(int i = 0; i < this.argumentCount(); ++i) {
         Expression exp = this.argument(i);
         if (!exp.typeCheck(stable).identicalTo(Type.String)) {
            this.setArgument(i, new CastExpr(exp, Type.String));
         }
      }

      return super._type = Type.String;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int nArgs = this.argumentCount();
      switch (nArgs) {
         case 0:
            il.append((CompoundInstruction)(new PUSH(cpg, "")));
            break;
         case 1:
            this.argument().translate(classGen, methodGen);
            break;
         default:
            int initBuffer = cpg.addMethodref("java.lang.StringBuffer", "<init>", "()V");
            org.apache.bcel.generic.Instruction append = new INVOKEVIRTUAL(cpg.addMethodref("java.lang.StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
            int toString = cpg.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;");
            il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("java.lang.StringBuffer"))));
            il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
            il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(initBuffer)));

            for(int i = 0; i < nArgs; ++i) {
               this.argument(i).translate(classGen, methodGen);
               il.append((org.apache.bcel.generic.Instruction)append);
            }

            il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(toString)));
      }

   }
}
